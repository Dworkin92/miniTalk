package mt.runtime;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import mt.exceptions.MTRuntimeException;

import mt.debug.MTDebug;

public class MTClass
        extends MTObject {

    private final MTSymbol name;

    private MTClass superclass;

    private MTMetaclass metaclazz;

    private final Map<MTSymbol, MTProperty>
            declaredProperties =
                new LinkedHashMap<>();

    private final Map<MTSymbol, MTProperty>
            allProperties =
                new LinkedHashMap<>();

    private final Map<MTSymbol, MTMethod>
            methods =
                new LinkedHashMap<>();

    public MTClass(MTSymbol name) {

        this.name = name;
    }

    public MTSymbol getName() {
        return name;
    }

    public MTClass getSuperclass() {
        return superclass;
    }

    public void setSuperclass(
            MTClass superclass) {

        this.superclass = superclass;
    }

    public MTMetaclass getMetaclazz() {
        return metaclazz;
    }

    public void setMetaclazz(
            MTMetaclass metaclazz) {

        this.metaclazz = metaclazz;
    }

    public void addProperty(
            MTSymbol symbol) {

        if (allProperties.containsKey(symbol)) {

            throw new IllegalStateException(
                    "Property already exists: "
                            + symbol);
        }

        MTProperty property =
                new MTProperty(
                        symbol,
                        this);

        declaredProperties.put(
                symbol,
                property);

        recomputeAllProperties();
    }

    public void addMethod(
            MTMethod method) {

        methods.put(
                method.getSelector(),
                method);
    }

    private void recomputeAllProperties() {

        allProperties.clear();

        if (superclass != null) {

            allProperties.putAll(
                    superclass.allProperties);
        }

        allProperties.putAll(
                declaredProperties);
    }

    public Map<MTSymbol, MTProperty>
    getAllProperties() {

        return allProperties;
    }

    public MTObject newInstance() {

        MTObject object =
                new MTObject();

        object.setClazz(this);

        object.rebindProps();

        return object;
    }

    public MTMethod lookupMethod(MTSymbol selector) {
        return lookupMethod(selector, new HashSet<>());
    }

    private MTMethod lookupMethod(MTSymbol selector, Set<MTClass> visited) {
        if (!visited.add(this)) {

            throw new MTRuntimeException("Cyclic superclass hierarchy detected while looking up "
               + selector + " from " + getName());
        }

        MTMethod method = methods.get(selector);

        if (method != null) {
            return method;
        }

        if (superclass != null) {
            return superclass.lookupMethod(selector, visited);
        }

        return null;
    }

}
