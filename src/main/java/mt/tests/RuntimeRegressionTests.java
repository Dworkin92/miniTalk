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

public final class RuntimeRegressionTests {

    private RuntimeRegressionTests() {
    }

    public static void runAll() {

        ParserRegressionTests.runAll();

        BlockRegressionTests.runAll();

        PrimitiveRegressionTests.runAll();

        testSymbols();

        testArray();

        testDictionary();

        testRuntime();

        testScope();



        testNonLocalReturnException();

        testNonLocalReturnBlock();

        testNonLocalReturnStopsExecution();

        testNonLocalReturnSkipsAssignment();

        testNonLocalReturnCapturedVariable();

        testInterpreter();

        testAssignment();

        testMessageSend();

        testSequence();

        testBlockNode();





        testParserBlock();

        testLexer();

        testParserBooleanAndNil();

        testClosureRead();

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

        testDites42();
    }

    private static void testSymbols() {

        System.out.println(
                "=== Symbol test ===");

        MTSymbol age =
                MTSymbol.intern("age");

        System.out.println(age.getValue());
        System.out.println(age);


        MTSymbol age2 =
                MTSymbol.intern("age");

        System.out.println(age == age2);

    }






    private static void testArray(){
System.out.println(
        "=== Array test ===");

MTClass arrayClass =
        MTKernelBootstrap
                .createArrayClass();

MTArray array =
        new MTArray();

array.setClazz(
        arrayClass);

MTClass integerClass =
        MTKernelBootstrap
                .createIntegerClass();

MTInteger one =
        new MTInteger(1);

one.setClazz(
        integerClass);

MTInteger two =
        new MTInteger(2);

two.setClazz(
        integerClass);

array.add(one);
array.add(two);

System.out.println(
        array.send(
                MTSymbol.intern("size"),
                new MTArray()));

MTArray atArgs =
        new MTArray();

MTInteger zero =
        new MTInteger(0);

zero.setClazz(
        integerClass);

atArgs.add(zero);

System.out.println(
        array.send(
                MTSymbol.intern("at:"),
                atArgs));
    }

    private static void testDictionary() {

        System.out.println(
                "=== Dictionary ===");

        MTClass dictionaryClass =
            MTKernelBootstrap
                .createDictionaryClass();

        MTDictionary dict = new MTDictionary();

        dict.setClazz(dictionaryClass);

        MTArray argsPut = new MTArray();

        argsPut.add(MTSymbol.intern("age"));

        argsPut.add(new MTInteger(38));

        dict.send(MTSymbol.intern("at:put:"), argsPut);

        MTArray argsAt = new MTArray();

        argsAt.add(MTSymbol.intern("age"));

        System.out.println(
            dict.send(
                MTSymbol.intern("at:"),
                argsAt));

        MTArray sizeArgs = new MTArray();

        System.out.println(
            dict.send(
                MTSymbol.intern("size"),
                sizeArgs));

        MTArray includesArgs = new MTArray();

        includesArgs.add(MTSymbol.intern("age"));

        System.out.println(
            dict.send(
                MTSymbol.intern("includesKey:"),
                includesArgs));
        // ton code actuel
    }

    private static void testRuntime() {

        System.out.println(
                "=== Bootstrap ===");

        MTRuntime runtime =
            MTRuntimeBootstrap.bootstrap();

        System.out.println(runtime.classCount());

        System.out.println(
            runtime.classNamed("Integer")
               .getName());

        System.out.println(
            runtime.classNamed("Boolean")
               .getName());

        System.out.println(
            runtime.classNamed("String")
               .getName());

        System.out.println(
            runtime.classNamed("Dictionary")
               .getName());

        System.out.println(
            runtime.getObjectClass().getName());

        System.out.println(
            runtime.getClassClass().getName());

        System.out.println(
            runtime.getObjectMetaclass().getName());

        System.out.println(
            runtime.getClassMetaclass().getName());

        System.out.println(
            runtime.getClassClass()
                .getSuperclass()
                .getName());

        System.out.println(
            runtime.getObjectClass()
                .getClazz()
                .getName());

        System.out.println(
            runtime.getClassClass()
                .getClazz()
                .getName());

        System.out.println(
            runtime.getObjectClass()
                .getSuperclass());

        System.out.println(
            runtime.getClassClass()
                .getSuperclass()
                .getName());

        System.out.println(
            runtime.getObjectMetaclass()
                .getSuperclass()
                .getName());

        System.out.println(
            runtime.getClassMetaclass()
                .getSuperclass()
                .getName());

        // ton code actuel
    }

    private static void testScope() {

        System.out.println(
                "=== Scope test ===");


        MTScope global =
            new MTScope(null);

        global.define(
            MTSymbol.intern("x"),
            new MTInteger(10));

        System.out.println(
            global.lookup(
                MTSymbol.intern("x")));

        MTScope child =
            new MTScope(
                global);

        System.out.println(
            child.lookup(
                MTSymbol.intern("x")));

        child.assign(
            MTSymbol.intern("x"),
            new MTInteger(20));

        System.out.println(
            global.lookup(
                MTSymbol.intern("x")));

        child.define(
            MTSymbol.intern("y"),
            new MTInteger(99));

        System.out.println(
            child.lookup(
                MTSymbol.intern("y")));

        System.out.println(
            child.hasLocal(
                MTSymbol.intern("y")));

        System.out.println(
            global.hasLocal(
                MTSymbol.intern("y")));
        // ton code actuel*
    }



    private static void testNonLocalReturnException() {

        System.out.println(
                "=== Non Local Return exception test ===");

        try {
            throw new MTNonLocalReturnException(
                new MTInteger(42));
        }
        catch(MTNonLocalReturnException ex) {
            System.out.println(ex.getValue());
        }

    }

    private static void testNonLocalReturnBlock() {

    System.out.println(
            "=== Non Local Return Block test ===");

    MTSequenceNode body =
            new MTSequenceNode();

    body.add(
            new MTNonLocalReturnNode(
                    new MTIntegerLiteralNode(
                            42)));

    MTBlockNode blockNode =
            new MTBlockNode(
                    new MTArray(),
                    body);

    MTInterpreter interpreter =
            new MTInterpreter();

    MTBlock block =
            (MTBlock)
            interpreter.evaluate(
                    blockNode,
                    new MTScope(null));

    System.out.println(
            block.value());
}

private static void testNonLocalReturnStopsExecution() {

    System.out.println(
            "=== Non Local Return Stops Execution test ===");

    MTSequenceNode body =
            new MTSequenceNode();

    body.add(
            new MTNonLocalReturnNode(
                    new MTIntegerLiteralNode(
                            42)));

    body.add(
            new MTIntegerLiteralNode(
                    99));

    MTBlockNode blockNode =
            new MTBlockNode(
                    new MTArray(),
                    body);

    MTInterpreter interpreter =
            new MTInterpreter();

    MTBlock block =
            (MTBlock)
            interpreter.evaluate(
                    blockNode,
                    new MTScope(null));

    System.out.println(
            block.value());
}

private static void testNonLocalReturnSkipsAssignment() {

    System.out.println(
            "=== Non Local Return Skips Assignment test ===");

    MTClass integerClass =
            MTKernelBootstrap
                    .createIntegerClass();

    MTScope global =
            new MTScope(
                    null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            integerClass);

    global.define(
            MTSymbol.intern("x"),
            zero);

    MTSequenceNode body =
            new MTSequenceNode();

    body.add(
            new MTNonLocalReturnNode(
                    new MTIntegerLiteralNode(
                            42)));

    body.add(
            new MTAssignmentNode(
                    MTSymbol.intern("x"),
                    new MTIntegerLiteralNode(
                            999)));

    MTBlockNode blockNode =
            new MTBlockNode(
                    new MTArray(),
                    body);

    MTInterpreter interpreter =
            new MTInterpreter();

    MTBlock block =
            (MTBlock)
            interpreter.evaluate(
                    blockNode,
                    global);

    System.out.println(
            block.value());

    System.out.println(
            global.lookup(
                    MTSymbol.intern("x")));
}

private static void testNonLocalReturnCapturedVariable() {

    System.out.println(
            "=== Non Local Return Captured Variable test ===");

    MTClass integerClass =
            MTKernelBootstrap
                    .createIntegerClass();

    MTScope global =
            new MTScope(null);

    MTInteger value =
            new MTInteger(123);

    value.setClazz(
            integerClass);

    global.define(
            MTSymbol.intern("x"),
            value);

    MTSequenceNode body =
            new MTSequenceNode();

    body.add(
            new MTNonLocalReturnNode(
                    new MTVariableNode(
                            MTSymbol.intern("x"))));

    MTBlockNode blockNode =
            new MTBlockNode(
                    new MTArray(),
                    body);

    MTInterpreter interpreter =
            new MTInterpreter();

    MTBlock block =
            (MTBlock)
            interpreter.evaluate(
                    blockNode,
                    global);

    System.out.println(
            block.value());
}

    private static void testInterpreter() {

        System.out.println(
            "=== Interpreter test ===");

        MTInterpreter interpreter =
            new MTInterpreter();

        MTIntegerLiteralNode fortyTwo =
            new MTIntegerLiteralNode(42);

        System.out.println(
            interpreter.evaluate(
                fortyTwo,
                null));

        MTSymbolLiteralNode ageNode =
            new MTSymbolLiteralNode(
                MTSymbol.intern("age"));

        System.out.println(
            interpreter.evaluate(
                ageNode,
                null));

        MTScope scope = new MTScope(null);

        scope.define(
            MTSymbol.intern("x"),
            new MTInteger(123));

        MTVariableNode node =
            new MTVariableNode(
                MTSymbol.intern("x"));

        System.out.println(
            interpreter.evaluate(
                node,
                scope));

        // ton code act*el
    }

    private static void testAssignment() {

        System.out.println(
                "=== Assignment test ===");
        MTInterpreter interpreter =
            new MTInterpreter();

        MTScope scope = new MTScope(null);

        scope.define(
            MTSymbol.intern("x"),
            new MTInteger(0));

        MTAssignmentNode assignNode =
            new MTAssignmentNode(
                MTSymbol.intern("x"),
                new MTIntegerLiteralNode(42));

        System.out.println(
            interpreter.evaluate(
                assignNode,
                scope));

        System.out.println(
            scope.lookup(
                MTSymbol.intern("x")));

        MTScope global = new MTScope(null);

        global.define(
            MTSymbol.intern("x"),
            new MTInteger(10));

        MTScope child = new MTScope(global);

        assignNode =
            new MTAssignmentNode(
                MTSymbol.intern("x"),
                new MTIntegerLiteralNode(99));

        interpreter.evaluate(
            assignNode,
            child);

        System.out.println(
            global.lookup(
                MTSymbol.intern("x")));
        // t*n code actuel
    }

    private static void testMessageSend() {

        System.out.println(
                "=== Message Send test ===");

        MTInterpreter interpreter =
            new MTInterpreter();

        MTClass integerClass =
            MTKernelBootstrap
                .createIntegerClass();

        MTIntegerLiteralNode threeL =
            new MTIntegerLiteralNode(3);

        MTIntegerLiteralNode fourL =
            new MTIntegerLiteralNode(4);

        MTInteger three = new MTInteger(3);

        three.setClazz(integerClass);

        MTInteger four = new MTInteger(4);

        four.setClazz(integerClass);

        MTArrayNode args = new MTArrayNode();

        args.add(
            new MTObjectLiteralNode(
                four));

        MTMessageSendNode plusNode =
            new MTMessageSendNode(
                new MTObjectLiteralNode(
                        three),
                MTSymbol.intern("+"),
                args);

        System.out.println(
            interpreter.evaluate(
                plusNode,
                null));
        // ton code actuel
    }

    private static void testSequence() {

        System.out.println(
                "=== Sequence test ===");

        MTInterpreter interpreter =
            new MTInterpreter();

        MTScope scope = new MTScope(null);

        scope.define(
            MTSymbol.intern("x"),
            new MTInteger(0));

        MTSequenceNode sequence = new MTSequenceNode();

        sequence.add(
            new MTAssignmentNode(
                MTSymbol.intern("x"),
                new MTIntegerLiteralNode(10)));

        sequence.add(
            new MTVariableNode(
                MTSymbol.intern("x")));

        System.out.println(
            interpreter.evaluate(
                sequence,
                scope));

        System.out.println(
            scope.lookup(
                MTSymbol.intern("x")));

        sequence = new MTSequenceNode();

        sequence.add(
            new MTAssignmentNode(
                MTSymbol.intern("x"),
                new MTIntegerLiteralNode(10)));

        sequence.add(
            new MTAssignmentNode(
                MTSymbol.intern("x"),
                new MTIntegerLiteralNode(20)));

        sequence.add(
            new MTVariableNode(
            MTSymbol.intern("x")));

        System.out.println(
            interpreter.evaluate(
                sequence,
                scope));

        System.out.println(
            scope.lookup(
                MTSymbol.intern("x")));
        // ton code actuel
    }

private static void testBlockNode() {

    System.out.println(
            "=== BlockNode test ===");

        MTInterpreter interpreter =
            new MTInterpreter();

        MTScope scope =
            new MTScope(null);

        scope.define(
            MTSymbol.intern("z"),
            new MTInteger(100));

        MTArray params =
            new MTArray();

        params.add(
            MTSymbol.intern("x"));

        MTSequenceNode body =
            new MTSequenceNode();

        body.add(
            new MTVariableNode(
                    MTSymbol.intern("z")));

        MTBlockNode blockNode =
            new MTBlockNode(
                    params,
                    body);

        MTBlock block =
            (MTBlock)
            interpreter.evaluate(
                    blockNode,
                    scope);

        System.out.println(
            block.parameterCount());

        System.out.println(
            block.getCapturedScope()
                 .lookup(
                     MTSymbol.intern("z")));

        try {

    MTArray badArgs =
            new MTArray();

    badArgs.add(
            new MTInteger(10));

    block.createActivationScope(
            badArgs);

}
catch (RuntimeException ex) {

    System.out.println(
            "Argument count error OK");
}
    }








private static void testParserBlock() {

    System.out.println(
            "=== Parser Block test ===");

    String source =
            "[ 42 ]";

    MTLexer lexer =
            new MTLexer(
                    source);

    System.out.println(
            lexer.tokenize());

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTNode ast =
            parser.parse();

    MTInterpreter interpreter =
            new MTInterpreter();

    MTBlock block =
            (MTBlock)
            interpreter.evaluate(
                    ast,
                    new MTScope(null));

    System.out.println(
            block.value());
}



    private static void testLexer() {
        System.out.println("=== Lexer test ===");

        String source = "nil true false self super foo";

        MTLexer lexer = new MTLexer(source);

        System.out.println(lexer.tokenize());
    }

    private static void testParserBooleanAndNil() {

    System.out.println(
            "=== Parser Boolean/Nil test ===");

    MTInterpreter interpreter =
            new MTInterpreter();

    String source = "true";

    MTNode ast =
            new MTParser(
                    new MTLexer(source)
                            .tokenize())
                    .parse();

    System.out.println(
            interpreter.evaluate(
                    ast,
                    null));

    source = "false";

    ast =
            new MTParser(
                    new MTLexer(source)
                            .tokenize())
                    .parse();

    System.out.println(
            interpreter.evaluate(
                    ast,
                    null));

    source = "nil";

    ast =
            new MTParser(
                    new MTLexer(source)
                            .tokenize())
                    .parse();

    System.out.println(
            interpreter.evaluate(
                    ast,
                    null));
}



private static void testClosureRead() {

    System.out.println(
            "=== Closure Read test ===");

    MTScope global =
            new MTScope(null);

    MTClass integerClass =
            MTKernelBootstrap
                    .createIntegerClass();

    MTInteger factor =
            new MTInteger(3);

    factor.setClazz(
            integerClass);

    global.define(
            MTSymbol.intern("factor"),
            factor);

    String source =
            """
            [:x |

                x * factor

            ]
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

    MTBlock block =
            (MTBlock)
            interpreter.evaluate(
                    parser.parse(),
                    global);

    MTArray args =
            new MTArray();

    MTInteger ten =
            new MTInteger(10);

    ten.setClazz(
            integerClass);

    args.add(ten);

    System.out.println(
            block.value(args));
}


private static void testIfTrue() {

    System.out.println(
            "=== ifTrue: test ===");

    String source =
            """
            true ifTrue: [
                42
            ]
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTNode ast =
            parser.parse();

    MTInterpreter interpreter =
            new MTInterpreter();

    System.out.println(
            interpreter.evaluate(
                    ast,
                    new MTScope(null)));
}

private static void testIfTrueFalseCondition() {

    System.out.println(
            "=== ifTrue: false condition test ===");

    String source =
            """
            false ifTrue: [
                42
            ]
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTNode ast =
            parser.parse();

    MTInterpreter interpreter =
            new MTInterpreter();

    System.out.println(
            interpreter.evaluate(
                    ast,
                    new MTScope(null)));
}

private static void testIfFalse() {

    System.out.println(
            "=== ifFalse: test ===");

    String source =
            """
            false ifFalse: [
                42
            ]
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTNode ast =
            parser.parse();

    MTInterpreter interpreter =
            new MTInterpreter();

    System.out.println(
            interpreter.evaluate(
                    ast,
                    new MTScope(null)));
}

private static void testIfTrueIfFalse() {

    System.out.println(
            "=== ifTrue:ifFalse: test ===");

    String source =
            """
            true
                ifTrue: [42]
                ifFalse: [99]
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTNode ast =
            parser.parse();

    MTInterpreter interpreter =
            new MTInterpreter();

    System.out.println(
            interpreter.evaluate(
                    ast,
                    new MTScope(null)));
}

private static void testIfFalseIfTrue() {

    System.out.println(
            "=== ifFalse:ifTrue: test ===");

    String source =
            """
            false
                ifTrue: [42]
                ifFalse: [99]
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTNode ast =
            parser.parse();

    MTInterpreter interpreter =
            new MTInterpreter();

    System.out.println(
            interpreter.evaluate(
                    ast,
                    new MTScope(null)));
}

private static void testIfTrueMutation() {

    System.out.println(
            "=== ifTrue: mutation test ===");

    String source =
            """
            x := 10.

            true ifTrue: [
                x := x + 1
            ].

            x
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTNode ast =
            parser.parse();

    MTInterpreter interpreter =
            new MTInterpreter();

    MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    ast,
                    scope));
}

private static void testWhileTrue() {

    System.out.println(
            "=== whileTrue: test ===");

    String source =
            """
            x := 0.

            [x < 5]
                whileTrue: [
                    x := x + 1
                ].

            x
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

        MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

private static void testWhileFalse() {

    System.out.println(
            "=== whileFalse: test ===");

    String source =
            """
            x := 5.

            [x = 0]
                whileFalse: [
                    x := x - 1
                ].

            x
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

        MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

private static void testWhileTrueResult() {

    System.out.println(
            "=== whileTrue: result test ===");

    String source =
            """
            x := 0.

            [x < 3]
                whileTrue: [
                    x := x + 1.
                    x
                ]
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

        MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

private static void testWhileTrueClosureMutation() {

    System.out.println(
            "=== whileTrue: closure mutation test ===");

    String source =
            """
            x := 1.

            [x < 16]
                whileTrue: [
                    x := x * 2
                ].

            x
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

                MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

private static void testToDoBase() {

    System.out.println(
            "=== to:do: test 1 ===");

    String source =
            """
            1 to: 5 do: [:i |
              nil
            ]
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

                MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

private static void testToDoSum() {

    System.out.println(
            "=== to:do: test 2 - calcul de somme ===");

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

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

                MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

private static void testToDoSumNeg() {

    System.out.println(
            "=== to:do: test 3 - calcul de somme, decroissant ===");

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

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

                MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

private static void testToDoLastResult() {

    System.out.println(
            "=== to:do: test 4 - dernier resultat ===");

    String source =
            """
1 to: 3 do: [:i |
    i * 10
]
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

                MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

private static void testToDoImportant() {

    System.out.println(
            "=== to:do: test 5 - important ===");

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

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

                MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

private static void testDites42() {

    System.out.println(
            "=== value 42 : test ===");

    String source =
            """
            [ 42 ] value
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter();

                MTScope scope =
            new MTScope(null);

    MTInteger zero =
            new MTInteger(0);

    zero.setClazz(
            MTKernelBootstrap
                    .createIntegerClass());

    scope.define(
            MTSymbol.intern("x"),
            zero);

    System.out.println(
            interpreter.evaluate(
                    parser.parse(),
                    scope));
}

}
