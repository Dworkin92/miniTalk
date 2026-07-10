package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTInteger;
import mt.runtime.MTObject;

public final class ArrayPrimitives {

    private ArrayPrimitives() {
    }

    @Primitive("size")
    public static MTObject size(
            MTObject receiver,
            MTArray arguments) {

        MTArray self =
                (MTArray) receiver;

        MTInteger result =
                new MTInteger(
                        self.size());

        result.setClazz(
                receiver.getClazz());

        return result;
    }

    @Primitive("at:")
    public static MTObject at(
            MTObject receiver,
            MTArray arguments) {

        MTArray self =
                (MTArray) receiver;

        MTInteger index =
                (MTInteger) arguments.at(0);

        return self.at(
                (int) index.getValue());
    }

    @Primitive("at:put:")
    public static MTObject atPut(
            MTObject receiver,
            MTArray arguments) {

        MTArray self =
                (MTArray) receiver;

        MTInteger index =
                (MTInteger) arguments.at(0);

        MTObject value =
                arguments.at(1);

        self.atPut(
                (int) index.getValue(),
                value);

        return value;
    }
}
