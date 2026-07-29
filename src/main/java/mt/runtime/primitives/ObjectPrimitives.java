package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTObject;
import mt.runtime.MTScope;
import mt.runtime.MTSymbol;

import mt.debug.MTDebug;

public final class ObjectPrimitives {

    private ObjectPrimitives() {
    }

    @Primitive("name")
    public static MTObject name(MTObject receiver, MTArray arguments,
            MTScope scope) {

        //MTClass clazz = (MTClass) receiver;
        MTSymbol result = receiver.getName();

        MTDebug.log("name result = " + result);
        //MTDebug.log("name result clazz = " + result.getClazz());

        return result;
    }

    @Primitive("name:")
    public static MTObject setName(MTObject receiver, MTArray arguments,
            MTScope scope) {

        //MTClass clazz = (MTClass) receiver;
        MTObject result = receiver;
        MTSymbol name = (MTSymbol)arguments.at(0);

        result.setName(name);
        //MTDebug.log("name result clazz = " + result.getClazz());

        return result;
    }

    @Primitive("class")
    public static MTObject clazz(MTObject receiver, MTArray arguments,
            MTScope scope) {
        MTDebug.log("class -> " + receiver.getClazz());

        MTDebug.log("class clazz -> " + receiver.getClazz().getClazz());

        return receiver.getClazz();
    }

    @Primitive("print")
    public static MTObject print(MTObject receiver, MTArray arguments,
            MTScope scope) {
        System.out.print(receiver);

        return receiver;
    }

    @Primitive("println")
    public static MTObject println(MTObject receiver, MTArray arguments,
            MTScope scope) {
        System.out.println(receiver);

        return receiver;
    }
}
