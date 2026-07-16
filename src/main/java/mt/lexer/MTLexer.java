package mt.lexer;

import java.util.ArrayList;
import java.util.List;
import mt.exceptions.MTRuntimeException;

public final class MTLexer {

    private final String source;

    private final List<MTToken> tokens = new ArrayList<>();

    private int position = 0;
    private int line = 1;
    private int column = 1;

    private int commentDepth = 0;

    public MTLexer(String source) {
        this.source = source;
    }

    public List<MTToken> tokenize() {

        while (!isAtEnd()) {

            skipWhitespace();

            if (isAtEnd()) {
                break;
            }

            char c = peek();

            /*
             * Commentaires
             */
            if (c == '/' && peekNext() == '*') {
                readCommentStart();
                continue;
            }

            if (c == '*' && peekNext() == '/') {
                readCommentEnd();
                continue;
            }

            /*
             * Directives META
             */
            if (insideComment() && c == '@') {
                readMeta();
                continue;
            }

            /*
             * Litteraux
             */
            if (Character.isDigit(c)) {
                readInteger();
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                readIdentifier();
                continue;
            }

            if (c == '"' || c == '\'') {
                readString();
                continue;
            }

            /*
             * Symboles
             */
            switch (c) {

                case ':' -> {
                    if (peekNext() == '=') {
                        addToken(MTTokenType.ASSIGN, ":=");
                        advance();
                        advance();
                    } else {
                        addToken(MTTokenType.COLON, ":");
                        advance();
                    }
                }

                case '.' -> {
                    addToken(MTTokenType.DOT, ".");
                    advance();
                }

                case ';' -> {
                    addToken(MTTokenType.SEMICOLON, ";");
                    advance();
                }

                case '(' -> {
                    addToken(MTTokenType.LPAREN, "(");
                    advance();
                }

                case ')' -> {
                    addToken(MTTokenType.RPAREN, ")");
                    advance();
                }

                case '[' -> {
                    addToken(MTTokenType.LBRACKET, "[");
                    advance();
                }

                case ']' -> {
                    addToken(MTTokenType.RBRACKET, "]");
                    advance();
                }

                case '|' -> {
                    addToken(MTTokenType.PIPE, "|");
                    advance();
                }

                case '^' -> {
                    addToken(MTTokenType.RETURN, "^");
                    advance();
                }

                default -> {

                    if (isBinarySelector(c)) {
                        readBinarySelector();
                    } else {
                        throw error(
                                "Unexpected character '" + c + "'");
                    }
                }
            }
        }

        if (commentDepth > 0) {
            throw error("Unterminated comment");
        }

        tokens.add(
                new MTToken(
                        MTTokenType.EOF,
                        "",
                        line,
                        column));

        return tokens;
    }

    private boolean insideComment() {
        return commentDepth > 0;
    }

    private void readCommentStart() {

        addToken(MTTokenType.LCOMMENT, "/*");

        advance();
        advance();

        commentDepth++;
    }

    private void readCommentEnd() {

        if (commentDepth == 0) {
            throw error("Unexpected comment terminator");
        }

        addToken(MTTokenType.RCOMMENT, "*/");

        advance();
        advance();

        commentDepth--;
    }

    private void readMeta() {

        int start = position;
        int startLine = line;
        int startColumn = column;

        advance(); // @

        while (!isAtEnd()) {

            char c = peek();

            if (Character.isLetterOrDigit(c)
                    || c == '_'
                    || c == '-') {

                advance();
            } else {
                break;
            }
        }

        tokens.add(
                new MTToken(
                        MTTokenType.META,
                        source.substring(start, position),
                        startLine,
                        startColumn));
    }

    private void readIdentifier() {

        int start = position;
        int startLine = line;
        int startColumn = column;

        while (!isAtEnd()) {

            char c = peek();

            if (Character.isLetterOrDigit(c)
                    || c == '_') {

                advance();
            } else {
                break;
            }
        }

        /*
        tokens.add(
                new MTToken(
                        MTTokenType.IDENTIFIER,
                        source.substring(start, position),
                        startLine,
                        startColumn));
        */
        String text = source.substring(start, position);
        MTTokenType type;

        switch (text) {
            case "nil" -> type = MTTokenType.NIL;

            case "true" -> type = MTTokenType.TRUE;

            case "false" -> type = MTTokenType.FALSE;

            case "self" -> type = MTTokenType.SELF;

            case "super" -> type = MTTokenType.SUPER;

            default -> type = MTTokenType.IDENTIFIER;
        }

        tokens.add(
            new MTToken(
                type,
                text,
                startLine,
                startColumn));
    }

    private void readInteger() {

        int start = position;
        int startLine = line;
        int startColumn = column;

        while (!isAtEnd()
                && Character.isDigit(peek())) {

            advance();
        }

        tokens.add(
                new MTToken(
                        MTTokenType.INTEGER,
                        source.substring(start, position),
                        startLine,
                        startColumn));
    }

    private void readString() {

        int startLine = line;
        int startColumn = column;

        char delimiter = advance();

        StringBuilder value = new StringBuilder();

        while (!isAtEnd()) {

            char c = advance();

            if (c == '\\') {

                if (isAtEnd()) {
                    throw error("Invalid escape sequence");
                }

                char escaped = advance();

                switch (escaped) {

                    case 'n' -> value.append('\n');
                    case 'r' -> value.append('\r');
                    case 't' -> value.append('\t');
                    case '\\' -> value.append('\\');
                    case '\'' -> value.append('\'');
                    case '"' -> value.append('"');

                    default -> value.append(escaped);
                }

                continue;
            }

            if (c == delimiter) {

                tokens.add(
                        new MTToken(
                                MTTokenType.STRING,
                                value.toString(),
                                startLine,
                                startColumn));

                return;
            }

            value.append(c);
        }

        throw error(
                "Unterminated string literal (expected "
                        + delimiter
                        + ")");
    }

    private void readBinarySelector() {

        int start = position;
        int startLine = line;
        int startColumn = column;

        while (!isAtEnd()
                && isBinarySelector(peek())) {

            advance();
        }

        tokens.add(
                new MTToken(
                        MTTokenType.BINARY_SELECTOR,
                        source.substring(start, position),
                        startLine,
                        startColumn));
    }

    private void skipWhitespace() {

        while (!isAtEnd()
                && Character.isWhitespace(peek())) {

            advance();
        }
    }

    private boolean isBinarySelector(char c) {

        return "+-*/=<>~&,%".indexOf(c) >= 0;
    }

    private char peek() {
        return source.charAt(position);
    }

    private char peekNext() {

        if (position + 1 >= source.length()) {
            return '\0';
        }

        return source.charAt(position + 1);
    }

    private char advance() {

        char c = source.charAt(position++);

        if (c == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        return c;
    }

    private boolean isAtEnd() {
        return position >= source.length();
    }

    private void addToken(
            MTTokenType type,
            String text) {

        tokens.add(
                new MTToken(
                        type,
                        text,
                        line,
                        column));
    }

    private MTRuntimeException error(String message) {

        return new MTRuntimeException(
                "[line "
                        + line
                        + ", column "
                        + column
                        + "] "
                        + message);
    }
}
