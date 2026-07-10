package mt.runtime;

public class MTScope
        extends MTObject {

    private final MTScope parent;

    private final MTDictionary bindings;

    public MTScope(
            MTScope parent) {

        this.parent = parent;

        this.bindings =
                new MTDictionary();
    }

    public MTScope parent() {

        return parent;
    }

    public void define(
            MTSymbol name,
            MTObject value) {

        bindings.atPut(
                name,
                value);
    }

    public MTObject lookup(
            MTSymbol name) {

        if (bindings.includesKey(
                name)) {

            return bindings.at(
                    name);
        }

        if (parent != null) {

            return parent.lookup(
                    name);
        }

        throw new RuntimeException(
                "Unknown variable: "
                        + name);
    }

    public void assign(
            MTSymbol name,
            MTObject value) {

        if (bindings.includesKey(
                name)) {

            bindings.atPut(
                    name,
                    value);

            return;
        }

        if (parent != null) {

            parent.assign(
                    name,
                    value);

            return;
        }

        throw new RuntimeException(
                "Unknown variable: "
                        + name);
    }

    public boolean hasLocal(
            MTSymbol name) {

        return bindings.includesKey(
                name);
    }

    public MTDictionary getBindings() {

        return bindings;
    }
}
