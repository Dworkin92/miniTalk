package mt.runtime;

import java.util.List;

public final class MTBoolean implements MTObject {

    private final boolean value;

    public MTBoolean(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {

            // ---------------------------------
            // logique de base
            // ---------------------------------

            case "not" -> new MTBoolean(!value);

            case "and:" -> {
                MTBlockObject block = requireBlock(args, 0);

                // court-circuit
                if (!value) {
                    yield new MTBoolean(false);
                }

                MTObject result = block.call(List.of());

                if (!(result instanceof MTBoolean b)) {
                    throw new RuntimeException("and: exige un Boolean");
                }

                yield b;
            }

            case "or:" -> {
                MTBlockObject block = requireBlock(args, 0);

                // court-circuit
                if (value) {
                    yield new MTBoolean(true);
                }

                MTObject result = block.call(List.of());

                if (!(result instanceof MTBoolean b)) {
                    throw new RuntimeException("or: exige un Boolean");
                }

                yield b;
            }

            case "xor:" -> {
                MTBlockObject block = requireBlock(args, 0);

                MTObject result = block.call(List.of());

                if (!(result instanceof MTBoolean b)) {
                    throw new RuntimeException("xor: exige un Boolean");
                }

                yield new MTBoolean(value ^ b.value());
            }

            // ---------------------------------
            // conditions
            // ---------------------------------

            case "ifTrue:" -> {
                MTBlockObject block = requireBlock(args, 0);
                if (value) {
                    yield block.call(List.of());
                }
                yield MTNil.INSTANCE;
            }

            case "ifFalse:" -> {
                MTBlockObject block = requireBlock(args, 0);
                if (!value) {
                    yield block.call(List.of());
                }
                yield MTNil.INSTANCE;
            }

            case "ifTrue:else:", "ifTrue:ifFalse:" -> {
                MTBlockObject t = requireBlock(args, 0);
                MTBlockObject f = requireBlock(args, 1);
                yield value ? t.call(List.of()) : f.call(List.of());
            }

            case "ifFalse:else:", "ifFalse:ifTrue:"  -> {
                MTBlockObject f = requireBlock(args, 0);
                MTBlockObject t = requireBlock(args, 1);
                yield value ? t.call(List.of()) : f.call(List.of());
            }

            case "printString" -> new MTString(Boolean.toString(value));

            default -> throw new RuntimeException(
                    "Message inconnu pour Boolean: " + selector
            );
        };
    }

    private MTBlockObject requireBlock(List<MTObject> args, int index) {
        if (index >= args.size()) {
            throw new RuntimeException("Block manquant à l’indice " + index);
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
        return Boolean.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
    	if (!(obj instanceof MTBoolean other)) return false;
    	return value == other.value();
    }

    @Override
    public int hashCode() {
    	return Boolean.hashCode(value);
    }
}