package mt.runtime;

import java.util.List;

public final class MTString implements MTObject {

    private final String value;

    public MTString(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {
            case "size" -> new MTInteger(value.length());
            case "," -> new MTString(value + ((MTString) args.get(0)).value());

	    case "*" -> {
    		if (!(args.get(0) instanceof MTInteger)) {
        		throw new RuntimeException("Argument entier attendu pour *");
    		}

    		int n = ((MTInteger) args.get(0)).value();

    		if (n < 0) {
        		throw new RuntimeException("Multiplication par un entier négatif interdite");
    		}

    		StringBuilder result = new StringBuilder();

    		for (int i = 0; i < n; i++) {
        		result.append(value);
    		}

    		yield new MTString(result.toString());
	    }

	    case "!=" -> {
    		MTObject eq = send("=", args);
    		yield ((MTBoolean) eq).value()
        		? new MTBoolean(false)
        		: new MTBoolean(true);
	    }

	    case "=" -> {
    		if (args.get(0) instanceof MTString s) {
        		yield new MTBoolean(value.equals(s.value()));
    		}
    		yield new MTBoolean(false);
	    }

            
	    case "charsDo:": {
    		MTBlockObject block = (MTBlockObject) args.get(0);

    		MTObject last = MTNil.INSTANCE;

    		for (int i = 0; i < value.length(); i++) {
        		String ch = String.valueOf(value.charAt(i));
       			last = block.call(List.of(new MTString(ch)));
    		}

    		yield last;
	    }

	    case "asArray": {
    		MTArray array = new MTArray();

    		for (int i = 0; i < value.length(); i++) {
        		String ch = String.valueOf(value.charAt(i));
        		array.add(new MTString(ch));
    		}

    		yield array;
	    }

	    case "printString" -> {
    		System.out.println(value);
    		yield this;
	    }

            default -> throw new RuntimeException("Message inconnu pour String: " + selector);
        };
    }

    @Override
    public String toString() {
        return value;
    }
}
