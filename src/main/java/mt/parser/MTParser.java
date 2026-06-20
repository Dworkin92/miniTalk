package mt.parser;

import mt.ast.MTArrayLiteral;
import mt.ast.MTAssignment;
import mt.ast.MTBlock;
import mt.ast.MTExpr;
import mt.ast.MTLiteral;
import mt.ast.MTMessageSend;
import mt.ast.MTReturnExpr;
import mt.ast.MTSequence;
import mt.ast.MTVariableRef;
import mt.lexer.MTToken;
import mt.lexer.MTTokenType;

import java.util.ArrayList;
import java.util.List;

public final class MTParser {

    private final List<MTToken> tokens;
    private int pos = 0;

    public MTParser(List<MTToken> tokens) {
        this.tokens = tokens;
    }

    public MTSequence parseProgram() {
        List<MTExpr> statements = new ArrayList<>();

        while (!check(MTTokenType.EOF)) {
            statements.add(parse());

            if (match(MTTokenType.PERIOD)) {
                continue;
            }

            if (!check(MTTokenType.EOF)) {
                throw new MTParseException(
                        "Expected '.' or EOF, found " + peek().text(),
                        peek().position()
                );
            }
        }

        return new MTSequence(statements);
    }

    public MTExpr parse() {
        return parseExpression();
    }

    /**
     * expression = return | assignment | keywordExpression
     */
    private MTExpr parseExpression() {
        if (match(MTTokenType.RETURN)) {
            MTExpr value = parseExpression();
            return new MTReturnExpr(value);
        }

        if (check(MTTokenType.IDENTIFIER) && lookAheadType(1) == MTTokenType.ASSIGN) {
            String name = advance().text(); // variable
            advance(); // :=
            MTExpr value = parseExpression();
            return new MTAssignment(name, value);
        }

        return parseKeywordExpression();
    }

    /**
     * sequence = expression ('.' expression)*
     * Utilisé dans ( ... ) et [ ... ]
     */
    private MTExpr parseSequence() {
        List<MTExpr> statements = new ArrayList<>();

        statements.add(parseExpression());

        while (match(MTTokenType.PERIOD)) {
            if (check(MTTokenType.RBRACKET) || check(MTTokenType.RPAREN)) {
                break;
            }
            statements.add(parseExpression());
        }

        if (statements.size() == 1) {
            return statements.get(0);
        }

        return new MTSequence(statements);
    }

    /**
     * keywordExpression = binaryExpression (KEYWORD binaryExpression)*
     */
    private MTExpr parseKeywordExpression() {
        MTExpr expr = parseBinaryExpression();

        if (!check(MTTokenType.KEYWORD)) {
            return expr;
        }

        List<String> selectors = new ArrayList<>();
        List<MTExpr> args = new ArrayList<>();

        while (check(MTTokenType.KEYWORD)) {
            MTToken kw = advance();
            selectors.add(kw.text());
            args.add(parseBinaryExpression());
        }

        return new MTMessageSend(expr, String.join("", selectors), args);
    }

    /**
     * binaryExpression = unaryExpression (BINARY_SELECTOR unaryExpression)*
     */
    private MTExpr parseBinaryExpression() {
        MTExpr expr = parseUnaryExpression();

        while (check(MTTokenType.BINARY_SELECTOR) &&
                !"]".equals(peek().text()) &&
                !")".equals(peek().text())) {
            String selector = advance().text();
            MTExpr right = parseUnaryExpression();
            expr = new MTMessageSend(expr, selector, List.of(right));
        }

        return expr;
    }

    /**
     * unaryExpression = primary (IDENTIFIER)*
     */
    private MTExpr parseUnaryExpression() {
        MTExpr expr = parsePrimary();

        while (check(MTTokenType.IDENTIFIER)) {
            String selector = advance().text();
            expr = new MTMessageSend(expr, selector, List.of());
        }

        return expr;
    }

    private MTExpr parsePrimary() {
        // tableau : #( ... )
        if (check(MTTokenType.BINARY_SELECTOR) && "#".equals(peek().text())) {
            advance(); // #
            consume(MTTokenType.LPAREN);

            List<MTExpr> elements = new ArrayList<>();
            while (!check(MTTokenType.RPAREN)) {
                elements.add(parseExpression());
            }

            consume(MTTokenType.RPAREN);
            return new MTArrayLiteral(elements);
        }

        // nombre négatif simple : -3
        if (check(MTTokenType.BINARY_SELECTOR) && "-".equals(peek().text())) {
            advance(); // consomme '-'

            if (match(MTTokenType.INTEGER)) {
                return new MTLiteral(-Integer.parseInt(previous().text()));
            }

            if (match(MTTokenType.FLOAT)) {
                return new MTLiteral(-Double.parseDouble(previous().text()));
            }

            throw new MTParseException(
                    "Nombre attendu après '-'",
                    peek().position()
            );
        }

        if (match(MTTokenType.INTEGER)) {
            return new MTLiteral(Integer.parseInt(previous().text()));
        }

        if (match(MTTokenType.FLOAT)) {
            return new MTLiteral(Double.parseDouble(previous().text()));
        }

        if (match(MTTokenType.STRING)) {
            return new MTLiteral(previous().text());
        }

        if (match(MTTokenType.IDENTIFIER)) {
            String name = previous().text();

            return switch (name) {
                case "true" -> new MTLiteral(Boolean.TRUE);
                case "false" -> new MTLiteral(Boolean.FALSE);
                case "nil" -> new MTLiteral(null);
                default -> new MTVariableRef(name);
            };
        }

        if (match(MTTokenType.LBRACKET)) {
            return parseBlock();
        }

        if (match(MTTokenType.LPAREN)) {
            MTExpr expr = parseSequence();
            consume(MTTokenType.RPAREN);
            return expr;
        }

        throw new MTParseException(
                "Unexpected token: " + peek().text(),
                peek().position()
        );
    }

    /**
     * block = '[' ((':' IDENTIFIER)+ '|')? sequence ']'
     */
    private MTBlock parseBlock() {
        List<String> params = new ArrayList<>();

        while (match(MTTokenType.COLON)) {
            MTToken name = consume(MTTokenType.IDENTIFIER);
            params.add(name.text());
        }

        if (!params.isEmpty()) {
            consume(MTTokenType.PIPE);
        }

        MTExpr body = parseSequence();

        consume(MTTokenType.RBRACKET);

        return new MTBlock(params, body);
    }

    private boolean match(MTTokenType type) {
        if (check(type)) {
            pos++;
            return true;
        }
        return false;
    }

    private boolean check(MTTokenType type) {
        return peek().type() == type;
    }

    private MTToken consume(MTTokenType type) {
        if (check(type)) {
            return tokens.get(pos++);
        }

        throw new MTParseException(
                "Expected " + type + " but found " + peek().type(),
                peek().position()
        );
    }

    private MTToken previous() {
        return tokens.get(pos - 1);
    }

    private MTToken peek() {
        return tokens.get(pos);
    }

    private MTToken advance() {
        return tokens.get(pos++);
    }

    private MTTokenType lookAheadType(int offset) {
        int index = pos + offset;
        if (index >= tokens.size()) {
            return MTTokenType.EOF;
        }
        return tokens.get(index).type();
    }
}
