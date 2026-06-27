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


	    case "//": {
    		if (args.get(0) instanceof MTFloat f) {
        		return new MTFloat(Math.floor(value / f.value()));
    		}

    		double divisor = ((MTInteger) args.get(0)).value();

    		if (divisor == 0) {
        		throw new RuntimeException("Division entière par zéro");
    		}

    		return new MTFloat(Math.floor(value / divisor));
	    }

	    case "%": {
    		if (args.get(0) instanceof MTFloat f) {
        		return new MTFloat(value % f.value());
    		}

    		return new MTFloat(value % ((MTInteger) args.get(0)).value());
	    }

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

	    case "to:step:do:": {
    		if (!(args.get(2) instanceof MTBlockObject)) {
        		throw new RuntimeException("Block attendu pour do:");
    		}

    		double end;
    		if (args.get(0) instanceof MTFloat f) {
        		end = f.value();
    		} else {
        		end = ((MTInteger) args.get(0)).value();
    		}

    		double step;
    		if (args.get(1) instanceof MTFloat f) {
        		step = f.value();
    		} else {
        		step = ((MTInteger) args.get(1)).value();
    		}

    		if (step == 0.0) {
        		throw new RuntimeException("step ne peut pas être 0");
    		}

    		MTBlockObject block = (MTBlockObject) args.get(2);
    		MTObject last = MTNil.INSTANCE;

    		if (step > 0) {
        		for (double i = value; i <= end; i += step) {
            			last = block.call(List.of(new MTFloat(i)));
        		}
    		} else {
        		for (double i = value; i >= end; i += step) {
            			last = block.call(List.of(new MTFloat(i)));
        		}
    		}

    		return last;
	    }

	    case "round:": {
    		int digits = ((MTInteger) args.get(0)).value();
    		double factor = Math.pow(10, digits);
    		double rounded = Math.round(value * factor) / factor;
    		return new MTFloat(rounded);
	    }

	    case "approxEqual:": {
    		double other = asDouble(args.get(0));
    		return new MTBoolean(Math.abs(value - other) < 1e-9);
	    }

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

    	return new java.math.BigDecimal(value)
            .stripTrailingZeros()
            .toPlainString();

        /* return Double.toString(value); */
    }
}