package mt.meta;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public final class MTModule {

    private final String name;

    private final Set<Path> loadedFiles =
            new HashSet<>();

    public MTModule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean contains(
            Path file) {

        return loadedFiles.contains(
                normalize(file));
    }

    public void register(
            Path file) {

        loadedFiles.add(
                normalize(file));
    }

    private Path normalize(
            Path path) {

        return path
                .toAbsolutePath()
                .normalize();
    }
}
