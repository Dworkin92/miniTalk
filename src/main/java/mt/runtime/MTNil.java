package mt.runtime;

import java.util.List;

public final class MTNil implements MTObject {

    public static final MTNil INSTANCE = new MTNil();

    private MTNil() {}

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {
            case "printString" -> new MTString("nil");
            default -> throw new RuntimeException("Message inconnu pour nil: " + selector);
        };
    }

    @Override
    public String toString() {
        return "nil";
    }
}
