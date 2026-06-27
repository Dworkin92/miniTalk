package mt.runtime;

import java.util.List;

public final class MTSelf implements MTObject {

    private final MTInstance instance;

    public MTSelf(MTInstance instance) {
        this.instance = instance;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return instance.send(selector, args);
    }
}