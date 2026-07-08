package mt.runtime.bootstrap;

import mt.runtime.*;
import mt.runtime.primitives.*;

public final class MTKernelBootstrap {

    private MTKernelBootstrap() {
    }

    public static MTClass createIntegerClass() {

        MTClass integerClass =
                new MTClass(
                        MTSymbol.intern("Integer"));


        /*
        MTClass integerClass =
            runtime.createClass("Integer", runtime.getObjectClass());

        runtime.registerClass(integerClass);
        */

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
