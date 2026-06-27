package mt.runtime;

import java.util.HashSet;
import java.util.List;

public final class MTSetObject extends MTCollectionObject {

    public MTSetObject() {
        super(new HashSet<>());
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        return switch (selector) {

            case "add:" -> {
                delegate.add(args.get(0));
                yield this;
            }

            case "contains:" -> {
                yield new MTBoolean(delegate.contains(args.get(0)));
            }

            default -> super.send(selector, args);
        };
    }
}
