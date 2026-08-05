package mt.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class MTArray extends MTObject implements Iterable<MTObject> {
//public class MTArray extends MTObject {

    private final List<MTObject> values;

    public MTArray() {

        this.values =
                new ArrayList<>();
    }

    public MTArray(int initialSize) {

        this.values =
                new ArrayList<>(initialSize);
    }

    public void add(
            MTObject value) {

        values.add(value);
    }

    public MTObject at(
            int index) {

        return values.get(index);
    }

    public void atPut(int index, MTObject value) {
        values.set(index, value);
    }

    public int size() {

        return values.size();
    }

    @Override
    public Iterator<MTObject> iterator() {
        return values.iterator();
    }

    @Override
    public String toString() {

        return values.toString();
    }
}
