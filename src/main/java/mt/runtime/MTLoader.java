package mt.runtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import mt.exceptions.MTFileException;
import mt.interpreter.MTInterpreter;
import mt.lexer.MTLexer;
import mt.parser.MTParser;

public final class MTLoader {

    private final MTScope globalScope;

    public MTLoader(
            MTScope globalScope) {

        this.globalScope = globalScope;
    }

    public MTObject loadFile(
            Path path) {

        try {

            String source =
                    Files.readString(path);

            MTLexer lexer =
                    new MTLexer(source);

            MTParser parser =
                    new MTParser(
                            lexer.tokenize());

            MTInterpreter interpreter =
                    new MTInterpreter();

            return interpreter.evaluate(
                    parser.parse(),
                    globalScope);
        }
        catch (IOException ex) {

            throw new MTFileException(
                    "Cannot load file: "
                    + path,
                    ex);
        }
    }
}
