package mt.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class MTModuleDependencyResolver {

    private final boolean debug;

    public MTModuleDependencyResolver(boolean debug) {
        this.debug = debug;
    }

    public List<Path> resolve(List<Path> files) {
        Map<String, Module> modules = new HashMap<>();

        // 1. parsing
        for (Path file : files) {
            Module m = parse(file);
            if (modules.containsKey(m.name)) {
                throw new RuntimeException(
                        "Module dupliqué: " + m.name + " (" + file + ")"
                );
            }
            modules.put(m.name, m);
        }

        // 2. debug : affichage graphe
        if (debug) {
            printDependencyGraph(modules);
        }

        // 3. validation
        for (Module m : modules.values()) {
            for (String dep : m.imports) {
                if (!modules.containsKey(dep)) {
                    throw new RuntimeException(
                            "Dépendance manquante: " + m.name + " -> " + dep
                    );
                }
            }
        }

        // 4. tri topo
        List<Path> result = new ArrayList<>();
        Map<String, State> state = new HashMap<>();

        for (String name : modules.keySet()) {
            visit(name, modules, state, result, new ArrayDeque<>());
        }

        return result;
    }

    private static void visit(String name,
                              Map<String, Module> modules,
                              Map<String, State> state,
                              List<Path> result,
                              Deque<String> stack) {

        State s = state.get(name);

        if (s == State.VISITING) {
            throw new RuntimeException("Cycle détecté: " + formatCycle(stack, name));
        }

        if (s == State.VISITED) return;

        state.put(name, State.VISITING);
        stack.push(name);

        for (String dep : modules.get(name).imports) {
            visit(dep, modules, state, result, stack);
        }

        stack.pop();
        state.put(name, State.VISITED);

        result.add(modules.get(name).file);
    }

    private static String formatCycle(Deque<String> stack, String repeated) {
        List<String> path = new ArrayList<>(stack);
        Collections.reverse(path);

        int start = path.indexOf(repeated);
        if (start >= 0) {
            List<String> cycle = new ArrayList<>(path.subList(start, path.size()));
            cycle.add(repeated);
            return String.join(" -> ", cycle);
        }

        return String.join(" -> ", path) + " -> " + repeated;
    }

    private Module parse(Path file) {
        try {
            List<String> lines = Files.readAllLines(file);

            String module = null;
            List<String> imports = new ArrayList<>();

            for (String line : lines) {
                String t = line.trim();

                if (t.startsWith("\"@") && t.endsWith("\"")) {
                    String content = t.substring(2, t.length() - 1);
                    String[] parts = content.split("\\s+");

                    switch (parts[0]) {
                        case "module" -> module = parts[1];
                        case "import" -> imports.add(parts[1]);
                    }
                } else if (!t.isEmpty()) {
                    break;
                }
            }

            if (module == null) {
                throw new RuntimeException("Pas de @module dans " + file);
            }

            return new Module(module, imports, file);

        } catch (Exception e) {
            throw new RuntimeException("Erreur parsing " + file + ": " + e.getMessage(), e);
        }
    }

    private void printDependencyGraph(Map<String, Module> modules) {
        System.out.println("[stdlib:deps] ===== Dependency Graph =====");

        List<String> names = new ArrayList<>(modules.keySet());
        Collections.sort(names);

        for (String name : names) {
            Module m = modules.get(name);

            if (m.imports.isEmpty()) {
                System.out.println("[stdlib:deps] " + name + " -> (none)");
            } else {
                System.out.println("[stdlib:deps] " + name + " -> " + String.join(", ", m.imports));
            }
        }

        System.out.println("[stdlib:deps] ============================");
    }

    private record Module(String name, List<String> imports, Path file) {}

    private enum State {
        VISITING,
        VISITED
    }
}
