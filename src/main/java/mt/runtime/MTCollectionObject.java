package mt.runtime;

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

                for (MTObject each : delegate) {
                    block.call(List.of(each));
                }

                yield this;
            }

            //--------------------------------------------------
            // collect / map
            //--------------------------------------------------

            case "collect:", "map:" -> {
                MTBlockObject block = requireBlock(args, 0);
                List<MTObject> result = new ArrayList<>();

                for (MTObject each : delegate) {
                    result.add(block.call(List.of(each)));
                }

                yield new MTArray(result);
            }

            //--------------------------------------------------
            // select / filter
            //--------------------------------------------------

            case "select:", "filter:" -> {
                MTBlockObject block = requireBlock(args, 0);
                List<MTObject> result = new ArrayList<>();

                for (MTObject each : delegate) {
                    MTObject cond = block.call(List.of(each));

                    if (!(cond instanceof MTBoolean b)) {
                        throw new RuntimeException("select: doit retourner Boolean");
                    }

                    if (b.value()) {
                        result.add(each);
                    }
                }

                yield new MTArray(result);
            }

            //--------------------------------------------------
            // reject
            //--------------------------------------------------

            case "reject:" -> {
                MTBlockObject block = requireBlock(args, 0);
                List<MTObject> result = new ArrayList<>();

                for (MTObject each : delegate) {
                    MTObject cond = block.call(List.of(each));

                    if (!(cond instanceof MTBoolean b)) {
                        throw new RuntimeException("reject: doit retourner Boolean");
                    }

                    if (!b.value()) {
                        result.add(each);
                    }
                }

                yield new MTArray(result);
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
            // reduce
            //--------------------------------------------------

            case "inject:into:", "reduce:with:" -> {
                MTObject acc = args.get(0);
                MTBlockObject block = requireBlock(args, 1);

                for (MTObject each : delegate) {
                    acc = block.call(List.of(acc, each));
                }

                yield acc;
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
}
