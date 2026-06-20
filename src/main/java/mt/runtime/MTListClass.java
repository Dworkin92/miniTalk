package mt.runtime;

import java.util.List;

public final class MTListClass implements MTObject {

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        if (selector.equals("new")) {
            return new MTListObject();
        }

        throw new RuntimeException("Message inconnu pour List");
    }
}

