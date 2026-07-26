package mt.runtime;

public class MTString
        extends MTObject {

    private static MTClass stringClass = null;

    private final String value;

    public MTString(String value) {

        this.value = value;
        if (stringClass != null) {
            setClazz(stringClass);
        }
    }

    public static void setStringClass(MTClass clazz) {
        stringClass = clazz;
    }


    public String getValue() {

        return value;
    }

    @Override
    public String toString() {

        return value;
    }
}
