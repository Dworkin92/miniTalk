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
        installStringMethods(stringClass);
        return stringClass;
    }

    public static MTClass createDictionaryClass() {
        MTClass dictionaryClass =
                new MTClass(
                        MTSymbol.intern("Dictionary"));

        installDictionaryMethods(dictionaryClass);

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


    private static void installStringMethods(MTClass stringClass){
        stringClass.addMethod(
            new MTMethod(
                MTSymbol.intern("+"),
                stringClass,
                (receiver, arguments) -> {
                    MTString left = (MTString) receiver;
                    MTString right = (MTString) arguments.at(0);

                    return new MTString(
                        left.getValue()
                        + right.getValue());
            }));


        stringClass.addMethod(
            new MTMethod(
                MTSymbol.intern("size"),
                stringClass,
                (receiver, arguments) -> {
                    MTString self = (MTString) receiver;

                    return new MTInteger(self.getValue().length());
            }));

        stringClass.addMethod(
            new MTMethod(
                MTSymbol.intern("="),
                stringClass,
                (receiver, arguments) -> {
                    MTString self = (MTString) receiver;
                    MTString other =
                        (MTString) arguments.at(0);

                    return MTBoolean.valueOf(
                        self.getValue().equals(
                                other.getValue()));
                }));
    }

    private static void installDictionaryMethods(MTClass dictionaryClass){
        dictionaryClass.addMethod(
            new MTMethod(
                MTSymbol.intern("at:"),
                dictionaryClass,
                (receiver, arguments) -> {
                    MTDictionary self = (MTDictionary) receiver;
                    MTObject     key =  (MTObject) arguments.at(0);

                    return self.at(key);
                }));

        dictionaryClass.addMethod(
            new MTMethod(
                MTSymbol.intern("at:put:"),
                dictionaryClass,
                (receiver, arguments) -> {

                    MTDictionary self =
                            (MTDictionary) receiver;

                    MTObject key =
                            arguments.at(0);

                    MTObject value =
                            arguments.at(1);

                    self.atPut(
                            key,
                            value);

                    return value;
                }));


        dictionaryClass.addMethod(
            new MTMethod(
                MTSymbol.intern("includesKey:"),
                dictionaryClass,
                (receiver, arguments) -> {
                    MTDictionary self =
                            (MTDictionary) receiver;
                    MTObject key =
                            arguments.at(0);
                    return MTBoolean.valueOf(
                            self.includesKey(key));
                }));

        dictionaryClass.addMethod(
            new MTMethod(
                MTSymbol.intern("size"),
                dictionaryClass,
                (receiver, arguments) -> {

                    MTDictionary self = (MTDictionary) receiver;

                    return new MTInteger(self.size());
                }));

    }
}
