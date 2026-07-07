package mt.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

public class MTDictionary
        extends MTObject {

    private final Map<MTObject, MTObject>
            entries =
                new LinkedHashMap<>();

    public MTObject at(
            MTObject key) {

        return entries.getOrDefault(
                key,
                MTNil.instance());
    }

    public void atPut(
            MTObject key,
            MTObject value) {

        entries.put(
                key,
                value);
    }

    public boolean includesKey(
            MTObject key) {

        return entries.containsKey(
                key);
    }

    public int size() {

        return entries.size();
    }

    @Override
    public String toString() {

        return entries.toString();
    }
}
