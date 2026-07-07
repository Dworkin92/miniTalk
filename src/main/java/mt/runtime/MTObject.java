package mt.runtime;

import java.util.HashMap;
import java.util.Map;

public class MTObject {

    private static long NEXT_ID = 1;

    private final long objectId;

    private MTClass clazz;

    private final Map<MTSymbol, MTObject> propertyValues =
            new HashMap<>();

    public MTObject() {
        this.objectId = NEXT_ID++;
    }

    public long getObjectId() {
        return objectId;
    }


    public MTObject send(MTSymbol selector) {
        return send(
            selector,
            new MTArray());
    }

    public MTObject send(MTSymbol selector,MTArray arguments) {
        MTMethod method = clazz.lookupMethod(selector);

        if (method == null) {
            throw new RuntimeException(
                "Unknown selector: " + selector);
        }

        return method.invoke(this, arguments);
    }

    public MTClass getClazz() {
        return clazz;
    }

    public void setClazz(MTClass clazz) {
        this.clazz = clazz;
    }

    public MTObject getProperty(MTSymbol symbol) {

        MTObject value =
                propertyValues.get(symbol);

        return value != null
                ? value
                : MTNil.instance();
    }

    public void setProperty(
            MTSymbol symbol,
            MTObject value) {

        propertyValues.put(symbol, value);
    }

    public void rebindProps() {

        if (clazz == null) {
            return;
        }

        for (MTProperty property :
                clazz.getAllProperties().values()) {

            propertyValues.putIfAbsent(
                    property.getName(),
                    MTNil.instance());
        }
    }


    /*
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    */

    /* methode temporaire : propertyCount */
    public int propertyCount() {
        return propertyValues.size();
    }

}
