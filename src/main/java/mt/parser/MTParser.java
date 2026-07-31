package mt.parser;

import java.util.List;
import java.util.ArrayList;


import mt.ast.*;
import mt.lexer.MTToken;
import mt.lexer.MTTokenType;
import mt.runtime.MTSymbol;
import mt.runtime.MTArray;

import mt.exceptions.MTRuntimeException;
import mt.exceptions.MTParseException;

public final class MTParser {

    private final List<MTToken> tokens;

    private int current;

    //private String moduleName;

    //private final List<String> imports = new ArrayList<>();

    public MTParser(List<MTToken> tokens) {

        this.tokens = tokens;

        this.current = 0;
    }

    public MTNode parse() {

        return parseTopLevelSequence();
    }

    private MTNode parseTopLevelSequence() {

        MTSequenceNode sequence = new MTSequenceNode();

        parseTemporaryDeclarations(sequence);

        if (isAtEnd()) {
            return sequence;
        }

        sequence.add(parseExpression());

        while (match(MTTokenType.DOT)) {
            if (isAtEnd()) {
                break;
            }

            sequence.add(parseExpression());
        }

        if (!sequence.hasTemporaries() && sequence.getStatements().size() == 1) {
            return sequence.getStatements().get(0);
        }

        return sequence;
    }

    private void parseTemporaryDeclarations(MTSequenceNode sequence) {

        if (!match(MTTokenType.PIPE)) {
            return;
        }

        while (!check(MTTokenType.PIPE)) {
            MTToken name = consume(MTTokenType.IDENTIFIER, "Expected temporary name");

            //System.out.println("temporary parsed: " + name.text());

            sequence.addTemporary(MTSymbol.intern(name.text()));
        }

        consume(MTTokenType.PIPE, "Expected '|'");
    }

    /*
     * sequence
     *
     * expr .
     * expr .
     * expr
     */
    private MTNode parseSequence() {

        MTSequenceNode sequence =
                new MTSequenceNode();

        //sequence.add(parseExpression());
        while (!isAtEnd()&& !check(MTTokenType.RBRACKET) && !check(MTTokenType.RPAREN)) {
            sequence.add(parseExpression());
            match(MTTokenType.DOT);
        }

        while (match(MTTokenType.DOT)) {

            if (isAtEnd()
                || check(MTTokenType.RBRACKET)
                || check(MTTokenType.RPAREN)) {
                break;
            }
            sequence.add(parseExpression());
        }

        if (sequence.getStatements()
                .size() == 1) {

            return sequence
                    .getStatements()
                    .get(0);
        }

        return sequence;
    }

    private MTNode parseExpression() {

        return parseAssignment();
    }

    /*
     * x := expr
     */
    private MTNode parseAssignment() {

        if (check(
                MTTokenType.IDENTIFIER)

                && lookAhead(
                        MTTokenType.ASSIGN)) {

            MTToken variable =
                    advance();

            consume(
                    MTTokenType.ASSIGN,
                    "Expected :=");

            MTNode value =
                    parseExpression();

            return new MTAssignmentNode(
                    MTSymbol.intern(
                            variable.text()),
                    value);
        }

        //return parseBinaryMessage();
        return parseKeywordMessage();
    }

    private MTNode parsePrimary() {

        if (match(MTTokenType.META_DIRECTIVE)) {
            return new MTMetaDirectiveNode(previous().text());
        }

        if (match(
                MTTokenType.INTEGER)) {

            return new MTIntegerLiteralNode(
                    Long.parseLong(previous().text()));
        }

        if (match(MTTokenType.NIL)) {
            return new MTNilLiteralNode();
        }

        if (match(MTTokenType.TRUE)) {
            return new MTBooleanLiteralNode(true);
        }

        if (match(MTTokenType.FALSE)) {
            return new MTBooleanLiteralNode(false);
        }

        if (match(MTTokenType.SYMBOL)) {
            return new MTSymbolLiteralNode(
                MTSymbol.intern(previous().text()));
        }

        if (match(
                MTTokenType.IDENTIFIER)) {

            return new MTVariableNode(
                    MTSymbol.intern(
                            previous()
                                    .text()));
        }

        if (match(MTTokenType.RETURN)) {
            MTNode expression = parseExpression();

            return new MTNonLocalReturnNode(expression);
        }

        if (match(MTTokenType.LBRACKET)) {
            return parseBlock();
        }

        if (match(MTTokenType.LPAREN)) {
            MTNode expression = parseExpression();

            consume(MTTokenType.RPAREN,"Expected ')'");

            return expression;
        }

        if (match(MTTokenType.STRING)) {
            return new MTStringLiteralNode(previous().text());
        }

        /*
        throw new IllegalStateException(
                "Expected expression at token "
                        + peek().text());
         */
         throw new MTParseException(
                "Expected expression at token "
                        + peek().text(),
                peek().line(),
                peek().column());
    }

    private MTNode parseUnaryMessage() {
        MTNode receiver = parsePrimary();

        while (check(MTTokenType.IDENTIFIER) &&
                !lookAhead(MTTokenType.COLON)) {

            MTToken selector = advance();

            receiver = new MTMessageSendNode(
                receiver,
                MTSymbol.intern(selector.text()),
                new MTArrayNode());
        }

        return receiver;
    }

    private MTNode parseBinaryMessage() {
        MTNode left = parseUnaryMessage();

        while (match(
            MTTokenType.BINARY_SELECTOR)) {

            MTToken selector = previous();

            MTNode right = parseUnaryMessage();

            MTArrayNode arguments = new MTArrayNode();

            arguments.add(right);

            left = new MTMessageSendNode(
                       left,
                       MTSymbol.intern(
                               selector.text()),
                       arguments);
        }

        return left;
    }

    private MTNode parseKeywordMessage() {
        MTNode receiver = parseBinaryMessage();

        if (!isKeywordStart()) {
            return receiver;
        }

        StringBuilder selector = new StringBuilder();

        MTArrayNode arguments = new MTArrayNode();

        while (isKeywordStart()) {

            MTToken keyword = advance();

            selector.append(keyword.text());

            consume(
                MTTokenType.COLON,
                "Expected ':'");

            selector.append(":");

            arguments.add(
                parseBinaryMessage());
        }

        return new MTMessageSendNode(
            receiver,
            MTSymbol.intern(
                    selector.toString()),
            arguments);
    }

    private MTBlockNode parseBlock() {
        MTArray parameters = new MTArray();

        MTArray temporaries = new MTArray();

        /*
         * Parametres eventuels
         *
         * [:x :y |
         */

        if (check(MTTokenType.COLON)) {
            while (match(MTTokenType.COLON)) {
                MTToken parameter =
                    consume(
                            MTTokenType.IDENTIFIER,
                            "Expected parameter name");

                parameters.add(
                    MTSymbol.intern(
                            parameter.text()));
            }

            consume(
                MTTokenType.PIPE,
                "Expected '|'");
        }

        if (match(MTTokenType.PIPE)) {
            while (!check(MTTokenType.PIPE)) {
                MTToken name =consume(
                        MTTokenType.IDENTIFIER,
                        "Expected temporary name");

                temporaries.add(MTSymbol.intern(name.text()));
            }

            consume(MTTokenType.PIPE,"Expected '|'");
        }

        MTNode node = parseSequence();
        MTSequenceNode body;

        if (node instanceof MTSequenceNode seq) {
            body = seq;
        } else {
            body = new MTSequenceNode();
            body.add(node);
        }

        consume(
            MTTokenType.RBRACKET,
            "Expected ']'");

        return new MTBlockNode(
            parameters,
            temporaries,
            body);
    }

    /*
    public String getModuleName() {

        return moduleName;
    }

    public List<String> getImports() {

        return imports;
    }
    */

    /*
     * Helpers
     */

    private boolean match(
            MTTokenType type) {

        if (check(type)) {

            advance();

            return true;
        }

        return false;
    }

    private MTToken consume(
            MTTokenType type,
            String message) {

        if (check(type)) {

            return advance();
        }

        throw new MTParseException(
            message,
            peek().line(),
            peek().column());

        /*
        throw new IllegalStateException(
            message + " at line "
            + peek().line()
            + ", column "
            + peek().column());
        */
    }

    private boolean check(
            MTTokenType type) {

        if (isAtEnd()) {

            return false;
        }

        return peek().type()
                == type;
    }

    private boolean lookAhead(
            MTTokenType type) {

        if (current + 1
                >= tokens.size()) {

            return false;
        }

        return tokens.get(
                current + 1)
                .type()
                == type;
    }

    private MTToken advance() {

        if (!isAtEnd()) {

            current++;
        }

        return previous();
    }

    private boolean isAtEnd() {

        return peek().type()
                == MTTokenType.EOF;
    }

    private boolean isKeywordStart() {
        return check(
            MTTokenType.IDENTIFIER)
            &&
            lookAhead(MTTokenType.COLON);
    }

    private MTToken peek() {

        return tokens.get(
                current);
    }

    private MTToken previous() {

        return tokens.get(
                current - 1);
    }
}
