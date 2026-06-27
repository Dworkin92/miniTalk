package mt.runtime;


import java.util.List;

public final class MTSetClass implements MTObject {

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        if (selector.equals("new")) {
            return new MTSetObject();
        }

        throw new RuntimeException("Message inconnu pour Set");
    }
}