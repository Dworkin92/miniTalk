package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTBoolean;
import mt.runtime.MTInteger;
import mt.runtime.MTObject;
import mt.runtime.MTString;

public final class StringPrimitives {

    private StringPrimitives() {
    }

    @Primitive("size")
    public static MTObject size(
            MTObject receiver,
            MTArray arguments) {

        MTString self =
                (MTString) receiver;

        MTInteger result =
            new MTInteger(
                self.getValue().length());

        result.setClazz(receiver.getClazz());

        return result;
    }

    @Primitive("+")
    public static MTObject plus(
            MTObject receiver,
            MTArray arguments) {

        MTString self =
                (MTString) receiver;

        MTString other =
                (MTString) arguments.at(0);

        MTString result =
            new MTString(
                self.getValue()
                + other.getValue());

        result.setClazz(receiver.getClazz());
    }

    @Primitive("=")
    public static MTObject equalsTo(
            MTObject receiver,
            MTArray arguments) {

        MTString self =
                (MTString) receiver;

        MTString other =
                (MTString) arguments.at(0);

        return MTBoolean.valueOf(
                self.getValue().equals(
                        other.getValue()));
    }
}
