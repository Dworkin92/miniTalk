package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTBlock;
import mt.runtime.MTObject;
import mt.runtime.MTBoolean;
import mt.runtime.MTNil;

public final class BlockPrimitives {

    private BlockPrimitives() {
    }

    @Primitive("value")
    public static MTObject value(
            MTObject receiver,
            MTArray arguments) {

        MTBlock self =
                (MTBlock) receiver;

        return self.value(
                new MTArray());
    }

    @Primitive("value:")
    public static MTObject value1(
            MTObject receiver,
            MTArray arguments) {

        MTBlock self =
                (MTBlock) receiver;

        return self.value(arguments);
    }

    @Primitive("value:value:")
    public static MTObject value2(
            MTObject receiver,
            MTArray arguments) {

        MTBlock self =
                (MTBlock) receiver;

        return self.value(arguments);
    }

    @Primitive("value:value:value:")
    public static MTObject value3(
            MTObject receiver,
            MTArray arguments) {

        MTBlock self =
                (MTBlock) receiver;

        return self.value(arguments);
    }

    @Primitive("valueArray:")
    public static MTObject valueArray(
            MTObject receiver,
            MTArray arguments) {

        MTBlock self =
                (MTBlock) receiver;

        MTArray actualArguments =
                (MTArray) arguments.at(0);

        return self.value(
                actualArguments);
    }

    @Primitive("whileTrue:")
    public static MTObject whileTrue(
        MTObject receiver,
        MTArray arguments) {

        MTBlock condition = (MTBlock) receiver;

        MTBlock body = (MTBlock) arguments.at(0);

        MTObject result = MTNil.instance();

        while (((MTBoolean)condition.value()).getValue()) {
            result = body.value();
        }

        return result;
    }

    @Primitive("whileFalse:")
    public static MTObject whileFalse(
        MTObject receiver,
        MTArray arguments) {

        MTBlock condition = (MTBlock) receiver;

        MTBlock body = (MTBlock) arguments.at(0);

        MTObject result = MTNil.instance();

        while (!((MTBoolean)condition.value()).getValue()) {
            result = body.value();
        }
        return result;
    }
}
