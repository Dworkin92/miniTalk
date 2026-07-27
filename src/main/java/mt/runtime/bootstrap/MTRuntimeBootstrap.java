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

        /*
         * Mop v1.0
         *
        objectMetaclass.setSuperclass(classMetaclass);

        classMetaclass.setSuperclass(objectMetaclass);
         **/

        /*
         * Mop v1.2
         */
        objectMetaclass.setSuperclass(objectClass);

        classMetaclass.setSuperclass(objectMetaclass);
         /**/


        /*
         * Instance-of
         */

        /** Mop v1.0
         *
        objectClass.setClazz(objectMetaclass);

        classClass.setClazz(classMetaclass);

        objectMetaclass.setClazz(classMetaclass);

        classMetaclass.setClazz(classMetaclass);
        */

        /** Mop v1.2
         */
        objectClass.setClazz(classClass);

        classClass.setClazz(classClass);

        objectMetaclass.setClazz(classMetaclass);

        classMetaclass.setClazz(classClass);


        objectClass.setMetaclazz(objectMetaclass);

        classClass.setMetaclazz(classMetaclass);

        objectMetaclass.setMetaclazz((MTMetaclass)null);

        classMetaclass.setMetaclazz((MTMetaclass)null);




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

        PrimitiveInstaller.install(
            runtime.getClassMetaclass(),
            ClassPrimitives.class);

    }

    private static void bootstrapFirstClasses(MTRuntime runtime) {

        MTClass objectClass = runtime.getObjectClass();


        MTClass nilClass = ClassDefInstaller.install(runtime, NilClassDef.class);
        MTNil.setNilClass(nilClass);

        MTClass integerClass = ClassDefInstaller.install(runtime, IntegerClassDef.class);


        MTClass booleanClass = ClassDefInstaller.install(runtime, BooleanClassDef.class);
        MTBoolean.setBooleanClass(booleanClass);

        MTClass stringClass = ClassDefInstaller.install(runtime, StringClassDef.class);
        MTString.setStringClass(stringClass);

        MTClass symbolClass = ClassDefInstaller.install(runtime, SymbolClassDef.class);
        bindInternedSymbols(symbolClass);


        MTClass arrayClass = ClassDefInstaller.install(runtime, ArrayClassDef.class);


        MTClass dictionaryClass = ClassDefInstaller.install(runtime, DictionaryClassDef.class);


        MTClass blockClass = ClassDefInstaller.install(runtime, BlockClassDef.class);

    }

    private static void bindInternedSymbols(MTClass symbolClass) {
        MTSymbol.setSymbolClass(symbolClass);

        for (MTSymbol symbol : MTSymbol.allInterned()) {

            symbol.setClazz(symbolClass);
        }
    }

}
