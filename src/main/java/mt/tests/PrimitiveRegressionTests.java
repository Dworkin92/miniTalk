package mt.tests;

import mt.runtime.*;
import mt.runtime.primitives.*;
import mt.runtime.bootstrap.*;
import mt.ast.*;
import mt.interpreter.*;
import mt.lexer.*;
import mt.parser.*;
import mt.debug.*;

import static mt.tests.TestUtils.assertResult;

public final class PrimitiveRegressionTests {

    private static final MTRuntime runtime = MTRuntimeBootstrap.bootstrap();

    private PrimitiveRegressionTests() {
    }

    public static void runAll() {

        testIntegerAddition();

        testIntegerSubtraction();

        testIntegerMultiplication();

        testIntegerDivision();

        testIntegerModulo();

        testIntegerEquals();

        testIntegerDifferentFrom();

        testIntegerLessThan();

        testIntegerGreaterThan();

        testIntegerLessOrEqual();

        testIntegerGreaterOrEqual();

        testBooleanOperations();

        testStrings();
/*
        testNil();

        testSymbol();
*/
    }

    private static void testIntegerAddition() {
        System.out.println("=== Integer addition ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* + */

        MTArray plusArgs = new MTArray();
        plusArgs.add(b);

        assertResult(" 10 + 3", a.send(MTSymbol.intern("+"), plusArgs));
    }

    private static void testIntegerSubtraction() {
        System.out.println("=== Integer subtraction ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* + */

        MTArray minusArgs = new MTArray();
        minusArgs.add(b);

        assertResult("10 - 3", a.send(MTSymbol.intern("-"), minusArgs));
    }

    private static void testIntegerMultiplication() {
        System.out.println("=== Integer multiplication ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* + */

        MTArray multArgs = new MTArray();
        multArgs.add(b);

        assertResult("10 * 3", a.send(MTSymbol.intern("*"), multArgs));
    }

    private static void testIntegerDivision() {
        System.out.println("=== Integer division ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* + */

        MTArray divArgs = new MTArray();
        divArgs.add(b);

        assertResult("10 / 3", a.send(MTSymbol.intern("/"), divArgs));
    }

    private static void testIntegerModulo() {
        System.out.println("=== Integer modulo ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray modArgs = new MTArray();
        modArgs.add(b);

        assertResult("10 % 3", a.send(MTSymbol.intern("%"), modArgs));
    }

    private static void testIntegerEquals() {
        System.out.println("=== Integer Equals ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray eqArgs = new MTArray();
        eqArgs.add(b);

        assertResult("10 = 3", a.send(MTSymbol.intern("="), eqArgs));
    }

    private static void testIntegerDifferentFrom() {
        System.out.println("=== Integer Different from ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray neqArgs = new MTArray();
        neqArgs.add(b);

        assertResult("10 <> 3", a.send(MTSymbol.intern("<>"), neqArgs));
        assertResult("10 != 3", a.send(MTSymbol.intern("!="), neqArgs));
    }

    private static void testIntegerLessThan() {
        System.out.println("=== Integer Less than ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray ltArgs = new MTArray();
        ltArgs.add(b);

        assertResult("10 < 3", a.send(MTSymbol.intern("<"), ltArgs));
    }

    private static void testIntegerGreaterThan() {
        System.out.println("=== Integer Greater than ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray gtArgs = new MTArray();
        gtArgs.add(b);

        assertResult("10 > 3", a.send(MTSymbol.intern(">"), gtArgs));
    }

    private static void testIntegerLessOrEqual() {
        System.out.println("=== Integer Less or Equal to ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray leArgs = new MTArray();
        leArgs.add(b);

        assertResult("10 ~< 3", a.send(MTSymbol.intern("~<"), leArgs));
        assertResult("10 <= 3", a.send(MTSymbol.intern("<="), leArgs));
    }

    private static void testIntegerGreaterOrEqual() {
        System.out.println("=== Integer Greter or Equal to ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray geArgs = new MTArray();
        geArgs.add(b);

        assertResult("10 >~ 3", a.send(MTSymbol.intern(">~"), geArgs));
        assertResult("10 >= 3", a.send(MTSymbol.intern(">="), geArgs));
    }

    private static void testBooleanOperations() {

        System.out.println(
                "=== Boolean operations ===");

        MTClass booleanClass =
            MTKernelBootstrap
                .createBooleanClass();

        MTBoolean t = MTBoolean.TRUE;

        t.setClazz(booleanClass);

        MTBoolean f = MTBoolean.FALSE;

        f.setClazz(booleanClass);

        assertResult("true not",
            t.send(
                MTSymbol.intern("not")));

        assertResult("false not",
            f.send(
                MTSymbol.intern("not")));

        MTArray andArgs = new MTArray();

        andArgs.add(f);

        assertResult("true and: false",
            t.send(
                MTSymbol.intern("and:"),
                andArgs));

        MTArray orArgs = new MTArray();

        orArgs.add(f);

        assertResult("true or: false",
            t.send(
                MTSymbol.intern("or:"),
                orArgs));

        MTArray xorArgs = new MTArray();

        xorArgs.add(t);

        assertResult("true xor: true",
            t.send(
                MTSymbol.intern("xor:"),
                xorArgs));

        xorArgs = new MTArray();

        xorArgs.add(t);

        assertResult("true xor: false",
            f.send(
                MTSymbol.intern("xor:"),
                xorArgs));

        assertResult("true getClazz",
            MTBoolean.TRUE.getClazz());

        assertResult("false getClazz",
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

        assertResult("'Hello '",
            hello.send(
                MTSymbol.intern("size")));

        MTArray concatArgs = new MTArray();

        concatArgs.add(world);

        assertResult("'Hello ' + 'World'",
            hello.send(
                MTSymbol.intern("+"),
                concatArgs));

        MTArray eqArgs2 = new MTArray();

        eqArgs2.add(new MTString("Hello "));

        assertResult("'Hello ' = 'Hello '",
            hello.send(
                MTSymbol.intern("="),
                eqArgs2));

        MTArray eqArgs3 = new MTArray();

        eqArgs3.add(new MTString("World"));

        assertResult("'Hello ' = 'World'",
            hello.send(
                MTSymbol.intern("="),
                eqArgs3));

    }
}
