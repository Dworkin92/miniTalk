package mt.runtime;

import java.util.List;

public final class MTFloat implements MTObject {

    private final double value;

    public MTFloat(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        switch (selector) {

            case "+":
                return new MTFloat(value + asDouble(args.get(0)));

            case "-":
                return new MTFloat(value - asDouble(args.get(0)));

            case "*":
                return new MTFloat(value * asDouble(args.get(0)));

            case "/":
                return new MTFloat(value / asDouble(args.get(0)));

            case ">":
                return new MTBoolean(value > asDouble(args.get(0)));

            case "<":
                return new MTBoolean(value < asDouble(args.get(0)));

            case "=":
                if (args.get(0) instanceof MTFloat f) {
                    return new MTBoolean(value == f.value());
                }
                if (args.get(0) instanceof MTInteger i) {
                    return new MTBoolean(value == i.value());
                }
                return new MTBoolean(false);

            case "printString":
                return new MTString(Double.toString(value));
        }

        throw new RuntimeException("Message inconnu Float: " + selector);
    }

    private double asDouble(MTObject obj) {
        if (obj instanceof MTInteger i) return i.value();
        if (obj instanceof MTFloat f) return f.value();
        throw new RuntimeException("Nombre attendu, reçu: " + obj);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
