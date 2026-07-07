package mt;

import mt.runtime.*;
import mt.runtime.bootstrap.MTKernelBootstrap;
import mt.runtime.bootstrap.MTRuntimeBootstrap;

public class Main {

    public static void main(String[] args) {

        /*
         * Symbol test
         */
        System.out.println("=== Symbol test ===");

        MTSymbol age1 =
                MTSymbol.intern("age");

        System.out.println(age1.getValue());
        System.out.println(age1);


        MTSymbol age2 =
                MTSymbol.intern("age");

        System.out.println(age1 == age2);

        /*
         * Dynamic properties
         */

        System.out.println(
                "=== Dynamic property test ===");

        MTClass person =
                new MTClass(
                        MTSymbol.intern("Person"));

        person.addProperty(
                MTSymbol.intern("age"));

        MTObject p =
                person.newInstance();

        p.setProperty(
                MTSymbol.intern("age"),
                new MTInteger(38));

        person.addProperty(
                MTSymbol.intern("name"));

        System.out.println(
                p.getProperty(
                        MTSymbol.intern("name")));

        System.out.println(
                p.getProperty(
                        MTSymbol.intern("age")));

        System.out.println(
                "Before rebind: "
                        + p.propertyCount());

        p.rebindProps();

        System.out.println(
                "After rebind: "
                        + p.propertyCount());

        System.out.println(
                p.getProperty(
                        MTSymbol.intern("age")));

        /*
         * Property inheritance
         */

        System.out.println(
                "=== Property inheritance ===");

        MTClass employee =
                new MTClass(
                        MTSymbol.intern("Employee"));

        employee.setSuperclass(
                person);

        employee.addProperty(
                MTSymbol.intern("salary"));

        MTObject e =
                employee.newInstance();

        System.out.println(
                e.getProperty(
                        MTSymbol.intern("age")));

        System.out.println(
                e.getProperty(
                        MTSymbol.intern("salary")));

        /*
         * Integer operations
         */

System.out.println(
        "=== Integer operations ===");

MTClass integerClass =
        MTKernelBootstrap
                .createIntegerClass();

MTInteger a =
        new MTInteger(10);

a.setClazz(
        integerClass);

MTInteger b =
        new MTInteger(3);

b.setClazz(
        integerClass);

/*
 * +
 */

MTArray plusArgs = new MTArray();

plusArgs.add(b);

System.out.println(
        a.send(
                MTSymbol.intern("+"),
                plusArgs));

/*
 * -
 */

MTArray minusArgs =
        new MTArray();

minusArgs.add(b);

System.out.println(
        a.send(
                MTSymbol.intern("-"),
                minusArgs));

/*
 * *
 */

MTArray multArgs =
        new MTArray();

multArgs.add(b);

System.out.println(
        a.send(
                MTSymbol.intern("*"),
                multArgs));

/*
 * /
 */

MTArray divArgs =
        new MTArray();

divArgs.add(b);

System.out.println(
        a.send(
                MTSymbol.intern("/"),
                divArgs));

/*
 * =
 */

MTArray eqArgs =
        new MTArray();

eqArgs.add(b);

System.out.println(
        a.send(
                MTSymbol.intern("="),
                eqArgs));

/*
 * <
 */

MTArray ltArgs =
        new MTArray();

ltArgs.add(b);

System.out.println(
        a.send(
                MTSymbol.intern("<"),
                ltArgs));

/*
 * >
 */

MTArray gtArgs =
        new MTArray();

gtArgs.add(b);

System.out.println(
        a.send(
                MTSymbol.intern(">"),
                gtArgs));

/*
 * ~<
 */

MTArray leArgs =
        new MTArray();

leArgs.add(b);

System.out.println(
        a.send(
                MTSymbol.intern("~<"),
                leArgs));

/*
 * >~
 */

MTArray geArgs =
        new MTArray();

geArgs.add(b);

System.out.println(
        a.send(
                MTSymbol.intern(">~"),
                geArgs));

        /*
         * Boolean
         */

        System.out.println(
                "=== Boolean test ===");

        System.out.println(
                MTBoolean.TRUE);

        System.out.println(
                MTBoolean.FALSE);

        System.out.println(
                MTBoolean.valueOf(true));

        System.out.println(
                MTBoolean.valueOf(false));

        /*
         * Method invocation
         */

        System.out.println(
                "=== Method invocation ===");

        MTMethod greet =
                new MTMethod(
                        MTSymbol.intern("greet"),
                        person,
                        (receiver, arguments1) -> {

                            System.out.println(
                                    "Hello World");

                            return MTNil.instance();
                        });

        person.addMethod(greet);

        p.send(
                MTSymbol.intern("greet"));

        /*
         * Method inheritance
         */

        System.out.println(
                "=== Method inheritance ===");

        e.send(
                MTSymbol.intern("greet"));

        /*
         * Method override
         */

        MTMethod employeeGreet =
                new MTMethod(
                        MTSymbol.intern("greet"),
                        employee,
                        (receiver, arguments1) -> {

                            System.out.println(
                                    "Hello Employee");

                            return MTNil.instance();
                        });

        employee.addMethod(
                employeeGreet);

        System.out.println(
                "=== Method override ===");

        e.send(
                MTSymbol.intern("greet"));

        /*
         * send() with arguments
         */

        System.out.println(
                "=== Array and Integer ===");


        MTInteger three =
                new MTInteger(3);

        three.setClazz(
                integerClass);

        MTArray plusArgs2 =
                new MTArray();

        plusArgs2.add(
                new MTInteger(4));

        MTObject result =
                three.send(
                        MTSymbol.intern("+"),
                        plusArgs2);

        System.out.println(
                result);


System.out.println(
        "=== String messages ===");

MTClass stringClass =
        MTKernelBootstrap
                .createStringClass();

MTString hello =
        new MTString("Hello ");

hello.setClazz(
        stringClass);

MTString world =
        new MTString("World");

world.setClazz(
        stringClass);

System.out.println(
        hello.send(
                MTSymbol.intern("size")));


MTArray concatArgs =
        new MTArray();

concatArgs.add(world);

System.out.println(
        hello.send(
                MTSymbol.intern("+"),
                concatArgs));


MTArray eqArgs2 =
        new MTArray();

eqArgs2.add(
        new MTString("Hello "));

System.out.println(
        hello.send(
                MTSymbol.intern("="),
                eqArgs2));


MTArray eqArgs3 =
        new MTArray();

eqArgs3.add(
        new MTString("World"));

System.out.println(
        hello.send(
                MTSymbol.intern("="),
                eqArgs3));

                System.out.println(
        "=== Bootstrap ===");

MTRuntime runtime =
        MTRuntimeBootstrap.bootstrap();

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

    System.out.println(
        "* Dictionary *");
    MTClass dictionaryClass =
        MTKernelBootstrap
                .createDictionaryClass();

MTDictionary dict =
        new MTDictionary();

dict.setClazz(
        dictionaryClass);

MTArray argsPut =
        new MTArray();

argsPut.add(
        MTSymbol.intern("age"));

argsPut.add(
        new MTInteger(38));

dict.send(
        MTSymbol.intern("at:put:"),
        argsPut);

MTArray argsAt =
        new MTArray();

argsAt.add(
        MTSymbol.intern("age"));

System.out.println(
        dict.send(
                MTSymbol.intern("at:"),
                argsAt));

MTArray sizeArgs =
        new MTArray();

System.out.println(
        dict.send(
                MTSymbol.intern("size"),
                sizeArgs));


MTArray includesArgs =
        new MTArray();

includesArgs.add(
        MTSymbol.intern("age"));

System.out.println(
        dict.send(
                MTSymbol.intern("includesKey:"),
                includesArgs));

}
}
