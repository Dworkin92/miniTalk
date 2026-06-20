package mt.interpreter;

import mt.runtime.MTObject;

import java.util.HashMap;
import java.util.Map;

public class MTEnvironment {

    private final Map<String, MTObject> values = new HashMap<>();
    private final MTEnvironment parent;

    public MTEnvironment() {
        this.parent = null;
    }

    public MTEnvironment(MTEnvironment parent) {
        this.parent = parent;
    }

    public void define(String name, MTObject value) {
        values.put(name, value);
    }

    public MTObject lookup(String name) {
        MTObject value = values.get(name);
        if (value != null) return value;
        if (parent != null) return parent.lookup(name);
        throw new RuntimeException("Variable inconnue: " + name);
    }

    public boolean containsLocal(String name) {
        return values.containsKey(name);
    }

    public void set(String name, MTObject value) {
        if (values.containsKey(name)) {
            values.put(name, value);
            return;
        }
        if (parent != null) {
            parent.set(name, value);
            return;
        }
        // si la variable n'existait nulle part, on la crée localement
        values.put(name, value);
    }
}
