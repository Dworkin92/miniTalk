package mt.meta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import mt.ast.MTMetaDirectiveNode;
import mt.ast.MTNode;
import mt.ast.MTSequenceNode;
import mt.lexer.MTLexer;
import mt.parser.MTParser;
import mt.runtime.MTSymbol;
import mt.runtime.MTRuntime;

public final class MTMetaResolver {

    private final Map<String, MTModule> modules =
            new HashMap<>();

    public MTNode resolve(
            MTNode ast,
            MTModule module,
            Path currentFile) {

        if (ast instanceof MTSequenceNode seq) {
            return resolveSequence(
                    seq,
                    module,
                    currentFile);
        }

        return ast;
    }

    private MTNode resolveSequence(MTSequenceNode sequence, MTModule module, Path currentFile) {

        MTSequenceNode result = new MTSequenceNode();

        for (MTSymbol temporary : sequence.getTemporaries()) {
            result.addTemporary(temporary);
        }

        for (MTNode node : sequence.getStatements()) {

            if (node instanceof MTMetaDirectiveNode meta) {

                String directive = meta.getText();

                if (directive.startsWith("module ")) {

                    String moduleName = directive.substring(7).trim();

                    module = modules.computeIfAbsent(moduleName, MTModule::new);

                    module.register(currentFile);

                    continue;
                }

                if (directive.startsWith("import ")) {

                    String fileName = directive.substring(7).trim();

                    Path importedFile = currentFile.getParent().resolve(fileName).toAbsolutePath().normalize();

                    if (module.contains(importedFile)) {
                        continue;
                    }

                    module.register(importedFile);

                    MTNode importedAst = loadAst(importedFile);

                    importedAst = resolve(importedAst, module, importedFile);

                    append(result, importedAst);

                    continue;
                }

                continue;
            }

            result.add(node);
        }

        return result;
    }

    private MTNode loadAst(
            Path file) {

        try {

            String source =
                    Files.readString(file);

            MTLexer lexer =
                    new MTLexer(source);

            MTParser parser =
                    new MTParser(
                            lexer.tokenize());

            return parser.parse();
        }
        catch (IOException ex) {

            throw new RuntimeException(ex);
        }
    }

    private void append(
            MTSequenceNode target,
            MTNode node) {

        if (node instanceof MTSequenceNode seq) {

            for (MTNode stmt : seq.getStatements()) {
                target.add(stmt);
            }
        }
        else {
            target.add(node);
        }
    }
}
