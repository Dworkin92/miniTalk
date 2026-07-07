package mt.runtime;

public class MTProperty
        extends MTObject {

    private final MTSymbol name;

    private final MTClass ownerClass;

    private MTObject defaultValue =
            MTNil.instance();

    public MTProperty(
            MTSymbol name,
            MTClass ownerClass) {

        this.name = name;
        this.ownerClass = ownerClass;
    }

    public MTSymbol getName() {
        return name;
    }

    public MTClass getOwnerClass() {
        return ownerClass;
    }

    public MTObject getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(
            MTObject defaultValue) {

        this.defaultValue = defaultValue;
    }
}
