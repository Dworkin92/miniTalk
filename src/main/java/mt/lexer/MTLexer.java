package mt.lexer;

import java.util.ArrayList;
import java.util.List;

public final class MTLexer {

    private final String input;
    private int pos = 0;

    public MTLexer(String input) {
        this.input = input;
    }

    public List<MTToken> tokenize() {
        List<MTToken> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char c = input.charAt(pos);

            // ========================
            // WHITESPACE
            // ========================
            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }

            int start = pos;

            // ========================
            // NUMBERS (INTEGER / FLOAT)
            // ========================
            if (Character.isDigit(c)) {
                while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                    pos++;
                }

                // FLOAT uniquement si '.' suivi de chiffre
                if (pos < input.length() - 1 &&
                        input.charAt(pos) == '.' &&
                        Character.isDigit(input.charAt(pos + 1))) {

                    pos++; // consomme '.'

                    while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                        pos++;
                    }

                    tokens.add(new MTToken(
                            MTTokenType.FLOAT,
                            input.substring(start, pos),
                            start
                    ));
                } else {
                    tokens.add(new MTToken(
                            MTTokenType.INTEGER,
                            input.substring(start, pos),
                            start
                    ));
                }

                continue;
            }

            // ========================
            // IDENTIFIERS / KEYWORDS
            // ========================
            if (Character.isLetter(c)) {
                while (pos < input.length() &&
                        Character.isLetterOrDigit(input.charAt(pos))) {
                    pos++;
                }

                String text = input.substring(start, pos);

                // keyword ?
                if (pos < input.length() && input.charAt(pos) == ':') {
                    pos++;
                    tokens.add(new MTToken(MTTokenType.KEYWORD, text + ":", start));
                } else {
                    tokens.add(new MTToken(MTTokenType.IDENTIFIER, text, start));
                }

                continue;
            }

            // ========================
            // SINGLE CHAR / SYMBOLS
            // ========================
            switch (c) {

                // opérateurs
                case '+', '-', '*', '%', '=', ',' -> {
                    pos++;
                    tokens.add(new MTToken(
                            MTTokenType.BINARY_SELECTOR,
                            Character.toString(c),
                            start
                    ));
                }

                case '/' -> {
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '/') {
                        pos += 2;
                        tokens.add(new MTToken(MTTokenType.BINARY_SELECTOR, "//", start));
                    } else {
                        pos++;
                        tokens.add(new MTToken(MTTokenType.BINARY_SELECTOR, "/", start));
                    }
                }

                case '!' -> {
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        pos += 2;
                        tokens.add(new MTToken(MTTokenType.BINARY_SELECTOR, "!=", start));
                    } else {
                        throw new RuntimeException("Unknown char: !");
                    }
                }

		case '<' -> {
    		        if (pos + 1 < input.length() && input.charAt(pos + 1) == '-') {
        		        pos += 2;
        		        tokens.add(new MTToken(MTTokenType.ASSIGN, "<-", start));
    		        } else if (pos + 1 < input.length() && input.charAt(pos + 1) == '>') {
        		        pos += 2;
        		        tokens.add(new MTToken(MTTokenType.BINARY_SELECTOR, "<>", start));
    		        } else if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
        		        pos += 2;
        		        tokens.add(new MTToken(MTTokenType.BINARY_SELECTOR, "<=", start));
    		        } else {
        		        pos++;
        		        tokens.add(new MTToken(MTTokenType.BINARY_SELECTOR, "<", start));
    		        }
		}

                case '>' -> {
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        pos += 2;
                        tokens.add(new MTToken(MTTokenType.BINARY_SELECTOR, ">=", start));
                    } else {
                        pos++;
                        tokens.add(new MTToken(MTTokenType.BINARY_SELECTOR, ">", start));
                    }
                }

                // #
                case '#' -> {
                    pos++;
                    tokens.add(new MTToken(
                            MTTokenType.BINARY_SELECTOR,
                            "#",
                            start
                    ));
                }

                // return ^
                case '^' -> {
                    pos++;
                    tokens.add(new MTToken(
                            MTTokenType.RETURN,
                            "^",
                            start
                    ));
                }

                case '(' -> {
                    pos++;
                    tokens.add(new MTToken(MTTokenType.LPAREN, "(", start));
                }

                case ')' -> {
                    pos++;
                    tokens.add(new MTToken(MTTokenType.RPAREN, ")", start));
                }

                case '[' -> {
                    pos++;
                    tokens.add(new MTToken(MTTokenType.LBRACKET, "[", start));
                }

                case ']' -> {
                    pos++;
                    tokens.add(new MTToken(MTTokenType.RBRACKET, "]", start));
                }

                case '|' -> {
                    pos++;
                    tokens.add(new MTToken(MTTokenType.PIPE, "|", start));
                }

                case '.' -> {
                    pos++;
                    tokens.add(new MTToken(MTTokenType.PERIOD, ".", start));
                }

                case ';' -> {
                    pos++;
                    tokens.add(new MTToken(MTTokenType.SEMICOLON, ";", start));
                }

                // assign :=
                case ':' -> {
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        pos += 2;
                        tokens.add(new MTToken(MTTokenType.ASSIGN, ":=", start));
                    } else {
                        pos++;
                        tokens.add(new MTToken(MTTokenType.COLON, ":", start));
                    }
                }

                // ========================
                // STRING (avec échappement)
                // ========================
                case '\'' -> {
                    pos++; // skip '

                    StringBuilder sb = new StringBuilder();

                    while (pos < input.length()) {
                        char ch = input.charAt(pos);

                        if (ch == '\\') {
                            pos++;
                            if (pos >= input.length()) break;

                            char next = input.charAt(pos);

                            switch (next) {
                                case '\'' -> sb.append('\'');
                                case '\\' -> sb.append('\\');
                                case 'n' -> sb.append('\n');
                                case 't' -> sb.append('\t');
                                default -> sb.append(next);
                            }

                            pos++;
                            continue;
                        }

                        if (ch == '\'') break;

                        sb.append(ch);
                        pos++;
                    }

                    pos++; // skip closing '

                    tokens.add(new MTToken(
                            MTTokenType.STRING,
                            sb.toString(),
                            start
                    ));
                }

                // ========================
                // COMMENT "..."
                // ========================
                case '"' -> {
                    pos++; // skip opening "

                    while (pos < input.length() &&
                            input.charAt(pos) != '"') {
                        pos++;
                    }

                    if (pos < input.length()) pos++; // skip closing
                }

                default -> throw new RuntimeException("Unknown char: " + c);
            }
        }

        tokens.add(new MTToken(MTTokenType.EOF, "", pos));
        return tokens;
    }
}