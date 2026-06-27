package mt.runtime;

import java.util.List;

public final class MTNil implements MTObject {

    public static final MTNil INSTANCE = new MTNil();

    private MTNil() {}

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {
	    case "=" -> {
    		if (args.get(0) instanceof MTNil) {
        		yield new MTBoolean(true);
    		}
    		yield new MTBoolean(false);
	    }

	    case "!=" -> {
    		MTObject eq = send("=", args);
    		yield ((MTBoolean) eq).value()
        		? new MTBoolean(false)
        		: new MTBoolean(true);
	    }

            case "printString" -> new MTString("nil");
            default -> throw new RuntimeException("Message inconnu pour nil: " + selector);
        };
    }

    @Override
    public String toString() {
        return "nil";
    }
}