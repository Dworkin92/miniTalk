package mt.runtime;

public class MTReturn extends RuntimeException {

    public static final String TARGET_KEY = "__return_target__";

    private final Object target;
    private final MTObject value;

    public MTReturn(Object target, MTObject value) {
        this.target = target;
        this.value = value;
    }

    public Object target() {
        return target;
    }

    public MTObject value() {
        return value;
    }
}