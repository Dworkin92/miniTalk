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


	    case "//":
    		if (args.get(0) instanceof MTFloat f) {
        		return new MTFloat(Math.floor(value / f.value()));
    		}

    		double divisor = ((MTInteger) args.get(0)).value();

    		if (divisor == 0) {
        		throw new RuntimeException("Division entière par zéro");
    		}

    		return new MTFloat(Math.floor(value / divisor));

	    case "%":
    		if (args.get(0) instanceof MTFloat f) {
        		return new MTFloat(value % f.value());
    		}

    		return new MTFloat(value % ((MTInteger) args.get(0)).value());

            case ">=":
                return new MTBoolean(value >= asDouble(args.get(0)));

            case ">":
                return new MTBoolean(value > asDouble(args.get(0)));

            case "<=":
                return new MTBoolean(value <= asDouble(args.get(0)));

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

            case "!=", "<>":
                MTObject eq = send("=", args);
                return ((MTBoolean) eq).value()
                    ? new MTBoolean(false)
                    : new MTBoolean(true);

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
