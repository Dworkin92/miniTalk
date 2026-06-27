package mt.runtime;

import java.util.List;

public final class MTReturnTarget implements MTObject {

    private final Object token;

    public MTReturnTarget(Object token) {
        this.token = token;
    }

    public Object token() {
        return token;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        throw new RuntimeException("Message inconnu pour return target: " + selector);
    }
}