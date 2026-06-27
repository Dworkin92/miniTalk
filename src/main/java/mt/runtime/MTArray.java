package mt.runtime;

import java.util.ArrayList;import mt.interpreter.MTInterpreter;
import java.util.List;

public final class MTArray extends MTCollectionObject {

    private final List<MTObject> values;

    public MTArray(List<MTObject> values) {
        super(new ArrayList<>(values));
        this.values = (List<MTObject>) delegate;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        return switch (selector) {

            case "=" -> {
                if (!(args.get(0) instanceof MTArray other)) {
                    yield new MTBoolean(false);
                }

                if (values.size() != other.values.size()) {
                    yield new MTBoolean(false);
                }

                for (int i = 0; i < values.size(); i++) {
                    MTObject eq = values.get(i).send("=", List.of(other.values.get(i)));

                    if (!(eq instanceof MTBoolean b) || !b.value()) {
                        yield new MTBoolean(false);
                    }
                }

                yield new MTBoolean(true);
            }

            case "!=" -> {
                MTBoolean eq = (MTBoolean) send("=", args);
                yield new MTBoolean(!eq.value());
            }

            case "at:" -> {
                int index = ((MTInteger) args.get(0)).value() - 1;
                yield values.get(index);
            }

            case "at:put:" -> {
                int index = ((MTInteger) args.get(0)).value() - 1;
                MTObject value = args.get(1);
                values.set(index, value);
                yield value;
            }

            case "add:" -> {
                values.add(args.get(0));
                yield this;
            }

            // tout le reste est factorisé
            default -> {
                yield super.send(selector, args);
            }
        };
    }
}
