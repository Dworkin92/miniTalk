package mt.runtime;

public class MTString
        extends MTObject {

    private final String value;

    public MTString(String value) {

        this.value = value;
    }

    public String getValue() {

        return value;
    }

    @Override
    public String toString() {

        return value;
    }
}
