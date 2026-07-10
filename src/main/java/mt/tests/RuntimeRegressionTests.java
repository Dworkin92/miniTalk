package mt.tests;

import mt.runtime.*;
import mt.runtime.primitives.*;
import mt.runtime.bootstrap.*;
import mt.ast.*;
import mt.interpreter.*;
import mt.lexer.*;

public final class RuntimeRegressionTests {

    private RuntimeRegressionTests() {
    }

    public static void runAll() {

        testSymbols();

        testIntegers();

        testBooleans();

        testStrings();

        testArray();

        testDictionary();

        testRuntime();

        testScope();

        testBlock();

        testNonLocalReturn();

        testInterpreter();

        testAssignment();

        testMessageSend();

        testSequence();

        testBlockNode();
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

    private static void testIntegers() {

        System.out.println(
                "=== Integer operations ===");
        MTClass integerClass =
            MTKernelBootstrap
                .createIntegerClass();

        MTInteger a = new MTInteger(10);

        a.setClazz(integerClass);

        MTInteger b = new MTInteger(3);

        b.setClazz(integerClass);

        /* + */

        MTArray plusArgs = new MTArray();

        plusArgs.add(b);

        System.out.println(
            a.send(
                MTSymbol.intern("+"),
                plusArgs));

        /* - */

        MTArray minusArgs = new MTArray();

        minusArgs.add(b);

        System.out.println(
            a.send(
                MTSymbol.intern("-"),
                minusArgs));

        /* * */

        MTArray multArgs = new MTArray();

        multArgs.add(b);

        System.out.println(
            a.send(
                MTSymbol.intern("*"),
                multArgs));

        /* / */

        MTArray divArgs = new MTArray();

        divArgs.add(b);

        System.out.println(
            a.send(
                MTSymbol.intern("/"),
                divArgs));

        /* = */

        MTArray eqArgs = new MTArray();

        eqArgs.add(b);

        System.out.println(
            a.send(
                MTSymbol.intern("="),
                eqArgs));

        /* < */

        MTArray ltArgs = new MTArray();

        ltArgs.add(b);

        System.out.println(
            a.send(
                MTSymbol.intern("<"),
                ltArgs));

        /* > */

        MTArray gtArgs = new MTArray();

        gtArgs.add(b);

        System.out.println(
            a.send(
                MTSymbol.intern(">"),
                gtArgs));

        /* ~< */

        MTArray leArgs = new MTArray();

        leArgs.add(b);

        System.out.println(
            a.send(
                MTSymbol.intern("~<"),
                leArgs));

        /* >~ */

        MTArray geArgs = new MTArray();

        geArgs.add(b);

        System.out.println(
            a.send(
                MTSymbol.intern(">~"),
                geArgs));

        MTBoolean result =
            (MTBoolean)
                a.send(
                    MTSymbol.intern("<"),
                    ltArgs);

        System.out.println(
            result.getClazz());

    }

    private static void testBooleans() {

        System.out.println(
                "=== Boolean operations ===");

        MTClass booleanClass =
            MTKernelBootstrap
                .createBooleanClass();

        MTBoolean t = MTBoolean.TRUE;

        t.setClazz(booleanClass);

        MTBoolean f = MTBoolean.FALSE;

        f.setClazz(booleanClass);

        System.out.println(
            t.send(
                MTSymbol.intern("not")));

        System.out.println(
            f.send(
                MTSymbol.intern("not")));

        MTArray andArgs = new MTArray();

        andArgs.add(f);

        System.out.println(
            t.send(
                MTSymbol.intern("and:"),
                andArgs));

        MTArray orArgs = new MTArray();

        orArgs.add(f);

        System.out.println(
            t.send(
                MTSymbol.intern("or:"),
                orArgs));

        MTArray xorArgs = new MTArray();

        xorArgs.add(t);

        System.out.println(
            t.send(
                MTSymbol.intern("xor:"),
                xorArgs));

        xorArgs = new MTArray();

        xorArgs.add(t);

        System.out.println(
            f.send(
                MTSymbol.intern("xor:"),
                xorArgs));

                System.out.println(
    MTBoolean.TRUE.getClazz());

System.out.println(
    MTBoolean.FALSE.getClazz());

    }

    private static void testStrings() {

        System.out.println(
                "=== String messages ===");

        MTClass stringClass =
            MTKernelBootstrap
                .createStringClass();

        MTString hello = new MTString("Hello ");

        hello.setClazz(stringClass);

        MTString world = new MTString("World");

        world.setClazz(stringClass);

        System.out.println(
            hello.send(
                MTSymbol.intern("size")));

        MTArray concatArgs = new MTArray();

        concatArgs.add(world);

        System.out.println(
            hello.send(
                MTSymbol.intern("+"),
                concatArgs));

        MTArray eqArgs2 = new MTArray();

        eqArgs2.add(new MTString("Hello "));

        System.out.println(
            hello.send(
                MTSymbol.intern("="),
                eqArgs2));

        MTArray eqArgs3 = new MTArray();

        eqArgs3.add(new MTString("World"));

        System.out.println(
            hello.send(
                MTSymbol.intern("="),
                eqArgs3));

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

    private static void testBlock() {

        System.out.println(
                "=== Block V4 test ===");
        MTScope global = new MTScope(null);

        MTClass integerClass =
            MTKernelBootstrap.createIntegerClass();

MTInteger zValue =
        new MTInteger(100);

zValue.setClazz(
        integerClass);

global.define(
        MTSymbol.intern("z"),
        zValue);

        /*
        global.define(
            MTSymbol.intern("z"),
            new MTInteger(100));
        */

        MTArray params = new MTArray();

        params.add(MTSymbol.intern("x"));

        params.add(MTSymbol.intern("y"));

        MTVariableNode x =
            new MTVariableNode(
                MTSymbol.intern("x"));

        MTVariableNode y =
            new MTVariableNode(
                MTSymbol.intern("y"));

        MTVariableNode z =
            new MTVariableNode(
                MTSymbol.intern("z"));

        MTArrayNode argsXY =
            new MTArrayNode();

        argsXY.add(y);

        MTMessageSendNode plusXY =
            new MTMessageSendNode(
                x,
                MTSymbol.intern("+"),
                argsXY);

        MTArrayNode argsXYZ =
            new MTArrayNode();

        argsXYZ.add(z);

        MTMessageSendNode plusXYZ =
            new MTMessageSendNode(
                plusXY,
                MTSymbol.intern("+"),
                argsXYZ);
        MTSequenceNode body =
            new MTSequenceNode();

        body.add(plusXYZ);

        MTBlockNode blockNode =
            new MTBlockNode(
                params,
                body);

        MTBlock block =
            new MTBlock(
                global,
                params,
                blockNode);

        MTArray arguments = new MTArray();

/*
MTClass integerClass =
        MTKernelBootstrap
                .createIntegerClass();
*/

MTInteger ten =
        new MTInteger(10);

ten.setClazz(
        integerClass);

MTInteger twenty =
        new MTInteger(20);

twenty.setClazz(
        integerClass);

arguments.add(
        ten);

arguments.add(
        twenty);

        MTScope activation =
            block.createActivationScope(
                arguments);

        System.out.println(
            activation.lookup(
                MTSymbol.intern("x")));

        System.out.println(
            activation.lookup(
                MTSymbol.intern("y")));

        System.out.println(
            activation.lookup(
                MTSymbol.intern("z")));

        MTInterpreter interpreter = new MTInterpreter();

        System.out.println(
            interpreter.evaluate(
                blockNode.getBody(),
                activation));

        System.out.println(
            block.value(
                arguments));
        // ton code ac*uel
    }

    private static void testNonLocalReturn() {

        System.out.println(
                "=== Non Local Return test ===");

        try {
            throw new MTNonLocalReturnException(
                new MTInteger(42));
        }
        catch(MTNonLocalReturnException ex) {
            System.out.println(ex.getValue());
        }
        // ton code actuel
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
}
