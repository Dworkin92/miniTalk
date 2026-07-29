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
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* + */

        MTArray plusArgs = new MTArray();
        plusArgs.add(b);

        assertResult(" 10 + 3", a.send(MTSymbol.intern("+"), plusArgs, scope));
    }

    private static void testIntegerSubtraction() {
        System.out.println("=== Integer subtraction ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* + */

        MTArray minusArgs = new MTArray();
        minusArgs.add(b);

        assertResult("10 - 3", a.send(MTSymbol.intern("-"), minusArgs, scope));
    }

    private static void testIntegerMultiplication() {
        System.out.println("=== Integer multiplication ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* + */

        MTArray multArgs = new MTArray();
        multArgs.add(b);

        assertResult("10 * 3", a.send(MTSymbol.intern("*"), multArgs, scope));
    }

    private static void testIntegerDivision() {
        System.out.println("=== Integer division ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* + */

        MTArray divArgs = new MTArray();
        divArgs.add(b);

        assertResult("10 / 3", a.send(MTSymbol.intern("/"), divArgs, scope));
    }

    private static void testIntegerModulo() {
        System.out.println("=== Integer modulo ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray modArgs = new MTArray();
        modArgs.add(b);

        assertResult("10 % 3", a.send(MTSymbol.intern("%"), modArgs, scope));
    }

    private static void testIntegerEquals() {
        System.out.println("=== Integer Equals ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray eqArgs = new MTArray();
        eqArgs.add(b);

        assertResult("10 = 3", a.send(MTSymbol.intern("="), eqArgs, scope));
    }

    private static void testIntegerDifferentFrom() {
        System.out.println("=== Integer Different from ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray neqArgs = new MTArray();
        neqArgs.add(b);

        assertResult("10 <> 3", a.send(MTSymbol.intern("<>"), neqArgs, scope));
        assertResult("10 != 3", a.send(MTSymbol.intern("!="), neqArgs, scope));
    }

    private static void testIntegerLessThan() {
        System.out.println("=== Integer Less than ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray ltArgs = new MTArray();
        ltArgs.add(b);

        assertResult("10 < 3", a.send(MTSymbol.intern("<"), ltArgs, scope));
    }

    private static void testIntegerGreaterThan() {
        System.out.println("=== Integer Greater than ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray gtArgs = new MTArray();
        gtArgs.add(b);

        assertResult("10 > 3", a.send(MTSymbol.intern(">"), gtArgs, scope));
    }

    private static void testIntegerLessOrEqual() {
        System.out.println("=== Integer Less or Equal to ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray leArgs = new MTArray();
        leArgs.add(b);

        assertResult("10 ~< 3", a.send(MTSymbol.intern("~<"), leArgs, scope));
        assertResult("10 <= 3", a.send(MTSymbol.intern("<="), leArgs, scope));
    }

    private static void testIntegerGreaterOrEqual() {
        System.out.println("=== Integer Greter or Equal to ===");
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime.getObjectClass());
        MTScope scope = new MTScope(runtime, null);

        MTInteger a = new MTInteger(10);
        a.setClazz(integerClass);
        MTInteger b = new MTInteger(3);
        b.setClazz(integerClass);

        /* % */

        MTArray geArgs = new MTArray();
        geArgs.add(b);

        assertResult("10 >~ 3", a.send(MTSymbol.intern(">~"), geArgs, scope));
        assertResult("10 >= 3", a.send(MTSymbol.intern(">="), geArgs, scope));
    }

    private static void testBooleanOperations() {

        System.out.println(
                "=== Boolean operations ===");

        MTClass booleanClass = MTKernelBootstrap.createBooleanClass();
        MTScope scope = new MTScope(runtime, null);

        MTBoolean t = MTBoolean.TRUE;

        t.setClazz(booleanClass);

        MTBoolean f = MTBoolean.FALSE;

        f.setClazz(booleanClass);

        assertResult("true not",
            t.send(
                MTSymbol.intern("not"), scope));

        assertResult("false not",
            f.send(
                MTSymbol.intern("not"), scope));

        MTArray andArgs = new MTArray();

        andArgs.add(f);

        assertResult("true and: false",
            t.send(
                MTSymbol.intern("and:"),
                andArgs, scope));

        MTArray orArgs = new MTArray();

        orArgs.add(f);

        assertResult("true or: false",
            t.send(
                MTSymbol.intern("or:"),
                orArgs, scope));

        MTArray xorArgs = new MTArray();

        xorArgs.add(t);

        assertResult("true xor: true",
            t.send(
                MTSymbol.intern("xor:"),
                xorArgs, scope));

        xorArgs = new MTArray();

        xorArgs.add(t);

        assertResult("true xor: false",
            f.send(
                MTSymbol.intern("xor:"),
                xorArgs, scope));

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
        MTScope scope = new MTScope(runtime, null);

        MTString hello = new MTString("Hello ");

        hello.setClazz(stringClass);

        MTString world = new MTString("World");

        world.setClazz(stringClass);

        assertResult("'Hello '",
            hello.send(
                MTSymbol.intern("size"), scope));

        MTArray concatArgs = new MTArray();

        concatArgs.add(world);

        assertResult("'Hello ' + 'World'",
            hello.send(
                MTSymbol.intern("+"),
                concatArgs, scope));

        MTArray eqArgs2 = new MTArray();

        eqArgs2.add(new MTString("Hello "));

        assertResult("'Hello ' = 'Hello '",
            hello.send(
                MTSymbol.intern("="),
                eqArgs2, scope));

        MTArray eqArgs3 = new MTArray();

        eqArgs3.add(new MTString("World"));

        assertResult("'Hello ' = 'World'",
            hello.send(
                MTSymbol.intern("="),
                eqArgs3, scope));

    }
}
