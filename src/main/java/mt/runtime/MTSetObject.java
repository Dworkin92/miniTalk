package mt.runtime;

import mt.interpreter.MTInterpreter;
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

            default -> {
                try {
                    yield super.send(selector, args);
                } catch (RuntimeException ignored) {
                    // continuer
                }

                // 3. fallback vers Collection
                MTClass collectionClass = (MTClass) MTInterpreter.GLOBAL.lookup("Collection");
                MTMethod method = collectionClass.lookup(selector);

                if (method != null) {
                    yield method.body().callWithReceiver(this, args, method.owner());
                }

                throw new RuntimeException("Message inconnu pour Set: " + selector);
            }
        };
    }
}
