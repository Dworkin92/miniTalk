package mt.runtime;

public final class MTInteger
        extends MTObject {

    private final long value;

    public MTInteger(long value) {

        this.value = value;
    }

    public long getValue() {

        return value;
    }

    public MTInteger add(
            MTInteger other) {

        return new MTInteger(
                value + other.value);
    }

    public MTInteger subtract(
            MTInteger other) {

        return new MTInteger(
                value - other.value);
    }

    public MTInteger multiply(
            MTInteger other) {

        return new MTInteger(
                value * other.value);
    }

    public MTInteger divide(
            MTInteger other) {

        return new MTInteger(
                value / other.value);
    }

    public MTBoolean equalsTo(
            MTInteger other) {

        return MTBoolean.valueOf(
                value == other.value);
    }

    public MTBoolean lessThan(
            MTInteger other) {

        return MTBoolean.valueOf(
                value < other.value);
    }

    public MTBoolean greaterThan(
            MTInteger other) {

        return MTBoolean.valueOf(
                value > other.value);
    }

    public MTBoolean lessOrEqual(
            MTInteger other) {

        return MTBoolean.valueOf(
                value <= other.value);
    }

    public MTBoolean greaterOrEqual(
            MTInteger other) {

        return MTBoolean.valueOf(
                value >= other.value);
    }

    @Override
    public String toString() {

        return Long.toString(value);
    }
}
