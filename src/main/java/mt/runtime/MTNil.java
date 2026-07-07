package mt.runtime;

public final class MTNil
        extends MTObject {

    private static final MTNil INSTANCE =
            new MTNil();

    private MTNil() {
    }

    public static MTNil instance() {

        return INSTANCE;
    }

    @Override
    public String toString() {

        return "nil";
    }
}
