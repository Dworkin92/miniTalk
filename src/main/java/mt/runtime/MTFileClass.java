package mt.runtime;

import java.util.List;

public final class MTFileClass implements MTObject {

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {
            case "open:mode:" -> {
                String path = ((MTString) args.get(0)).value();
                String mode = ((MTString) args.get(1)).value();
                yield new MTFileHandle(path, mode);
            }
            case "exists:" -> {
                String path = ((MTString) args.get(0)).value();
                yield new MTBoolean(MTFileHandle.exists(path));
            }
            case "delete:" -> {
                String path = ((MTString) args.get(0)).value();
                MTFileHandle.delete(path);
                yield MTNil.INSTANCE;
            }
            case "size:" -> {
                String path = ((MTString) args.get(0)).value();
                yield new MTInteger((int) MTFileHandle.size(path));
            }
            case "printString" -> new MTString("File");
            default -> throw new RuntimeException("Message inconnu pour File: " + selector);
        };
    }

    @Override
    public String toString() {
        return "File";
    }
}
