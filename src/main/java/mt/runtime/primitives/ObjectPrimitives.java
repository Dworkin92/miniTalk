package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTObject;

public final class ObjectPrimitives {

    private ObjectPrimitives() {
    }

    @Primitive("class")
    public static MTObject clazz(MTObject receiver, MTArray arguments) {
        System.out.println(
    "class -> " + receiver.getClazz());

System.out.println(
    "class clazz -> " +
    receiver.getClazz().getClazz());

        return receiver.getClazz();
    }

    @Primitive("print")
    public static MTObject print(MTObject receiver, MTArray arguments) {
        System.out.print(receiver);

        return receiver;
    }

    @Primitive("println")
    public static MTObject println(MTObject receiver, MTArray arguments) {
        System.out.println(receiver);

        return receiver;
    }
}
