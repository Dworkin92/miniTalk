package mt.runtime;

import mt.interpreter.MTInterpreter;
import java.util.ArrayList;
import java.util.List;


public final class MTArray implements MTObject {

    private final List<MTObject> values;
    MTClass arrayClass = (MTClass) MTInterpreter.GLOBAL.lookup("Array");

    public MTArray(List<MTObject> values) {
        this.values = new ArrayList<>(values);
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {

            case "size" -> new MTInteger(values.size());

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

            case "do:" -> {
                MTBlockObject block = requireBlock(args, 0);

                for (MTObject each : values) {
                    block.call(List.of(each));
                }

                yield this;
            }

            case "collect:" -> {
                MTBlockObject block = requireBlock(args, 0);
                List<MTObject> result = new ArrayList<>();

                for (MTObject each : values) {
                    result.add(block.call(List.of(each)));
                }

                yield new MTArray(result);
            }

            case "select:" -> {
                MTBlockObject block = requireBlock(args, 0);
                List<MTObject> result = new ArrayList<>();

                for (MTObject each : values) {
                    MTObject condition = block.call(List.of(each));

                    if (!(condition instanceof MTBoolean b)) {
                        throw new RuntimeException(
                                "Le bloc passé à select: doit retourner un Boolean, reçu: " + condition
                        );
                    }

                    if (b.value()) {
                        result.add(each);
                    }
                }

                yield new MTArray(result);
            }

            case "reject:" -> {
                MTBlockObject block = requireBlock(args, 0);
                List<MTObject> result = new ArrayList<>();

                for (MTObject each : values) {
                    MTObject condition = block.call(List.of(each));

                    if (!(condition instanceof MTBoolean b)) {
                        throw new RuntimeException(
                                "Le bloc passé à reject: doit retourner un Boolean, reçu: " + condition
                        );
                    }

                    if (!b.value()) {
                        result.add(each);
                    }
                }

                yield new MTArray(result);
            }

            case "detect:" -> {
                MTBlockObject block = requireBlock(args, 0);

                for (MTObject each : values) {
                    MTObject condition = block.call(List.of(each));

                    if (!(condition instanceof MTBoolean b)) {
                        throw new RuntimeException(
                                "Le bloc passé à detect: doit retourner un Boolean, reçu: " + condition
                        );
                    }

                    if (b.value()) {
                        yield each;
                    }
                }

                yield MTNil.INSTANCE;
            }

            case "anySatisfy:" -> {
                MTBlockObject block = requireBlock(args, 0);

                for (MTObject each : values) {
                    MTObject condition = block.call(List.of(each));

                    if (!(condition instanceof MTBoolean b)) {
                        throw new RuntimeException(
                                "Le bloc passé à anySatisfy: doit retourner un Boolean, reçu: " + condition
                        );
                    }

                    if (b.value()) {
                        yield new MTBoolean(true);
                    }
                }

                yield new MTBoolean(false);
            }

            case "allSatisfy:" -> {
                MTBlockObject block = requireBlock(args, 0);

                for (MTObject each : values) {
                    MTObject condition = block.call(List.of(each));

                    if (!(condition instanceof MTBoolean b)) {
                        throw new RuntimeException(
                                "Le bloc passé à allSatisfy: doit retourner un Boolean, reçu: " + condition
                        );
                    }

                    if (!b.value()) {
                        yield new MTBoolean(false);
                    }
                }

                yield new MTBoolean(true);
            }

            case "inject:into:" -> {
                MTObject accumulator = args.get(0);
                MTBlockObject block = requireBlock(args, 1);

                for (MTObject each : values) {
                    accumulator = block.call(List.of(accumulator, each));
                }

                yield accumulator;
            }

            case "printString" -> {
                StringBuilder sb = new StringBuilder("#(");

                for (int i = 0; i < values.size(); i++) {
                    if (i > 0) sb.append(" ");
                    sb.append(values.get(i));
                }

                sb.append(")");
                yield new MTString(sb.toString());
            }

            

	    default -> {
    		MTClass arrayClass = (MTClass) MTInterpreter.GLOBAL.lookup("Array");

    		MTMethod method = arrayClass.lookup(selector);

    		if (method != null) {
        		yield method.body().callWithReceiver(this, args, method.owner());
    		}

    		throw new RuntimeException("Message inconnu pour Array: " + selector);
	    }


        };
    }

    private MTBlockObject requireBlock(List<MTObject> args, int index) {
        if (index >= args.size()) {
            throw new RuntimeException("Argument manquant à l’indice " + index);
        }

        if (!(args.get(index) instanceof MTBlockObject block)) {
            throw new RuntimeException(
                    "Block attendu à l’indice " + index + ", reçu: " + args.get(index)
            );
        }

        return block;
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
