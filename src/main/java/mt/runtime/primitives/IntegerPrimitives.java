package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTBoolean;
import mt.runtime.MTInteger;
import mt.runtime.MTObject;

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

    @Primitive("=")
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
    public static MTObject greaterOrEqual(
            MTObject receiver,
            MTArray arguments) {

        MTInteger left =
                (MTInteger) receiver;

        MTInteger right =
                (MTInteger) arguments.at(0);

        return left.greaterOrEqual(right);
    }
}
