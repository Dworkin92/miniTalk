package mt.interop;

import mt.runtime.MTObject;
import mt.runtime.MTString;

import java.util.List;

public final class MTJavaObject implements MTObject {

    private final Object value;

    public MTJavaObject(Object value) {
        this.value = value;
    }

    public Object value() {
        return value;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        if ("printString".equals(selector) && args.isEmpty()) {
            return new MTString(String.valueOf(value));
        }

        return MTInterop.sendToInstance(value, selector, args);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
