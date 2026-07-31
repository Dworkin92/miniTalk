package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTClass;
import mt.runtime.MTMetaclass;
import mt.runtime.MTObject;
import mt.runtime.MTNil;
import mt.runtime.MTSymbol;
import mt.runtime.MTScope;
import mt.debug.MTDebug;

public final class ClassPrimitives {

    private ClassPrimitives() {
    }

    /*
    @Primitive("name")
    public static MTObject name(
            MTObject receiver,
            MTArray arguments) {

        MTClass clazz = (MTClass) receiver;

        MTSymbol result = clazz.getName();

        MTDebug.log("name result = " + result);
        MTDebug.log("name result clazz = " + result.getClazz());

        return result;
    }
    */

    @Primitive("superclass")
    public static MTObject superclass(
            MTObject receiver,
            MTArray arguments,
            MTScope scope) {

        MTClass clazz = (MTClass) receiver;

        MTClass superclass = clazz.getSuperclass();

        return superclass != null
                ? superclass
                : MTNil.instance();
    }

    @Primitive("superclass:")
    public static MTObject setSuperclass(MTObject receiver, MTArray arguments, MTScope scope) {
        MTClass clazz = (MTClass) receiver;
        MTClass superclass = (MTClass) arguments.at(0);

        clazz.setSuperclass(superclass);

        if (clazz.getMetaclazz() != null) {
            clazz.getMetaclazz().setSuperclass(superclass.getMetaclazz());
        }

        return superclass;
    }

    @Primitive("metaclass")
    public static MTObject metaclass(
            MTObject receiver,
            MTArray arguments,
            MTScope scope) {

        MTClass clazz = (MTClass) receiver;

        MTMetaclass metaclass = clazz.getMetaclazz();

        return metaclass != null
                ? metaclass
                : MTNil.instance();
    }

}
