package mt.runtime;

import mt.exceptions.MTRuntimeException;

public class MTScope extends MTObject {

    private final MTScope parent;

    private final MTRuntime runtime;

    private final MTDictionary bindings;

    private MTBlock homeBlock;

    public MTScope(
            MTRuntime runtime,
            MTScope parent) {

        this.parent = parent;

        this.runtime = runtime;

        this.bindings = new MTDictionary();
    }

    public MTScope parent() {

        return parent;
    }

    public MTRuntime getRuntime() {
        return runtime;
    }

    public MTRuntime runtime() {
        return runtime;
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

        throw new MTRuntimeException(
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

        throw new MTRuntimeException(
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

    public MTBlock getHomeBlock() {
        return homeBlock;
    }

    public void setHomeBlock(MTBlock homeBlock) {
        this.homeBlock = homeBlock;
    }
}
