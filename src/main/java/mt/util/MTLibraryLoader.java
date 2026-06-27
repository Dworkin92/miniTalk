package mt.util;

import mt.util.MTModuleDependencyResolver;
import mt.interpreter.MTInterpreter;
import mt.lexer.MTLexer;
import mt.parser.MTParser;
import mt.runtime.MTNil;
import mt.runtime.MTObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class MTLibraryLoader {

    public static final String ENV_MT_HOME = "MT_HOME";
    public static final String ENV_MT_STDLIB_PATH = "MT_STDLIB_PATH";
    public static final String DEFAULT_STDLIB_DIR = "stdlib";
    public static final String HASH_ALGORITHM = "SHA-256";

    private static final Map<MTInterpreter, LoaderState> STATES =
            Collections.synchronizedMap(new IdentityHashMap<>());

    private MTLibraryLoader() {
    }

    public static List<Path> resolveSearchRoots(List<String> cliPaths) {
        LinkedHashSet<Path> ordered = new LinkedHashSet<>();

        String mtHome = System.getenv(ENV_MT_HOME);
        if (mtHome != null && !mtHome.isBlank()) {
            addRoot(ordered, Path.of(mtHome, DEFAULT_STDLIB_DIR));
        } else {
            addRoot(ordered, Path.of(DEFAULT_STDLIB_DIR));
        }

        String envPaths = System.getenv(ENV_MT_STDLIB_PATH);
        if (envPaths != null && !envPaths.isBlank()) {
            String separator = Pattern.quote(File.pathSeparator);
            for (String entry : envPaths.split(separator)) {
                if (entry != null && !entry.isBlank()) {
                    addRoot(ordered, Path.of(entry.trim()));
                }
            }
        }

        for (String cliPath : cliPaths) {
            if (cliPath != null && !cliPath.isBlank()) {
                addRoot(ordered, Path.of(cliPath.trim()));
            }
        }

        return List.copyOf(ordered);
    }

    public static void loadAllAtStartup(MTInterpreter interpreter, List<Path> roots) {
        LoaderState state = stateFor(interpreter, roots);
        synchronized (state) {
            for (Path root : roots) {
                state.roots.add(normalize(root));
                loadRoot(root, interpreter, state);
            }
        }
    }

    public static MTObject loadNamedLibrary(String name, MTInterpreter interpreter, List<Path> roots) {
        String targetName = name.endsWith(".mt") ? name : name + ".mt";
        LoaderState state = stateFor(interpreter, roots);
        MTObject lastResult = MTNil.INSTANCE;
        boolean found = false;

        synchronized (state) {
            for (Path root : roots) {
                state.roots.add(normalize(root));
                for (Path candidate : listLibraryFilesNamed(root, targetName)) {
                    found = true;
                    MTObject result = loadCandidate(candidate, interpreter, state);
                    if (result != MTNil.INSTANCE) {
                        lastResult = result;
                    }
                }
            }
        }

        if (!found) {
            throw new RuntimeException("Librairie introuvable: " + name);
        }

        return lastResult;
    }

    public static List<Path> loadedRoots(MTInterpreter interpreter) {
        LoaderState state = STATES.get(interpreter);
        if (state == null) {
            return List.of();
        }
        synchronized (state) {
            return List.copyOf(state.roots);
        }
    }

    public static List<LoadedLibrary> loadedLibraries(MTInterpreter interpreter) {
        LoaderState state = STATES.get(interpreter);
        if (state == null) {
            return List.of();
        }
        synchronized (state) {
            return List.copyOf(state.loadedByPath.values());
        }
    }

    public static MTObject executeFile(Path file, MTInterpreter interpreter) throws IOException {
        String source = Files.readString(file);
        return executeSource(source, interpreter);
    }

    public static MTObject executeSource(String source, MTInterpreter interpreter) {
        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        return interpreter.eval(parser.parseProgram());
    }

    private static LoaderState stateFor(MTInterpreter interpreter, List<Path> roots) {
        LoaderState state = STATES.computeIfAbsent(interpreter, ignored -> new LoaderState());
        synchronized (state) {
            for (Path root : roots) {
                state.roots.add(normalize(root));
            }
        }
        return state;
    }

private static void loadRoot(Path root, MTInterpreter interpreter, LoaderState state) {

    List<Path> files = listLibraryFiles(root);

    boolean debug = true; // <-- active/désactive ici

    MTModuleDependencyResolver resolver =
            new MTModuleDependencyResolver(debug);

    List<Path> ordered = resolver.resolve(files);

    for (Path file : ordered) {
        loadCandidate(file, interpreter, state);
    }
}

    private static MTObject loadCandidate(Path file, MTInterpreter interpreter, LoaderState state) {
        try {
            Path normalized = normalize(file);
            String name = normalized.getFileName().toString();

            if (state.loadedByPath.containsKey(normalized)) {
                System.out.println("[stdlib:skip:path] " + normalized);
                return MTNil.INSTANCE;
            }

            String hash = sha256(normalized);
            LoadedLibrary previous = state.loadedByName.get(name);

            if (previous != null && previous.sha256().equals(hash)) {
                state.loadedByPath.put(normalized, new LoadedLibrary(name, normalized, hash));
                System.out.println("[stdlib:skip:hash] " + normalized + " (même contenu que " + previous.path() + ")");
                return MTNil.INSTANCE;
            }

            MTObject result = executeFile(normalized, interpreter);
            LoadedLibrary current = new LoadedLibrary(name, normalized, hash);
            state.loadedByPath.put(normalized, current);
            state.loadedByName.put(name, current);

            if (previous == null) {
                System.out.println("[stdlib:load] " + normalized);
            } else {
                System.out.println("[stdlib:override] " + name + " : " + previous.path() + " -> " + normalized);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Erreur chargement librairie: " + file + " -> " + e.getMessage(), e);
        }
    }

    private static List<Path> listLibraryFiles(Path root) {
        Path normalizedRoot = normalize(root);
        if (!Files.isDirectory(normalizedRoot)) {
            return List.of();
        }

        try (Stream<Path> stream = Files.walk(normalizedRoot)) {
            return stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".mt"))
                    .sorted(Comparator
                            .comparing((Path path) -> safeRelative(normalizedRoot, path).toString())
                            .thenComparing(path -> path.toAbsolutePath().normalize().toString()))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Erreur parcours librairies: " + normalizedRoot, e);
        }
    }

    private static List<Path> listLibraryFilesNamed(Path root, String targetName) {
        Path normalizedRoot = normalize(root);
        if (!Files.isDirectory(normalizedRoot)) {
            return List.of();
        }

        try (Stream<Path> stream = Files.walk(normalizedRoot)) {
            return stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals(targetName))
                    .sorted(Comparator
                            .comparing((Path path) -> safeRelative(normalizedRoot, path).toString())
                            .thenComparing(path -> path.toAbsolutePath().normalize().toString()))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Erreur recherche librairie nommée: " + normalizedRoot, e);
        }
    }

    private static Path safeRelative(Path root, Path path) {
        try {
            return root.relativize(path);
        } catch (IllegalArgumentException e) {
            return path.toAbsolutePath().normalize();
        }
    }

    private static void addRoot(Set<Path> roots, Path path) {
        roots.add(normalize(path));
    }

    private static Path normalize(Path path) {
        return path.toAbsolutePath().normalize();
    }

    private static String sha256(Path file) {
        try {
            byte[] content = Files.readAllBytes(file);
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            return HexFormat.of().formatHex(digest.digest(content));
        } catch (Exception e) {
            throw new RuntimeException("Impossible de calculer le hash de " + file, e);
        }
    }

    private static final class LoaderState {
        private final LinkedHashSet<Path> roots = new LinkedHashSet<>();
        private final LinkedHashMap<Path, LoadedLibrary> loadedByPath = new LinkedHashMap<>();
        private final LinkedHashMap<String, LoadedLibrary> loadedByName = new LinkedHashMap<>();
    }

    public record LoadedLibrary(String name, Path path, String sha256) {
        public LoadedLibrary {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(path, "path");
            Objects.requireNonNull(sha256, "sha256");
        }
    }
}