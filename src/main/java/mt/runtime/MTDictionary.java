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

    public void atPutIfAbsent(MTObject key, MTObject value) {

        if (!includesKey(key)) {
            atPut(key, value);
        }
    }
    public boolean includesKey(
            MTObject key) {

        return entries.containsKey(
                key);
    }

    public int size() {

        return entries.size();
    }

    public MTArray keys() {

        MTArray result = new MTArray();

        for (MTObject key : entries.keySet()) {
            result.add(key);
        }

        return result;
    }

    public MTArray values() {

        MTArray result = new MTArray();

        for (MTObject value : entries.values()) {
            result.add(value);
        }

        return result;
    }

    @Override
    public String toString() {

        return entries.toString();
    }
}
