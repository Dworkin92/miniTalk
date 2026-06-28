package mt.runtime;

import mt.interpreter.MTInterpreter;
import mt.runtime.MTCollectionObject;
import java.util.ArrayList;
import java.util.List;

public final class MTListObject extends MTCollectionObject {

    public MTListObject() {
        super(new ArrayList<>());
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        return switch (selector) {

            case "add:" -> {
                delegate.add(args.get(0));
                yield this;
            }

            case "at:" -> {
                int index = ((MTInteger) args.get(0)).value() - 1;
                yield ((ArrayList<MTObject>) delegate).get(index);
            }

            default -> {
                yield dispatchWithFallback(selector, args);
            }
        };
    }
}
