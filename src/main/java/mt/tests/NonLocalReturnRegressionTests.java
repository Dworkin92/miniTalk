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

public final class NonLocalReturnRegressionTests {

    private NonLocalReturnRegressionTests() {
    }

    public static void runAll() {

        testNonLocalReturnException();

        testNonLocalReturnBlock();

        testNonLocalReturnStopsExecution();

        testNonLocalReturnSkipsAssignment();

        testNonLocalReturnCapturedVariable();

    }

    private static void testNonLocalReturnException() {
        System.out.println("=== Non Local Return exception test ===");

        try {
            throw new MTNonLocalReturnException(
                new MTInteger(42));
        }
        catch(MTNonLocalReturnException ex) {
            assertResult("^42.", ex.getValue());
        }

    }

    private static void testNonLocalReturnBlock() {
        System.out.println("=== Non Local Return Block test ===");

        MTSequenceNode body = new MTSequenceNode();
        body.add( new MTNonLocalReturnNode(new MTIntegerLiteralNode(42)));

        MTBlockNode blockNode = new MTBlockNode(new MTArray(),body);

        MTInterpreter interpreter = new MTInterpreter();

        MTBlock block = (MTBlock)
            interpreter.evaluate(
                    blockNode,
                    new MTScope(null));

        assertResult("[ ^42 ]",
            block.value());
    }

    private static void testNonLocalReturnStopsExecution() {
        System.out.println("=== Non Local Return Stops Execution test ===");

        String source = """
            [
              ^42.
              99
            ]
            """;
        MTSequenceNode body = new MTSequenceNode();
        body.add(new MTNonLocalReturnNode(new MTIntegerLiteralNode(42)));

        body.add(new MTIntegerLiteralNode(99));

        MTBlockNode blockNode = new MTBlockNode(new MTArray(), body);

        MTInterpreter interpreter = new MTInterpreter();

        MTBlock block = (MTBlock)interpreter.evaluate(
                    blockNode, new MTScope(null));

        System.out.println(source + " ==> " + block.value());
    }

    private static void testNonLocalReturnSkipsAssignment() {
        System.out.println("=== Non Local Return Skips Assignment test ===");

        MTClass integerClass = MTKernelBootstrap.createIntegerClass();

        String source = """
            x := 0
            [
              ^42.
              x <- 999.
            ] value.
            """;
        MTScope global = new MTScope(null);
        MTInteger zero = new MTInteger(0);
        zero.setClazz(integerClass);
        global.define(MTSymbol.intern("x"),zero);

        MTSequenceNode body = new MTSequenceNode();
        body.add(new MTNonLocalReturnNode(new MTIntegerLiteralNode(42)));

        body.add(new MTAssignmentNode(MTSymbol.intern("x"),
                    new MTIntegerLiteralNode(999)));

        MTBlockNode blockNode = new MTBlockNode(new MTArray(), body);

        MTInterpreter interpreter = new MTInterpreter();

        MTBlock block = (MTBlock)interpreter.evaluate(
                    blockNode, global);

        assertResult( source,
            block.value());

        assertResult("x",
            global.lookup(
                    MTSymbol.intern("x")));
    }

    private static void testNonLocalReturnCapturedVariable() {
        System.out.println("=== Non Local Return Captured Variable test ===");

        MTClass integerClass = MTKernelBootstrap.createIntegerClass();

        MTScope global = new MTScope(null);
        MTInteger value = new MTInteger(123);
        value.setClazz(integerClass);
        global.define(MTSymbol.intern("x"), value);

        MTSequenceNode body = new MTSequenceNode();

        body.add( new MTNonLocalReturnNode(
            new MTVariableNode(MTSymbol.intern("x"))));

        MTBlockNode blockNode = new MTBlockNode(
            new MTArray(),body);

        MTInterpreter interpreter = new MTInterpreter();

        MTBlock block = (MTBlock)interpreter.evaluate(
            blockNode, global);

        assertResult(" x <- 123. [ ^x ].", block.value());
    }

}
