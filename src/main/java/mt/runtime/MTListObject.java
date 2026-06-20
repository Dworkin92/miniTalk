package mt.runtime;

import java.util.ArrayList;
import java.util.List;

public final class MTListObject extends MTCollectionObject {

    public MTListObject() {
        super(new ArrayList<>());
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        switch (selector) {

            case "add:" -> {
                delegate.add(unwrap(args.get(0)));
                return this;
            }

            case "at:" -> {
                int index = ((MTInteger) args.get(0)).value();
                return wrap(((ArrayList<Object>) delegate).get(index));
            }
        }

        return super.send(selector, args);
    }

    private MTObject wrap(Object value) {
        if (value instanceof Integer i) return new MTInteger(i);
        if (value instanceof String s) return new MTString(s);
        if (value instanceof Boolean b) return new MTBoolean(b);
        return (MTObject) value;
    }
}
