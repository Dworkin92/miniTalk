package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTBoolean;
import mt.runtime.MTObject;

public final class BooleanPrimitives {

    private BooleanPrimitives() {
    }

    @Primitive("not")
    public static MTObject not(
            MTObject receiver,
            MTArray arguments) {

        MTBoolean self =
                (MTBoolean) receiver;

        return MTBoolean.valueOf(
                !self.getValue());
    }

    @Primitive("and:")
    public static MTObject and(
            MTObject receiver,
            MTArray arguments) {

        MTBoolean self =
                (MTBoolean) receiver;

        MTBoolean other =
                (MTBoolean) arguments.at(0);

        return MTBoolean.valueOf(
                self.getValue()
                && other.getValue());
    }

    @Primitive("or:")
    public static MTObject or(
            MTObject receiver,
            MTArray arguments) {

        MTBoolean self =
                (MTBoolean) receiver;

        MTBoolean other =
                (MTBoolean) arguments.at(0);

        return MTBoolean.valueOf(
                self.getValue()
                || other.getValue());
    }

    @Primitive("xor:")
    public static MTObject xor(
            MTObject receiver,
            MTArray arguments) {

        MTBoolean self =
                (MTBoolean) receiver;

        MTBoolean other =
                (MTBoolean) arguments.at(0);

        return MTBoolean.valueOf(
                self.getValue()
                ^ other.getValue());
    }
}
