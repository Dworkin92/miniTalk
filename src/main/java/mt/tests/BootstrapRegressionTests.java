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
import static mt.tests.TestUtils.assertEquals;

public final class BootstrapRegressionTests {

    private static final MTRuntime runtime = MTRuntimeBootstrap.bootstrap();

    private BootstrapRegressionTests() {
    }

    public static void runAll() {
        testIntegerClassCreation();

        testBooleanClassCreation();

        testStringClassCreation();

        testArrayClassCreation();

        testDictionaryClassCreation();

        testBlockClassCreation();

        testUserClassCreation();

        testClassDefInstaller();

        testQuadrant();
    }

    private static void testIntegerClassCreation() {

        System.out.println(
            "=== Integer class creation test ===");

        MTClass integerClass =
            MTKernelBootstrap.createIntegerClass(
                runtime,
                "Integer",
                runtime.getObjectClass());

System.out.println("Integer class name = " +
    integerClass.getName());

System.out.println("Integer metaclass name = " +
    integerClass.getClazz().getName());

System.out.println("Integer class superclass name = " +
    integerClass.getSuperclass().getName());

System.out.println("Integer metaclass superclass name = " +
    integerClass.getClazz()
                .getSuperclass()
                .getName());

System.out.println("Integer Metaclass class name = " +
    integerClass.getClazz()
                .getClazz()
                .getName());

        assertEquals(
            "Integer metaclass class",
            "#ClassClass",
            integerClass.getClazz().getClazz().getName().toString());
    }

    private static void testBooleanClassCreation() {

        System.out.println(
            "=== Boolean class creation test ===");

        MTClass booleanClass =
            MTKernelBootstrap.createBooleanClass(
                runtime,
                "Boolean",
                runtime.getObjectClass());

        System.out.println("Boolean class name = " +
            booleanClass.getName());

        System.out.println("Boolean metaclass name = " +
            booleanClass.getClazz().getName());

        System.out.println("Boolean class superclass name = " +
            booleanClass.getSuperclass().getName());

        System.out.println("Boolean metaclass superclass name = " +
            booleanClass.getClazz()
                .getSuperclass()
                .getName());

        System.out.println("Boolean Metaclass class name = " +
            booleanClass.getClazz()
                .getClazz()
                .getName());

        assertEquals(
            "Boolean metaclass class",
            "#ClassClass",
            booleanClass.getClazz().getClazz().getName().toString());
    }

    private static void testStringClassCreation() {

        System.out.println(
            "=== String class creation test ===");

        MTClass stringClass =
            MTKernelBootstrap.createStringClass(
                runtime,
                "String",
                runtime.getObjectClass());

        System.out.println("String class name = " +
            stringClass.getName());

        System.out.println("String metaclass name = " +
            stringClass.getClazz().getName());

        System.out.println("String class superclass name = " +
            stringClass.getSuperclass().getName());

        System.out.println("String metaclass superclass name = " +
            stringClass.getClazz()
                .getSuperclass()
                .getName());

        System.out.println("String Metaclass class name = " +
            stringClass.getClazz()
                .getClazz()
                .getName());

        assertEquals(
            "String metaclass class",
            "#ClassClass",
            stringClass.getClazz().getClazz().getName().toString());
    }

    private static void testArrayClassCreation() {

        System.out.println(
            "=== Array class creation test ===");

        MTClass arrayClass =
            MTKernelBootstrap.createArrayClass(
                runtime,
                "Array",
                runtime.getObjectClass());

        System.out.println("Array class name = " +
            arrayClass.getName());

        System.out.println("Array metaclass name = " +
            arrayClass.getClazz().getName());

        System.out.println("Array class superclass name = " +
            arrayClass.getSuperclass().getName());

        System.out.println("Array metaclass superclass name = " +
            arrayClass.getClazz()
                .getSuperclass()
                .getName());

        System.out.println("Array Metaclass class name = " +
            arrayClass.getClazz()
                .getClazz()
                .getName());

        assertEquals(
            "Array metaclass class",
            "#ClassClass",
            arrayClass.getClazz().getClazz().getName().toString());
    }

    private static void testDictionaryClassCreation() {

        System.out.println(
            "=== Dictionary class creation test ===");

        MTClass dictionaryClass =
            MTKernelBootstrap.createDictionaryClass(
                runtime,
                "Dictionary",
                runtime.getObjectClass());

        System.out.println("Dictionary class name = " +
            dictionaryClass.getName());

        System.out.println("Dictionary metaclass name = " +
            dictionaryClass.getClazz().getName());

        System.out.println("Dictionary class superclass name = " +
            dictionaryClass.getSuperclass().getName());

        System.out.println("Dictionary metaclass superclass name = " +
            dictionaryClass.getClazz()
                .getSuperclass()
                .getName());

        System.out.println("Dictionary Metaclass class name = " +
            dictionaryClass.getClazz()
                .getClazz()
                .getName());

        assertEquals(
            "Dictionary metaclass class",
            "#ClassClass",
            dictionaryClass.getClazz().getClazz().getName().toString());
    }

    private static void testBlockClassCreation() {

        System.out.println(
            "=== Block class creation test ===");

        MTClass blockClass =
            MTKernelBootstrap.createBlockClass(
                runtime,
                "Block",
                runtime.getObjectClass());

        System.out.println("Block class name = " +
            blockClass.getName());

        System.out.println("Block metaclass name = " +
            blockClass.getClazz().getName());

        System.out.println("Block class superclass name = " +
            blockClass.getSuperclass().getName());

        System.out.println("Block metaclass superclass name = " +
            blockClass.getClazz()
                .getSuperclass()
                .getName());

        System.out.println("Block Metaclass class name = " +
            blockClass.getClazz()
                .getClazz()
                .getName());

        assertEquals(
            "Block metaclass class",
            "#ClassClass",
            blockClass.getClazz().getClazz().getName().toString());
    }

    public static void testUserClassCreation() {

    System.out.println(
        "=== User class creation test ===");

    MTRuntime runtime =
        MTRuntimeBootstrap.bootstrap();

    MTClass objectClass =
        runtime.getObjectClass();

    MTClass personClass =
        new MTClass(
            MTSymbol.intern("Person"));

    personClass.setSuperclass(
        objectClass);

    MTMetaclass personMetaclass =
        new MTMetaclass(
            MTSymbol.intern("PersonClass"));

    personMetaclass.setSuperclass(
        (MTClass) objectClass.getClazz());

    personMetaclass.setClazz(
        runtime.getClassMetaclass());

    personClass.setClazz(
        personMetaclass);

    System.out.println(
        "Person class name = "
        + personClass.getName());

    System.out.println(
        "Person metaclass name = "
        + personClass.getClazz().getName());

    System.out.println(
        "Person metaclass superclass name = "
        + personClass.getClazz()
                     .getSuperclass()
                     .getName());

    System.out.println(
        "Person metaclass class name = "
        + personClass.getClazz()
                     .getClazz()
                     .getName());

    MTClass employeeClass = new MTClass(
        MTSymbol.intern("Employee"));

employeeClass.setSuperclass(
    personClass);

MTMetaclass employeeMetaclass =
    new MTMetaclass(
        MTSymbol.intern("EmployeeClass"));

employeeMetaclass.setSuperclass(
    (MTClass) personClass.getClazz());

employeeMetaclass.setClazz(
    runtime.getClassMetaclass());

employeeClass.setClazz(
    employeeMetaclass);

        assertEquals(
            "Employee class name",
            "#Employee",
            employeeClass.getName().toString());

        assertEquals(
            "Employee metaclass name",
            "#EmployeeClass",
            employeeClass.getClazz().getName().toString());

        assertEquals(
            "Employee class superclass name",
            "#Person",
            employeeClass.getSuperclass().getName().toString());

        assertEquals(
            "Employee metaclass superclass name",
            "#PersonClass",
            employeeClass.getClazz().getSuperclass().getName().toString());

        assertEquals(
            "Employee metaclass class name",
            "#ClassClass",
            employeeClass.getClazz().getClazz().getName().toString());
    }

    public static void testClassDefInstaller() {

        System.out.println("=== ClassDefInstaller test ===");

        MTRuntime runtime = MTRuntimeBootstrap.bootstrapCoreOnly();

        MTClass integerClass = ClassDefInstaller.install(runtime,IntegerClassDef.class);
        MTMetaclass integerMetaclass = integerClass.getMetaclazz();

        assertEquals(
            "Integer : name",
            "#Integer",
            integerClass.getName().toString());

        assertEquals(
            "Integer : metaclass name",
            "#IntegerClass",
            integerMetaclass.getName().toString());

        assertEquals(
            "Integer : class name",
            "#Class",
            integerClass.getClazz().getName().toString());

        assertEquals(
            "Integer : superclass name",
            "#Object",
            integerClass.getSuperclass().getName().toString());

        assertEquals(
            "IntegerClass : class name",
            "#ClassClass",
            integerMetaclass.getClazz().getName().toString());

        assertEquals(
            "IntegerClass : superclass name",
            "#ObjectClass",
            integerMetaclass.getSuperclass().getName().toString());

        if ( integerMetaclass.getMetaclazz() == null)
            System.out.println("IntegerClass : metaclass name expected: null ==> OK");
        else
            System.out.println("IntegerClass : metaclass name expected: null ==> ECHEC");

    }

    public static void testQuadrant() {
        System.out.println("=== Magic Quad test ===");

        MTRuntime runtime = MTRuntimeBootstrap.bootstrapCoreOnly();

        /* Object */
        MTClass objectClass = runtime.getObjectClass();
        assertEquals("Object : name",
            "#Object",
            objectClass.getName().toString());

        assertEquals("Object : is instance of ",
            "#Class",
            objectClass.getClazz().getName().toString());

        if ( objectClass.getSuperclass() == null)
            System.out.println("Object : superclass name expected: null ==> OK");
        else
            System.out.println("Object : superclass name expected: null ==> ECHEC");


        assertEquals("Object : metaclass name",
                     "#ObjectClass",
                     objectClass.getMetaclazz().getName().toString());
        /* Class */
        MTClass classClass = runtime.getClassClass();
        assertEquals("Class : name",
            "#Class",
            classClass.getName().toString());
        assertEquals("Class : is instance of",
            "#Class",
            classClass.getClazz().getName().toString());
        assertEquals("Class : superclass name",
            "#Object",
            classClass.getSuperclass().getName().toString());
        assertEquals("Class : metaclass name",
            "#ClassClass",
            classClass.getMetaclazz().getName().toString());

        /* Object MetaClasse */
        MTClass objectMetaclass = runtime.getObjectMetaclass();
        assertEquals("ObjectClass :  name",
            "#ObjectClass",
            objectMetaclass.getName().toString());
        assertEquals("ObjectClass : is instance of",
            "#ClassClass",
            objectMetaclass.getClazz().getName().toString());
        assertEquals("ObjectClass : superclass name",
            "#Object",
            objectMetaclass.getSuperclass().getName().toString());

        if ( objectMetaclass.getMetaclazz() == null)
            System.out.println("ObjectClass : metaclass name expected: null ==> OK");
        else
            System.out.println("ObjectClass : metaclass name expected: null ==> ECHEC");

        /* Class MetaClasse */
        MTClass classMetaclass = runtime.getClassMetaclass();
        assertEquals("ClassClass : name",
            "#ClassClass",
            classMetaclass.getName().toString());
        assertEquals("ClassClass : is instance of",
            "#Class",
            classMetaclass.getClazz().getName().toString());
        assertEquals("ClassClass : superclass name",
            "#ObjectClass",
            classMetaclass.getSuperclass().getName().toString());

        if ( classMetaclass.getMetaclazz() == null)
            System.out.println("ClassClass : metaclass name expected: null ==> OK");
        else
            System.out.println("ClassClass : metaclass name expected: null ==> ECHEC");

    }
}
