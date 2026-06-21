package mt.runtime;

import java.util.ArrayList;
import java.util.List;

public final class MTProcessClass implements MTObject {

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {

            case "shell:" -> {
                String command = ((MTString) args.get(0)).value();
                yield MTProcessHandle.startShell(command);
            }

            case "exec:" -> {
                MTArray array = (MTArray) args.get(0);
                yield MTProcessHandle.startExec(toStringList(array));
            }

            case "printString" -> new MTString("Process");

            default -> throw new RuntimeException("Message inconnu pour Process: " + selector);
        };
    }

    private List<String> toStringList(MTArray array) {
        int size = ((MTInteger) array.send("size", List.of())).value();
        List<String> result = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            MTObject obj = array.send("at:", List.of(new MTInteger(i)));
            if (!(obj instanceof MTString s)) {
                throw new RuntimeException("exec: attend un Array de String");
            }
            result.add(s.value());
        }

        return result;
    }

    @Override
    public String toString() {
        return "Process";
    }
}
