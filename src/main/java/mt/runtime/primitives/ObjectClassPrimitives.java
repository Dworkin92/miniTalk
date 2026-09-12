package mt.runtime.primitives;

import mt.runtime.MTObject;
import mt.runtime.MTArray;
import mt.runtime.MTScope;
import mt.runtime.MTClass;
import mt.runtime.MTNil;
import mt.runtime.MTSymbol;
import mt.runtime.MTRuntime;
import mt.runtime.MTMethod;
import mt.runtime.MTBlock;
import mt.debug.MTDebug;

public final class ObjectClassPrimitives {

    private ObjectClassPrimitives() {
    }

    @Primitive("new")
    public static MTObject newInstance(
            MTObject receiver,
            MTArray arguments,
            MTScope scope) {

        MTClass clazz =
                (MTClass) receiver;

        MTObject instance =
                new MTObject();

        instance.setClazz(clazz);

        instance.rebindProps();

        return instance;
    }

    @Primitive("addInstProperty:")
    @Primitive("addInstProp:")
    public static MTObject addInstProp(
        MTObject receiver,
        MTArray arguments,
        MTScope scope) {

        //System.out.println(">>> addInstProperty called");

        MTClass clazz = (MTClass) receiver;

        MTSymbol propertyName = (MTSymbol) arguments.at(0);

        clazz.addProperty(propertyName);

        /*
        * getter
        */
        MTDebug.log("[addInstProp] add getter " + propertyName.toString() + " to class " + clazz.getName().toString() );
        clazz.addMethod(
            new MTMethod(
                    propertyName,
                    clazz,
                    (self, args, activationScope) ->
                            self.getProperty(propertyName)));
        System.out.println(
"lookup after add = "
+ clazz.lookupMethod(
MTSymbol.intern(propertyName + ":")));

        /*
        * setter
        */
        MTDebug.log("[addInstProp] add setter "+ propertyName.toString() + ": to class " +  clazz.getName().toString() );
        clazz.addMethod(
            new MTMethod(
                    MTSymbol.intern(propertyName.getValue() + ":"),
                    clazz,
                    (self, args, activationScope) -> {
                        MTObject value = args.at(0);
                        self.setProperty(propertyName, value);
                        return value;}));


        return receiver;
    }

    @Primitive("addClassProperty:")
    @Primitive("addClassProp:")
    public static MTObject addClassProp(
        MTObject receiver,
        MTArray arguments,
        MTScope scope) {

        MTClass clazz = (MTClass) receiver;

        MTClass metaclass = clazz.getMetaclazz();

        MTSymbol propertyName = (MTSymbol) arguments.at(0);

        metaclass.addProperty(propertyName);

        /*
        * getter
        */
        metaclass.addMethod(
            new MTMethod(
                    propertyName,
                    metaclass,
                    (self, args, activationScope) -> self.getProperty(propertyName)));

        /*
        * setter
        */
        metaclass.addMethod(
            new MTMethod(
                    MTSymbol.intern(propertyName + ":"),
                    metaclass,
                    (self, args, activationScope) -> {
                        MTObject value = args.at(0);
                        self.setProperty(propertyName, value);
                        return value;}));
        return receiver;
    }

    @Primitive("addInstMethod:body:")
    @Primitive("addInstMeth:body:")
    public static MTObject addInstMeth(
        MTObject receiver,
        MTArray arguments,
        MTScope scope) {

        MTClass clazz = (MTClass) receiver;

        MTSymbol selector = (MTSymbol) arguments.at(0);

        MTBlock block = (MTBlock) arguments.at(1);

        clazz.addMethod(
            new MTMethod( selector, clazz,
                    (self, methodArguments, activationScope) ->
                            block.value(self, methodArguments)));

        return receiver;
    }

    @Primitive("addClassMethod:body:")
    @Primitive("addClassMeth:body:")
    public static MTObject addClassMeth(
        MTObject receiver,
        MTArray arguments,
        MTScope scope) {

        MTClass clazz = (MTClass) receiver;

        MTClass metaclass = clazz.getMetaclazz();

        MTSymbol selector = (MTSymbol) arguments.at(0);

        MTBlock block = (MTBlock) arguments.at(1);

        metaclass.addMethod(
            new MTMethod(selector, metaclass,
                    (self, methodArguments, activationScope) ->
                            block.value(self, methodArguments)));

        return receiver;
    }
}
