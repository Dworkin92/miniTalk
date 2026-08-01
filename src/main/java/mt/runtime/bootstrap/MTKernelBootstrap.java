package mt.runtime.bootstrap;

import mt.runtime.*;
import mt.runtime.primitives.*;

public final class MTKernelBootstrap {

    private MTKernelBootstrap() {
    }




    public static MTClass createIntegerClass(MTRuntime runtime, String className, MTClass superclass) {

        MTClass integerClass = new MTClass(MTSymbol.intern(className));
        integerClass.setSuperclass(superclass);

        MTClass integerMetaclass = new MTClass(
            MTSymbol.intern(className + "Class"));
        integerMetaclass.setSuperclass(superclass.getClazz());

        integerClass.setClazz(integerMetaclass);
        integerMetaclass.setClazz(runtime.getClassMetaclass());

        PrimitiveInstaller.install(
            integerClass,
            IntegerPrimitives.class);

        return integerClass;
    }

    public static MTClass createBooleanClass(MTRuntime runtime, String className, MTClass superclass) {

        MTClass booleanClass = new MTClass(MTSymbol.intern(className));
        booleanClass.setSuperclass(superclass);

        MTClass booleanMetaclass = new MTClass(
            MTSymbol.intern(className + "Class"));
        booleanMetaclass.setSuperclass(superclass.getClazz());

        booleanClass.setClazz(booleanMetaclass);
        booleanMetaclass.setClazz(runtime.getClassMetaclass());

        PrimitiveInstaller.install(
            booleanClass,
            BooleanPrimitives.class);

        return booleanClass;
    }

    public static MTClass createStringClass(MTRuntime runtime, String className, MTClass superclass) {
        MTClass stringClass = new MTClass(MTSymbol.intern(className));
        stringClass.setSuperclass(superclass);

        /* création de la meta classe et ajout de la superclass */
        MTClass stringMetaclass = new MTClass(
            MTSymbol.intern(className + "Class"));
        stringMetaclass.setSuperclass(superclass.getClazz());

        /* mise en place des classes */
        stringClass.setClazz(stringMetaclass);
        stringMetaclass.setClazz(runtime.getClassMetaclass());

        PrimitiveInstaller.install(
            stringClass,
            StringPrimitives.class);

        return stringClass;
    }

    public static MTClass createArrayClass(MTRuntime runtime, String className, MTClass superclass) {

        MTClass arrayClass = new MTClass(MTSymbol.intern(className));
        arrayClass.setSuperclass(superclass);

        /* création de la meta classe et ajout de la superclass */
        MTClass arrayMetaclass = new MTClass(
            MTSymbol.intern(className + "Class"));
        arrayMetaclass.setSuperclass(superclass.getClazz());

        /* mise en place des classes */
        arrayClass.setClazz(arrayMetaclass);
        arrayMetaclass.setClazz(runtime.getClassMetaclass());

        PrimitiveInstaller.install(
            arrayClass,
            ArrayPrimitives.class);

        return arrayClass;
    }

    public static MTClass createDictionaryClass(MTRuntime runtime, String className, MTClass superclass) {
        MTClass dictionaryClass = new MTClass(MTSymbol.intern(className));
        dictionaryClass.setSuperclass(superclass);

        /* création de la meta classe et ajout de la superclass */
        MTClass dictionaryMetaclass = new MTClass(
            MTSymbol.intern(className + "Class"));
        dictionaryMetaclass.setSuperclass(superclass.getClazz());

        /* mise en place des classes */
        dictionaryClass.setClazz(dictionaryMetaclass);
        dictionaryMetaclass.setClazz(runtime.getClassMetaclass());

        PrimitiveInstaller.install(
            dictionaryClass,
            DictionaryPrimitives.class);

        return dictionaryClass;
    }

    public static MTClass createBlockClass(MTRuntime runtime, String className, MTClass superclass) {
        MTClass blockClass = new MTClass(MTSymbol.intern(className));
        blockClass.setSuperclass(superclass);

        /* création de la meta classe et ajout de la superclass */
        MTClass blockMetaclass = new MTClass(
            MTSymbol.intern(className + "Class"));
        blockMetaclass.setSuperclass(superclass.getClazz());

        /* mise en place des classes */
        blockClass.setClazz(blockMetaclass);
        blockMetaclass.setClazz(runtime.getClassMetaclass());

        PrimitiveInstaller.install(
            blockClass,
            BlockPrimitives.class);

        return blockClass;
    }

    /*------------------------------------------------------------*/
    /* anciennes versions conservées pour ne pas casser les tests */
    /*------------------------------------------------------------*/
    public static MTClass createIntegerClass(MTClass superclass) {

        MTClass integerClass = new MTClass(MTSymbol.intern("Integer"));
        integerClass.setSuperclass(superclass);

        PrimitiveInstaller.install(
            integerClass,
            IntegerPrimitives.class);

        return integerClass;
    }

    public static MTClass createBooleanClass() {

        MTClass booleanClass =
                new MTClass(
                        MTSymbol.intern("Boolean"));


        PrimitiveInstaller.install(
            booleanClass,
            BooleanPrimitives.class);

        return booleanClass;
    }

    public static MTClass createStringClass() {
        MTClass stringClass =
                new MTClass(MTSymbol.intern("String"));
        //installStringMethods(stringClass);
        PrimitiveInstaller.install(
            stringClass,
            StringPrimitives.class);

        return stringClass;
    }

    public static MTClass createDictionaryClass() {
        MTClass dictionaryClass =
                new MTClass(
                        MTSymbol.intern("Dictionary"));

        //installDictionaryMethods(dictionaryClass);
        PrimitiveInstaller.install(
            dictionaryClass,
            DictionaryPrimitives.class);

        return dictionaryClass;
    }

    public static MTClass createArrayClass() {

        MTClass arrayClass =
            new MTClass(
                    MTSymbol.intern("Array"));

        PrimitiveInstaller.install(
            arrayClass,
            ArrayPrimitives.class);

        return arrayClass;
    }

    public static MTClass createBlockClass() {

        MTClass blockClass = new MTClass(
            MTSymbol.intern("Block"));

        PrimitiveInstaller.install(
            blockClass,
            BlockPrimitives.class);

        return blockClass;
    }

    /* =====  fin de la section des anciennes créations de classes === */


    private static void install(
        MTClass clazz,
        String selector,
        MTMethodBody body) {

        clazz.addMethod(

        new MTMethod(
                MTSymbol.intern(selector),
                clazz,
                body));
    }

}
