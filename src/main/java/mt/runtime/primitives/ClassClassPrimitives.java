package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTClass;
import mt.runtime.MTMetaclass;
import mt.runtime.MTObject;
import mt.runtime.MTNil;
import mt.runtime.MTSymbol;
import mt.runtime.MTScope;
import mt.runtime.MTRuntime;
import mt.debug.MTDebug;

public final class ClassClassPrimitives {

    private ClassClassPrimitives() {
    }

    @Primitive("new")
    public static MTObject newClass(MTObject receiver, MTArray arguments, MTScope scope) {

        MTRuntime runtime = scope.getRuntime();

        MTClass clazz = new MTClass(MTSymbol.intern("AnonymousClass"));

        MTMetaclass metaclazz = new MTMetaclass(MTSymbol.intern("AnonymousClassClass"));

        clazz.setClazz((MTClass) receiver);
        clazz.setMetaclazz(metaclazz);

        metaclazz.setClazz(runtime.getClassMetaclass());

        metaclazz.setSuperclass(runtime.getObjectMetaclass());

        clazz.setSuperclass(runtime.getObjectClass());

        return clazz;
    }
}
