package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTClass;
import mt.runtime.MTObject;
import mt.runtime.MTNil;
import mt.runtime.MTSymbol;
import mt.debug.MTDebug;

public final class ClassPrimitives {

    private ClassPrimitives() {
    }

    @Primitive("name")
    public static MTObject name(
            MTObject receiver,
            MTArray arguments) {

        MTClass clazz = (MTClass) receiver;

        MTSymbol result = clazz.getName();

        MTDebug.log("name result = " + result);
        MTDebug.log("name result clazz = " + result.getClazz());

return result;
        //return clazz.getName();
    }

    @Primitive("superclass")
    public static MTObject superclass(
            MTObject receiver,
            MTArray arguments) {

        MTClass clazz = (MTClass) receiver;

        MTClass superclass = clazz.getSuperclass();

        return superclass != null
                ? superclass
                : MTNil.instance();
    }

    @Primitive("new")
    public static MTObject newInstance(
            MTObject receiver,
            MTArray arguments) {

        MTClass clazz = (MTClass) receiver;

        return clazz.newInstance();
    }
}
