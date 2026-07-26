package mt.runtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Iterator;

import mt.lexer.MTToken;
import mt.lexer.MTTokenType;
import mt.lexer.MTLexer;

import mt.runtime.MTRuntime;
import mt.runtime.MTScope;
import mt.ast.MTNode;

import mt.meta.MTModule;
import mt.meta.MTMetaResolver;
import mt.exceptions.MTFileException;
import mt.interpreter.MTInterpreter;
import mt.parser.MTParser;

public final class MTLoader {

    private final MTRuntime runtime;
    private final MTScope   globalScope;
    private final Set<Path> loadedFiles = new HashSet<>();

    public MTLoader(
            MTRuntime runtime,
            MTScope   globalScope) {
        this.runtime = runtime;
        this.globalScope = globalScope;
    }

    public MTObject loadFile(
            Path path) {

        path = path.toAbsolutePath().normalize();

        if (loadedFiles.contains(path)) {
            return MTNil.instance();
        }

        try {

            String source = Files.readString(path);

            loadedFiles.add(path);

            /*
            MTLexer lexer = new MTLexer(source);

            List<MTToken> tokens = lexer.tokenize();

            processMetaDirectives(tokens, path);

            MTParser parser = new MTParser(tokens);

            MTInterpreter interpreter = new MTInterpreter(runtime);

            return interpreter.evaluate(
                    parser.parse(),
                    globalScope);
                    */
            MTLexer lexer = new MTLexer(source);

            MTParser parser = new MTParser(lexer.tokenize());

            MTNode ast = parser.parse();

            MTModule module = new MTModule("anonymous");

            module.register(path);

            MTMetaResolver resolver = new MTMetaResolver();

            ast = resolver.resolve(ast, module, path);

            MTInterpreter interpreter = new MTInterpreter(runtime);

            return interpreter.evaluate(ast,globalScope);

        }
        catch (IOException ex) {

            throw new MTFileException(
                    "Cannot load file: "
                    + path,
                    ex);
        }
    }

    private void processMetaDirectives(
        List<MTToken> tokens,
        Path currentFile) {

        Iterator<MTToken> iterator = tokens.iterator();

        while (iterator.hasNext()) {
            MTToken token = iterator.next();
            if (token.type() == MTTokenType.META_DIRECTIVE) {
                executeMetaDirective(token.text(), currentFile);
                iterator.remove();
            }
        }
    }

    private void executeMetaDirective(
        String directive,
        Path currentFile) {

        String[] parts = directive.split("\\s+", 2);

        if (parts.length == 0) {
            return;
        }

        String keyword = parts[0];

        switch (keyword) {

            case "module" -> {

                if (parts.length < 2) {
                    throw new MTFileException("Missing module name");
                }

                System.out.println("Module: " + parts[1]);
            }

            case "import" -> {

                if (parts.length < 2) {
                    throw new MTFileException("Missing import file");
                }

                Path parent = currentFile.getParent();

                Path importedFile = (parent != null) ? parent.resolve(parts[1]) : Path.of(parts[1]);

                loadFile(importedFile.normalize().toAbsolutePath());
            }

            default ->
                throw new MTFileException("Unknown meta directive: " + directive);
        }
    }

}
