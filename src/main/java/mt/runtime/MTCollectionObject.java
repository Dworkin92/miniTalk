package mt.runtime;

import mt.interpreter.MTInterpreter;
import mt.util.MTDebug;
import mt.util.MTConfig;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public abstract class MTCollectionObject implements MTObject {

    protected final Collection<MTObject> delegate;

    protected MTCollectionObject(Collection<MTObject> delegate) {
        this.delegate = delegate;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        MTDebug.enter("[COLLECTION] send:" + selector);

        try {
        return switch (selector) {

            //--------------------------------------------------
            // basique
            //--------------------------------------------------

            case "size" -> new MTInteger(delegate.size());

            case "isEmpty" -> new MTBoolean(delegate.isEmpty());

            case "remove:" -> {
                delegate.remove(args.get(0));
                yield this;
            }

            //--------------------------------------------------
            // itération
            //--------------------------------------------------

            case "do:" -> {
                MTBlockObject block = requireBlock(args, 0);


                List<MTObject> snapshot = new ArrayList<>(delegate);

                for (MTObject each : snapshot) {
                    block.send("value:", List.of(each));
                }

                yield this;
            }



            //--------------------------------------------------
            // detect
            //--------------------------------------------------

            case "detect:" -> {
                MTBlockObject block = requireBlock(args, 0);

                for (MTObject each : delegate) {
                    MTObject cond = block.call(List.of(each));

                    if (cond instanceof MTBoolean b && b.value()) {
                        yield each;
                    }
                }

                yield MTNil.INSTANCE;
            }

            //--------------------------------------------------
            // logique
            //--------------------------------------------------

            case "anySatisfy:" -> {
                MTBlockObject block = requireBlock(args, 0);

                for (MTObject each : delegate) {
                    MTObject cond = block.call(List.of(each));

                    if (cond instanceof MTBoolean b && b.value()) {
                        yield new MTBoolean(true);
                    }
                }

                yield new MTBoolean(false);
            }

            case "allSatisfy:" -> {
                MTBlockObject block = requireBlock(args, 0);

                for (MTObject each : delegate) {
                    MTObject cond = block.call(List.of(each));

                    if (!(cond instanceof MTBoolean b) || !b.value()) {
                        yield new MTBoolean(false);
                    }
                }

                yield new MTBoolean(true);
            }


            //--------------------------------------------------
            // affichage
            //--------------------------------------------------

            case "printString" -> {
                StringBuilder sb = new StringBuilder("#(");
                boolean first = true;

                for (MTObject obj : delegate) {
                    if (!first) sb.append(" ");
                    first = false;

                    MTObject ps = obj.send("printString", List.of());
                    if (ps instanceof MTString s) {
                        sb.append(s.value());
                    } else {
                        sb.append(ps.toString());
                    }
                }

                sb.append(")");
                yield new MTString(sb.toString());
            }

            default -> throw new RuntimeException("Message inconnu pour Collection: " + selector);
        };
        } finally {
            MTDebug.exit("[COLLECTION] end " + selector);
        }

    }

    //--------------------------------------------------
    // utils
    //--------------------------------------------------

    protected MTBlockObject requireBlock(List<MTObject> args, int index) {
        if (index >= args.size()) {
            throw new RuntimeException("Argument manquant " + index);
        }
        if (!(args.get(index) instanceof MTBlockObject block)) {
            throw new RuntimeException("Block attendu " + index);
        }
        return block;
    }

    protected MTObject dispatchWithFallback(String selector, List<MTObject> args) {


        // 1. essayer les méthodes Java de Collection
        MTObject result = sendFromCollection(selector, args);
        if (result != null) {
            return result;
        }

        MTClass collectionClass = (MTClass) MTInterpreter.GLOBAL.lookup("Collection");
        MTMethod method = collectionClass.lookup(selector);

        if (method != null) {
            return method.body().callWithReceiver(this, args, method.owner());
        }

        throw new RuntimeException("Message inconnu pour Collection: " + selector);
    }

    protected MTObject sendFromCollection(String selector, List<MTObject> args) {
        return switch (selector) {
            case "size" -> new MTInteger(delegate.size());
            case "isEmpty" -> new MTBoolean(delegate.isEmpty());
            case "remove:" -> {
                delegate.remove(args.get(0));
                yield this;
            }
            case "do:" -> {
                MTBlockObject block = requireBlock(args, 0);
                List<MTObject> snapshot = new ArrayList<>(delegate);
                for (MTObject each : snapshot) {
                    block.send("value:", List.of(each));
                }
                yield this;
            }
            case "printString" -> {
                StringBuilder sb = new StringBuilder("#(");
                boolean first = true;
                for (MTObject obj : delegate) {
                    if (!first) sb.append(" ");
                    first = false;
                    MTObject ps = obj.send("printString", List.of());
                    sb.append(ps);
                }
                sb.append(")");
                yield new MTString(sb.toString());
            }
            default -> null;
        };
    }

}
