package mt.runtime;

public class MTProperty
        extends MTObject {

    private final MTSymbol name;

    private final MTClass ownerClass;

    private MTObject value =
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

    public MTObject getValue() {
        return value;
    }

    public void setValue(
            MTObject value) {

        this.value = value;
    }
}
