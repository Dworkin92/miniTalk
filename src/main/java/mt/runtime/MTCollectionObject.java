package mt.runtime;

import java.util.Collection;
import java.util.List;

public abstract class MTCollectionObject implements MTObject {

    protected final Collection<Object> delegate;

    protected MTCollectionObject(Collection<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        switch (selector) {

            case "size" -> {
                return new MTInteger(delegate.size());
            }

            case "isEmpty" -> {
                return new MTBoolean(delegate.isEmpty());
            }

            case "remove:" -> {
                Object value = unwrap(args.get(0));
                delegate.remove(value);
                return this;
            }

            case "printString" -> {
                return new MTString(delegate.toString());
            }
        }

        throw new RuntimeException("Message inconnu pour Collection: " + selector);
    }

    protected Object unwrap(MTObject obj) {
        if (obj instanceof MTInteger i) return i.value();
        if (obj instanceof MTString s) return s.value();
        if (obj instanceof MTBoolean b) return b.value();
        return obj;
    }
}
