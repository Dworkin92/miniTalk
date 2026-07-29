package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTBoolean;
import mt.runtime.MTObject;
import mt.runtime.MTBlock;
import mt.runtime.MTScope;
import mt.runtime.MTNil;

public final class BooleanPrimitives {

    private BooleanPrimitives() {
    }

    @Primitive("not")
    public static MTObject not(
            MTObject receiver,
            MTArray arguments,
            MTScope scope) {

        MTBoolean self =
                (MTBoolean) receiver;

        return MTBoolean.valueOf(
                !self.getValue());
    }

    @Primitive("and:")
    public static MTObject and(
            MTObject receiver,
            MTArray arguments,
            MTScope scope) {

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
            MTArray arguments,
            MTScope scope) {

        MTBoolean self =
                (MTBoolean) receiver;

        MTBoolean other =
                (MTBoolean) arguments.at(0);

        return MTBoolean.valueOf(
                self.getValue()
                || other.getValue());
    }

    @Primitive("xor:")
    public static MTObject xor(MTObject receiver, MTArray arguments, MTScope scope) {

        MTBoolean self = (MTBoolean) receiver;

        MTBoolean other = (MTBoolean) arguments.at(0);

        return MTBoolean.valueOf(
                self.getValue()
                ^ other.getValue());
    }

    @Primitive("ifTrue:")
    public static MTObject ifTrue(
        MTObject receiver,
        MTArray arguments,
        MTScope scope) {

        MTBoolean self = (MTBoolean) receiver;

        MTBlock block = (MTBlock) arguments.at(0);

        if (self.getValue()) {
            return block.value();
        }

        return MTNil.instance();
    }

    @Primitive("ifFalse:")
    public static MTObject ifFalse(
        MTObject receiver,
        MTArray arguments,
        MTScope scope) {

        MTBoolean self = (MTBoolean) receiver;

        MTBlock block = (MTBlock) arguments.at(0);

        if (!self.getValue()) {
            return block.value();
        }

        return MTNil.instance();
    }

    @Primitive("ifTrue:ifFalse:")
    public static MTObject ifTrueIfFalse(
        MTObject receiver,
        MTArray arguments,
        MTScope scope) {

        MTBoolean self = (MTBoolean) receiver;

        MTBlock trueBlock = (MTBlock) arguments.at(0);

        MTBlock falseBlock = (MTBlock) arguments.at(1);

        if (self.getValue()) {

            return trueBlock.value();
        }

        return falseBlock.value();
    }


}
