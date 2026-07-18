package mt.runtime.bootstrap;

import mt.runtime.*;
import mt.runtime.primitives.*;

public final class MTRuntimeBootstrap {

    private MTRuntimeBootstrap() {
    }

    public static MTRuntime bootstrap() {

        /*
         * Construction brute
         */

        MTRuntime runtime = bootstrapCore();

        bootstrapFirstClasses(runtime);

        bootstrapPrimitives(runtime);

        return runtime;
    }

    private static MTRuntime bootstrapCore() {

        MTClass objectClass = new MTClass(
                MTSymbol.intern("Object"));

        MTClass classClass = new MTClass(
                MTSymbol.intern("Class"));

        MTMetaclass objectMetaclass = new MTMetaclass(
                MTSymbol.intern("ObjectClass"));

        MTMetaclass classMetaclass = new MTMetaclass(
                MTSymbol.intern("ClassClass"));

        MTRuntime runtime = new MTRuntime(
                objectClass,
                classClass,
                objectMetaclass,
                classMetaclass);

        /*
         * Héritage
         */

        objectClass.setSuperclass(null);

        classClass.setSuperclass(objectClass);

        objectMetaclass.setSuperclass(classMetaclass);

        classMetaclass.setSuperclass(objectMetaclass);
        //classMetaclass.setSuperclass(classMetaclass);

        /*
         * Instance-of
         */

        /*
        objectClass.setClazz(objectMetaclass);

        classClass.setClazz(classMetaclass);

        objectMetaclass.setClazz(classMetaclass);

        classMetaclass.setClazz(classMetaclass);
        */

        objectClass.setClazz(classClass);

        classClass.setClazz(classClass);

        objectMetaclass.setClazz(classMetaclass);

        classMetaclass.setClazz(classMetaclass);

        /*
         * Classe -> métaclasse associée
         */

        objectClass.setMetaclass(objectMetaclass);

        classClass.setMetaclass(classMetaclass);

        objectMetaclass.setMetaclass(classMetaclass);

        classMetaclass.setMetaclass(classMetaclass);




        runtime.registerClass(objectClass);

        runtime.registerClass(classClass);

        runtime.registerClass(objectMetaclass);

        runtime.registerClass(classMetaclass);

        return runtime;
    }

    private static void bootstrapFirstClasses(MTRuntime runtime) {

        MTClass objectClass = runtime.getObjectClass();

        MTClass integerClass = MTKernelBootstrap.createIntegerClass(objectClass);
        integerClass.setSuperclass(objectClass);
        runtime.registerClass(integerClass);

        MTClass booleanClass = MTKernelBootstrap.createBooleanClass();
        booleanClass.setSuperclass(objectClass);
        runtime.registerClass(booleanClass);

        MTClass stringClass = MTKernelBootstrap.createStringClass();
        stringClass.setSuperclass(objectClass);
        runtime.registerClass(stringClass);

        MTClass arrayClass = MTKernelBootstrap.createArrayClass();
        arrayClass.setSuperclass(objectClass);
        runtime.registerClass(arrayClass);

        MTClass dictionaryClass = MTKernelBootstrap.createDictionaryClass();
        dictionaryClass.setSuperclass(objectClass);
        runtime.registerClass(dictionaryClass);

        MTClass blockClass = MTKernelBootstrap.createBlockClass();
        blockClass.setSuperclass(objectClass);
        runtime.registerClass(blockClass);
    }

    private static void bootstrapPrimitives(MTRuntime runtime) {
        PrimitiveInstaller.install(
            runtime.getObjectClass(),
            ObjectPrimitives.class);
    }
}
