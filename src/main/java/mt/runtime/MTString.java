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
