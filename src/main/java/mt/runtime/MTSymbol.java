package mt.runtime;

import java.util.Map;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public final class MTSymbol
        extends MTString {

    private static final Map<String, MTSymbol>
            INTERNED =
                new ConcurrentHashMap<>();

    private static MTClass symbolClass = null;


    private MTSymbol(String value) {

        super(value);
    }

    public static void setSymbolClass(MTClass clazz) {
        symbolClass = clazz;
    }

    public static MTSymbol intern(String value) {
        MTSymbol symbol = INTERNED.computeIfAbsent(
            value, MTSymbol::new);

        if (symbolClass != null && symbol.getClazz() == null) {
            symbol.setClazz(symbolClass);
        }

        return symbol;
    }

    public static void setRuntimeClass(MTClass clazz) {
        symbolClass = clazz;
    }

    public static Collection<MTSymbol> allInterned() {
        return INTERNED.values();
    }

    @Override
    public String toString() {

        return "#" + getValue();
    }

    @Override
    public int hashCode() {

        return getValue().hashCode();
    }

    @Override
    public boolean equals(
            Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MTSymbol other)) {
            return false;
        }

        return getValue().equals(
                other.getValue());
    }
}
