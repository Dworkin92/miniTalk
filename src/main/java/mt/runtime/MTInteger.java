package mt.runtime;

import java.util.List;

public final class MTInteger implements MTObject {

    private final int value;

    public MTInteger(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        switch (selector) {

            case "+":
                if (args.get(0) instanceof MTFloat f) {
                    return new MTFloat(value + f.value());
                }
                return new MTInteger(value + ((MTInteger) args.get(0)).value());

            case "-":
                if (args.get(0) instanceof MTFloat f) {
                    return new MTFloat(value - f.value());
                }
                return new MTInteger(value - ((MTInteger) args.get(0)).value());

            case "*":
                if (args.get(0) instanceof MTFloat f) {
                    return new MTFloat(value * f.value());
                }
                return new MTInteger(value * ((MTInteger) args.get(0)).value());

            case "/":
                return new MTFloat((double) value / ((MTInteger) args.get(0)).value());

	    case "//":
    		if (args.get(0) instanceof MTFloat) {
        		MTFloat f = (MTFloat) args.get(0);
        		int divisor = (int) f.value();

        		if (divisor == 0) {
            			throw new RuntimeException("Division entière par zéro");
        		}

        		return new MTInteger(value / divisor);
    		}

    		int divisor = ((MTInteger) args.get(0)).value();

    		if (divisor == 0) {
        		throw new RuntimeException("Division entière par zéro");
    		}
    		return new MTInteger(value / divisor);

	    case "%":
    		if (args.get(0) instanceof MTFloat f) {
        		return new MTFloat(value % f.value());
    		}
    		return new MTInteger(value % ((MTInteger) args.get(0)).value());


            case ">=":
                return new MTBoolean(value >= ((MTInteger) args.get(0)).value());

            case ">":
                return new MTBoolean(value > ((MTInteger) args.get(0)).value());
 
            case "<=":
                return new MTBoolean(value <= ((MTInteger) args.get(0)).value());

            case "<":
                return new MTBoolean(value < ((MTInteger) args.get(0)).value());

            case "=":
                if (args.get(0) instanceof MTInteger i) {
                    return new MTBoolean(value == i.value());
                }
                if (args.get(0) instanceof MTFloat f) {
                    return new MTBoolean(value == f.value());
                }
                return new MTBoolean(false);

            case "!=", "<>": 
                MTObject eq = send("=", args);
                return ((MTBoolean) eq).value()
                    ? new MTBoolean(false)
                    : new MTBoolean(true);

	    case "to:do:": {
    		if (!(args.get(0) instanceof MTInteger)) {
        		throw new RuntimeException("Argument entier attendu pour to:");
    		}
    		if (!(args.get(1) instanceof MTBlockObject)) {
        		throw new RuntimeException("Block attendu pour do:");
    		}

    		int end = ((MTInteger) args.get(0)).value();
    		MTBlockObject block = (MTBlockObject) args.get(1);

    		MTObject last = MTNil.INSTANCE;

    		if (value <= end) {
        		// sens croissant
        		for (int i = value; i <= end; i++) {
            			last = block.call(List.of(new MTInteger(i)));
        		}
    		} else {
        		// sens décroissant
        		for (int i = value; i >= end; i--) {
            			last = block.call(List.of(new MTInteger(i)));
        		}
    		}

    		return last;
	    }

            case "timesRepeat:": {
                if (!(args.get(0) instanceof MTBlockObject)) {
                    throw new RuntimeException("Block attendu pour timesRepeat:");
                }

                MTBlockObject block = (MTBlockObject) args.get(0);
                MTObject last = MTNil.INSTANCE;

                for (int i = 0; i < value; i++) {
                    last = block.call(List.of());
                }

                return last;
	    }

	    case "timesRepeatWithIndex:": {
    		if (!(args.get(0) instanceof MTBlockObject)) {
        		throw new RuntimeException("Block attendu pour timesRepeatWithIndex:");
    		}

    		MTBlockObject block = (MTBlockObject) args.get(0);
    		MTObject last = MTNil.INSTANCE;

    		for (int i = 0; i < value; i++) {
        		last = block.call(List.of(new MTInteger(i)));
    		}

    		return last;
	    }

            case "printString", "toString" :
                return new MTString(Integer.toString(value));
        }

        throw new RuntimeException("Message inconnu Integer: " + selector);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
