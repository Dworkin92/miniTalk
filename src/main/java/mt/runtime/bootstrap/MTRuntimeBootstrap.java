package mt.runtime.bootstrap;

import mt.runtime.*;

public final class MTRuntimeBootstrap {

    private MTRuntimeBootstrap() {
    }

    public static MTRuntime bootstrap() {

        /*
         * Construction brute
         */
        MTClass objectClass =
                new MTClass(
                        MTSymbol.intern("Object"));

        MTClass classClass =
                new MTClass(
                        MTSymbol.intern("Class"));

        MTMetaclass objectMetaclass =
                new MTMetaclass(
                        MTSymbol.intern("ObjectClass"));

        MTMetaclass classMetaclass =
                new MTMetaclass(
                        MTSymbol.intern("ClassClass"));


        MTRuntime runtime =
                new MTRuntime(
                    objectClass,
                    classClass,
                    objectMetaclass,
                    classMetaclass);

        /*
         * Relier la hierarchie
         */

        objectClass.setSuperclass(
                null);

        classClass.setSuperclass(
                objectClass);

        objectMetaclass.setSuperclass(
                classMetaclass);

        classMetaclass.setSuperclass(
                objectMetaclass);

        /*
         * Relier les classes
         */

        objectClass.setClazz(
                objectMetaclass);

        classClass.setClazz(
                classMetaclass);

        objectMetaclass.setClazz(
                classMetaclass);

        classMetaclass.setClazz(
                classMetaclass);


        runtime.registerClass(objectClass);

        runtime.registerClass(classClass);

        runtime.registerClass(objectMetaclass);

        runtime.registerClass(classMetaclass);


        runtime.registerClass(
            MTKernelBootstrap.createIntegerClass());

        runtime.registerClass(
            MTKernelBootstrap.createBooleanClass());

        runtime.registerClass(
            MTKernelBootstrap.createStringClass());

        runtime.registerClass(
            MTKernelBootstrap.createArrayClass());

        runtime.registerClass(
            MTKernelBootstrap.createDictionaryClass());


        /*
        return new MTRuntime(
                objectClass,
                classClass,
                objectMetaclass,
                classMetaclass);
        */
        return runtime;
    }
}
