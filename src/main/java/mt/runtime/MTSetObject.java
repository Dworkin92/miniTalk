package mt.runtime;

import java.util.HashSet;
import java.util.List;

public final class MTSetObject extends MTCollectionObject {

    public MTSetObject() {
        super(new HashSet<>());
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        switch (selector) {

            case "add:" -> {
                delegate.add(unwrap(args.get(0)));
                return this;
            }

            case "contains:" -> {
                Object value = unwrap(args.get(0));
                return new MTBoolean(delegate.contains(value));
            }
        }

        return super.send(selector, args);
    }
}
