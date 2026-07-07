package mt.runtime;

public final class MTBoolean
        extends MTObject {

    public static final MTBoolean TRUE =
            new MTBoolean(true);

    public static final MTBoolean FALSE =
            new MTBoolean(false);

    private final boolean value;

    private MTBoolean(boolean value) {

        this.value = value;
    }

    public static MTBoolean valueOf(
            boolean value) {

        return value
                ? TRUE
                : FALSE;
    }

    public boolean getValue() {

        return value;
    }

    @Override
    public String toString() {

        return Boolean.toString(value);
    }
}
