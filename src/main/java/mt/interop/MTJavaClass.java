package mt.interop;

import mt.runtime.MTString;import mt.runtime.MTObject;

import java.util.List;

public final class MTJavaClass implements MTObject {

    private final Class<?> value;

    public MTJavaClass(Class<?> value) {
        this.value = value;
    }

    public Class<?> value() {
        return value;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        if ("printString".equals(selector) && args.isEmpty()) {
            return new MTString(value.getName());
        }

        return MTInterop.sendToClass(value, selector, args);
    }

    @Override
    public String toString() {
        return value.getName();
    }
}
