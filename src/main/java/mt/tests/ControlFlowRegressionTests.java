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

public final class ControlFlowRegressionTests {

    private ControlFlowRegressionTests() {
    }

    public static void runAll() {

        testIfTrue();

        testIfTrueFalseCondition();

        testIfFalse();

        testIfTrueIfFalse();

        testIfFalseIfTrue();

        testIfTrueMutation();

        testWhileTrue();

        testWhileFalse();

        testWhileTrueResult();

        testWhileTrueClosureMutation();

        testToDoBase();

        testToDoSum();

        testToDoSumNeg();

        testToDoLastResult();

        testToDoImportant();
    }

    private static void testIfTrue() {

        System.out.println("=== ifTrue: test ===");

        String source =
            """
            true ifTrue: [
                42
            ] """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();

        MTInterpreter interpreter = new MTInterpreter();

        assertResult(source, interpreter.evaluate(
                ast, new MTScope(null)));
    }

    private static void testIfTrueFalseCondition() {
        System.out.println(
            "=== ifTrue: false condition test ===");

        String source =
            """
            false ifTrue: [
                42
            ] """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();

        MTInterpreter interpreter = new MTInterpreter();

        assertResult( source,
            interpreter.evaluate( ast, new MTScope(null)));
    }

    private static void testIfFalse() {
        System.out.println("=== ifFalse: test ===");

        String source =
            """
            false ifFalse: [
                42
            ] """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter();

        assertResult(source, interpreter.evaluate(
                    ast,new MTScope(null)));
    }

    private static void testIfTrueIfFalse() {
        System.out.println("=== ifTrue:ifFalse: test ===");

        String source =
            """
            true
                ifTrue: [42]
                ifFalse: [99]
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter();

        assertResult(source, interpreter.evaluate(
                    ast, new MTScope(null)));
    }

    private static void testIfFalseIfTrue() {
        System.out.println("=== ifFalse:ifTrue: test ===");

        String source =
            """
            false
                ifTrue: [42]
                ifFalse: [99]
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter();

        assertResult( source,
            interpreter.evaluate(
                    ast,
                    new MTScope(null)));
    }

    private static void testIfTrueMutation() {
        System.out.println("=== ifTrue: mutation test ===");

        String source =
            """
            x := 10.

            true ifTrue: [
                x := x + 1
            ].

            x
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter();

        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());
        scope.define(MTSymbol.intern("x"),zero);

        assertResult(source,
            interpreter.evaluate(
                    ast,
                    scope));
    }

    private static void testWhileTrue() {
        System.out.println("=== whileTrue: test ===");
        String source =
            """
            x := 0.

            [x < 5]
                whileTrue: [
                    x := x + 1
                ].

            x
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTInterpreter interpreter = new MTInterpreter();

        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());
        scope.define(MTSymbol.intern("x"), zero);

        assertResult(source,
            interpreter.evaluate(
                    parser.parse(),
                    scope));
    }

    private static void testWhileFalse() {

        System.out.println("=== whileFalse: test ===");

        String source =
            """
            x := 5.

            [x = 0]
                whileFalse: [
                    x := x - 1
                ].

            x
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTInterpreter interpreter = new MTInterpreter();

        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());
        scope.define(MTSymbol.intern("x"),zero);

        assertResult(source,
            interpreter.evaluate(
                    parser.parse(),
                    scope));
    }

    private static void testWhileTrueResult() {
        System.out.println("=== whileTrue: result test ===");

        String source =
            """
            x := 0.

            [x < 3]
                whileTrue: [
                    x := x + 1.
                    x
                ]
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTInterpreter interpreter = new MTInterpreter();

        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());

        scope.define(MTSymbol.intern("x"),zero);

        assertResult(source,
            interpreter.evaluate(
                    parser.parse(),
                    scope));
    }

    private static void testWhileTrueClosureMutation() {
        System.out.println("=== whileTrue: closure mutation test ===");

        String source =
            """
            x := 1.

            [x < 16]
                whileTrue: [
                    x := x * 2
                ].

            x
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTInterpreter interpreter = new MTInterpreter();

        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());
        scope.define(MTSymbol.intern("x"), zero);

        assertResult(source,
            interpreter.evaluate(
                    parser.parse(),
                    scope));
    }

    private static void testToDoBase() {
        System.out.println("=== to:do: test 1 ===");

        String source =
            """
            1 to: 5 do: [:i |
              nil
            ]
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTInterpreter interpreter = new MTInterpreter();

        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());
        scope.define(MTSymbol.intern("x"), zero);

        assertResult(source,
            interpreter.evaluate(
                    parser.parse(),
                    scope));
    }

    private static void testToDoSum() {
        System.out.println("=== to:do: test 2 - calcul de somme ===");

        String source =
            """
            [
              | sum |
              sum := 0.
              1 to: 5 do: [:i |
                  sum := sum + i
              ].

              sum
            ] value
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTInterpreter interpreter = new MTInterpreter();

        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());
        scope.define(MTSymbol.intern("x"), zero);

        assertResult(source,
            interpreter.evaluate(
                    parser.parse(),
                    scope));
    }

    private static void testToDoSumNeg() {
        System.out.println("=== to:do: test 3 - calcul de somme, decroissant ===");

        String source =
            """
            [
               | sum |
               sum := 0.

               5 to: 1 do: [:i |
                   sum := sum + i
               ].

               sum
            ] value
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTInterpreter interpreter = new MTInterpreter();

        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());
        scope.define(MTSymbol.intern("x"), zero);

        assertResult(source,
            interpreter.evaluate(
                    parser.parse(),
                    scope));
    }

    private static void testToDoLastResult() {
        System.out.println("=== to:do: test 4 - dernier resultat ===");

        String source =
            """
             1 to: 3 do: [:i |
                i * 10
             ]
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTInterpreter interpreter = new MTInterpreter();
        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());
        scope.define(MTSymbol.intern("x"),zero);

        assertResult(source,
            interpreter.evaluate(
                    parser.parse(),
                    scope));
    }

    private static void testToDoImportant() {
        System.out.println("=== to:do: test 5 - important ===");

        String source =
            """
            [
              | x |

              x := 1.

              1 to: 4 do: [:i |
                   x := x * 2
              ].

              x
            ] value
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTInterpreter interpreter = new MTInterpreter();

        MTScope scope = new MTScope(null);

        MTInteger zero = new MTInteger(0);
        zero.setClazz(MTKernelBootstrap.createIntegerClass());

        scope.define(MTSymbol.intern("x"),zero);

        assertResult(source,
            interpreter.evaluate(
                    parser.parse(),
                    scope));
    }

}
