package mt.runtime;

public class MTNonLocalReturnException
        extends RuntimeException {

    private final MTObject value;

    public MTNonLocalReturnException(
            MTObject value) {

        this.value = value;
    }

    public MTObject getValue() {

        return value;
    }
}
