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

        bootstrapCorePrimitives(runtime);

        bootstrapFirstClasses(runtime);

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

        objectClass.setClazz(objectMetaclass);

        classClass.setClazz(classMetaclass);

        objectMetaclass.setClazz(classMetaclass);

        classMetaclass.setClazz(classMetaclass);



        runtime.registerClass(objectClass);

        runtime.registerClass(classClass);

        runtime.registerClass(objectMetaclass);

        runtime.registerClass(classMetaclass);

        return runtime;
    }

    /**
     * classe spécialement créée pour les tests afin de
     * rendre visible l'accès a bootstrapCore()
     */
    public static MTRuntime bootstrapCoreOnly() {
        return bootstrapCore();
    }

    private static void bootstrapCorePrimitives(MTRuntime runtime) {
        PrimitiveInstaller.install(
            runtime.getObjectClass(),
            ObjectPrimitives.class);
    }

    private static void bootstrapFirstClasses(MTRuntime runtime) {

        MTClass objectClass = runtime.getObjectClass();


        MTClass integerClass = ClassDefInstaller.install(runtime, IntegerClassDef.class);
        PrimitiveInstaller.install(integerClass, IntegerPrimitives.class);

        /*
        MTClass integerClass = MTKernelBootstrap.createIntegerClass(runtime, "Integer", objectClass);
        //integerClass.setSuperclass(objectClass);
        runtime.registerClass(integerClass);
        runtime.registerClass(integerClass.getClazz());
        */

        MTClass booleanClass = MTKernelBootstrap.createBooleanClass(runtime, "Boolean", objectClass);
        //booleanClass.setSuperclass(objectClass);
        runtime.registerClass(booleanClass);
        runtime.registerClass(booleanClass.getClazz());

        MTClass stringClass = MTKernelBootstrap.createStringClass(runtime, "String", objectClass);
        //stringClass.setSuperclass(objectClass);
        runtime.registerClass(stringClass);
        runtime.registerClass(stringClass.getClazz());

        MTClass arrayClass = MTKernelBootstrap.createArrayClass(runtime, "Array", objectClass);
        //arrayClass.setSuperclass(objectClass);
        runtime.registerClass(arrayClass);
        runtime.registerClass(arrayClass.getClazz());

        MTClass dictionaryClass = MTKernelBootstrap.createDictionaryClass(runtime, "Dictionary", objectClass);
        dictionaryClass.setSuperclass(objectClass);
        runtime.registerClass(dictionaryClass);

        MTClass blockClass = MTKernelBootstrap.createBlockClass(runtime, "Block", objectClass);
        blockClass.setSuperclass(objectClass);
        runtime.registerClass(blockClass);
    }


}
