package mt.tests;

import mt.runtime.*;
import mt.runtime.primitives.*;
import mt.runtime.bootstrap.*;
import mt.ast.*;
import mt.interpreter.*;
import mt.lexer.*;
import mt.parser.*;
import mt.debug.*;
import mt.tests.*;

import static mt.tests.TestUtils.assertResult;

public final class ParserRegressionTests {

    private static final MTRuntime runtime = MTRuntimeBootstrap.bootstrap();

    private ParserRegressionTests() {
    }

    public static void runAll() {

        testParserInteger();

        testParserVariable();

        testParserAssignment();

        testParserSequence();

        testParserUnaryMessage();

        testParserBinaryMessage();

        testUnaryBeforeBinary();

        testParserParentheses();

        testParserReturn();

        testParserString();

        testParserValueKeyword();

        testParserValueValueKeyword();
    }

    private static void testParserInteger() {

        System.out.println(
            "=== Parser Integer test ===");

        String source = "42";

        MTLexer lexer = new MTLexer(source);

        MTDebug.log(lexer.tokenize().toString());

        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();

        MTInterpreter interpreter = new MTInterpreter(runtime);

        assertResult("42",interpreter.evaluate(ast,null));
    }

    private static void testParserVariable() {
        System.out.println("=== Parser Variable test ===");

        String source = "x";

        MTLexer lexer = new MTLexer(source);

        MTDebug.log(lexer.tokenize().toString());

        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();

        MTScope scope = new MTScope(runtime,null);

        scope.define(MTSymbol.intern("x"),
                     new MTInteger(123));

        MTInterpreter interpreter = new MTInterpreter(runtime);

        assertResult("x <- 123 (building AST)", interpreter.evaluate(ast, scope));
    }

    private static void testParserAssignment() {

        System.out.println("=== Parser Assignment test ===");

        String source = "x := 42";

        MTLexer lexer = new MTLexer(source);

        MTDebug.log(lexer.tokenize().toString());

        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();

        MTScope scope = new MTScope(runtime, null);

        scope.define(MTSymbol.intern("x"), new MTInteger(0));

        MTInterpreter interpreter = new MTInterpreter(runtime);

        assertResult("x := 42", interpreter.evaluate(
                    ast,
                    scope));

        assertResult("x from block scope",
            scope.lookup(MTSymbol.intern("x")));
    }

    private static void testParserSequence() {
        System.out.println("=== Parser Sequence test ===");

        String source =
            """
            x := 10.
            x
            """;

        MTLexer lexer = new MTLexer(source);

        MTDebug.log(lexer.tokenize().toString());

        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();

        MTScope scope = new MTScope(runtime, null);

        scope.define(
            MTSymbol.intern("x"),
            new MTInteger(0));

        MTInterpreter interpreter = new MTInterpreter(runtime);

        assertResult("x <- 10. x", interpreter.evaluate(
                    ast, scope));
    }

    private static void testParserUnaryMessage() {
        System.out.println("=== Parser Unary Message test ===");

        String source ="[42] value";

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTMessageSendNode send = (MTMessageSendNode) ast;
        System.out.println(send.getSelector());

        MTInterpreter interpreter = new MTInterpreter(runtime);
        assertResult(source, interpreter.evaluate(
            ast, new MTScope(runtime, null)));
    }

    private static void testParserBinaryMessage() {
        System.out.println("=== Parser Binary Message test ===");

        String source = "3 + 4";

        MTLexer lexer = new MTLexer(source);

        MTDebug.log(lexer.tokenize().toString());

        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();

        MTInterpreter interpreter = new MTInterpreter(runtime);

        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        assertResult(source, interpreter.evaluate(
                    ast, null));
    }

    private static void testUnaryBeforeBinary() {

        System.out.println("=== Unary Before Binary test ===");

        String source = "[1] value + 2";

        MTLexer lexer = new MTLexer(source);

        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();

        MTInterpreter interpreter = new MTInterpreter(runtime);

        assertResult(source, interpreter.evaluate(
                    ast, new MTScope(runtime, null)));
    }

    private static void testParserParentheses() {
        System.out.println("=== Parser Parentheses test ===");

        String source = "(3 + 4)";
        MTLexer lexer = new MTLexer(source);
        MTDebug.log(lexer.tokenize().toString());
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter(runtime);
        assertResult(source, interpreter.evaluate(ast, null));

        source = "(3 + 4) * 5";
        lexer = new MTLexer(source);
        MTDebug.log(lexer.tokenize().toString());
        parser = new MTParser(lexer.tokenize());
        ast = parser.parse();
        assertResult(source, interpreter.evaluate(ast,null));

        source = "3 - (4 + 5)";
        lexer = new MTLexer(source);
        MTDebug.log(lexer.tokenize().toString());
        parser = new MTParser(lexer.tokenize());
        ast = parser.parse();
        assertResult(source, interpreter.evaluate(ast,null));
    }

    private static void testParserReturn() {

        System.out.println("=== Parser Return test ===");

        String source = "^42";

        MTLexer lexer = new MTLexer(source);
        MTDebug.log(lexer.tokenize().toString());
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter(runtime);

        try {
            MTScope scope = new MTScope(runtime, null);
            interpreter.evaluate(ast,scope);
            System.out.println("FAILED");
        }
        catch (MTNonLocalReturnException ex) {
            assertResult(source, ex.getValue());
        }
    }
    private static void testParserString() {
        System.out.println("=== Parser String test ===");

        String source = "'Hello World'";

        MTLexer lexer = new MTLexer(source);
        MTDebug.log(lexer.tokenize().toString());

        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter(runtime);
        assertResult(source, interpreter.evaluate(
                    ast,null));

        MTObject value = interpreter.evaluate(
                ast,null);

        assertResult(source + ".getClazz()",value.getClazz());
    }

    private static void testParserValueKeyword() {
        System.out.println("=== Parser value: test ===");

        String source = "block value: 10";

        MTLexer lexer = new MTLexer(source);

        MTDebug.log(lexer.tokenize().toString());
        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();
        MTMessageSendNode send = (MTMessageSendNode) ast;

        System.out.println(send.getSelector());
        System.out.println("'" + source + "' nb arguments ==> " + send.getArguments().size());
    }

    private static void testParserValueValueKeyword() {
        System.out.println("=== Parser value:value: test ===");

        String source = "block value: 10 value: 23";

        MTLexer lexer = new MTLexer(source);
        MTDebug.log(lexer.tokenize().toString());

        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();
        MTMessageSendNode send = (MTMessageSendNode) ast;

        System.out.println(send.getSelector());
        System.out.println("'" + source + "' nb arguments ==> " + send.getArguments().size());
    }

}
