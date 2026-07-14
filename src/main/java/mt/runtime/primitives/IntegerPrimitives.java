package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTBoolean;
import mt.runtime.MTInteger;
import mt.runtime.MTObject;
import mt.runtime.MTBlock;
import mt.runtime.MTNil;

public final class IntegerPrimitives {

    private IntegerPrimitives() {
    }

    @Primitive("+")
    public static MTObject plus(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.add(right);
    }

    @Primitive("-")
    public static MTObject minus(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.subtract(right);
    }

    @Primitive("*")
    public static MTObject multiply(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.multiply(right);
    }

    @Primitive("/")
    public static MTObject divide(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.divide(right);
    }

    @Primitive("%")
    public static MTObject modulo(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.modulo(right);
    }

    @Primitive("=")
    @Primitive("==")
    public static MTObject equalsTo(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.equalsTo(right);
    }

    @Primitive("<")
    public static MTObject lessThan(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.lessThan(right);
    }

    @Primitive(">")
    public static MTObject greaterThan(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.greaterThan(right);
    }

    @Primitive("~<")
    @Primitive("<=")
    public static MTObject lessOrEqual(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.lessOrEqual(right);
    }

    @Primitive(">~")
    @Primitive(">=")
    public static MTObject greaterOrEqual(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.greaterOrEqual(right);
    }

    @Primitive("<>")
    @Primitive("!=")
    public static MTObject differentFrom(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.differentFrom(right);
    }

    @Primitive("to:do:")
    public static MTObject toDo(MTObject receiver, MTArray arguments) {

        MTInteger start = (MTInteger) receiver;

        MTInteger end = (MTInteger) arguments.at(0);

        MTBlock block = (MTBlock) arguments.at(1);

        long first = start.getValue();

        long last = end.getValue();

        long step = first <= last ? 1 : -1;

        MTObject result = MTNil.instance();

        for (long i = first;
             step > 0 ? i <= last : i >= last;
             i += step) {

            MTInteger current = new MTInteger(i);
            current.setClazz(start.getClazz());
            MTArray blockArguments = new MTArray();
            blockArguments.add(current);
            result = block.value(blockArguments);
        }

        return result;
    }
}
