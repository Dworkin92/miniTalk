package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTObject;

public final class ObjectPrimitives {

    private ObjectPrimitives() {
    }

    @Primitive("print")
    public static MTObject print(
            MTObject receiver,
            MTArray arguments) {

        System.out.print(receiver);

        return receiver;
    }

    @Primitive("println")
    public static MTObject println(
            MTObject receiver,
            MTArray arguments) {

        System.out.println(receiver);

        return receiver;
    }
}
