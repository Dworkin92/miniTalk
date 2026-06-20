package mt.runtime;

import java.util.List;

public class MTSuper implements MTObject {

    private final MTInstance self;
    private final MTClass startClass;

    public MTSuper(MTInstance self, MTClass startClass) {
        this.self = self;
        this.startClass = startClass;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        MTMethod method = startClass.lookup(selector);

        if (method == null) {
            throw new RuntimeException("super: message inconnu " + selector);
        }

        return method.body().callWithReceiver(self, args, method.owner());
    }
}
