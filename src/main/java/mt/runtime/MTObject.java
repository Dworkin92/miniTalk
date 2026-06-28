package mt.runtime;

import java.util.List;

public interface MTObject {
    MTObject send(String selector, List<MTObject> args);

    default MTObject sendDefaultIntrospection(String selector,
	List<MTObject> args, MTClass cls) {
    	return switch (selector) {
        	case "class" -> cls;

        	case "respondsTo:" -> {
            		String sel = ((MTString) args.get(0)).value();
            		yield new MTBoolean(cls.lookup(sel) != null);
        	}

        	case "perform:" -> {
            		String sel = ((MTString) args.get(0)).value();
            		yield this.send(sel, List.of());
        	}

        	case "perform:with:" -> {
            		String sel = ((MTString) args.get(0)).value();
            		MTArray arr = (MTArray) args.get(1);
            		yield this.send(sel, ((List<MTObject>) arr.send("asList", List.of())));
        	}

        	default -> throw new RuntimeException("Unknown message: " + selector);
    	};
    }
}