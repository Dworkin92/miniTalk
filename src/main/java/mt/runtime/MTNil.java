package mt.runtime;

import mt.runtime.MTObject;
import mt.runtime.MTClass;

public final class MTNil
        extends MTObject {

    private static final MTNil INSTANCE =
            new MTNil();

    private MTNil() {
    }

    public static MTNil instance() {
        return INSTANCE;
    }

    public static void setNilClass(MTClass clazz) {
        instance().setClazz(clazz);
    }

    @Override
    public String toString() {

        return "nil";
    }
}
