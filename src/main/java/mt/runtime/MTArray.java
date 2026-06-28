package mt.runtime;

import mt.util.MTDebug;
import mt.util.MTConfig;
import java.util.ArrayList;
import mt.interpreter.MTInterpreter;
import java.util.List;

public final class MTArray extends MTCollectionObject {

    private final List<MTObject> values;

    public MTArray(List<MTObject> values) {
        super(new ArrayList<>(values));
        this.values = (List<MTObject>) delegate;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        if (MTConfig.DEBUG) {
            if (selector.equals("do:"))
                System.out.println("[ARRAY] DO: RECEIVED");
            else
                System.out.println("[ARRAY] send: " + selector);
        }


if (MTConfig.DEBUG) {
    System.out.println("[ARRAY] selector=" + selector);
}

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


            case "class" -> {
                MTClass arrayClass = (MTClass) MTInterpreter.GLOBAL.lookup("Array");
                yield arrayClass;
            }

            // tout le reste est factorisé
            default -> {

                // 1. fallback Java → CollectionObject
                MTObject result;
                try {
                    result = super.send(selector, args);
                    yield result;
                } catch (RuntimeException ignored) {
                // continuer
                }

                // 2. fallback miniTalk -> Array
                MTClass arrayClass = (MTClass) MTInterpreter.GLOBAL.lookup("Array");
                MTMethod method = arrayClass.lookup(selector);

                if (method != null) {
                    yield method.body().callWithReceiver(this, args, method.owner());
                }

                // 3. fallback vers Collection
                MTClass collectionClass = (MTClass) MTInterpreter.GLOBAL.lookup("Collection");
                MTMethod collMethod = collectionClass.lookup(selector);
                if (collMethod != null) {
                    yield collMethod.body().callWithReceiver(this, args, collMethod.owner());
                }

                throw new RuntimeException("Message inconnu pour Array: " + selector);
            }
        };
    }
}
