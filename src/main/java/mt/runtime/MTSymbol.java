package mt.runtime;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MTSymbol
        extends MTString {

    private static final Map<String, MTSymbol>
            INTERNED =
                new ConcurrentHashMap<>();

    private MTSymbol(String value) {

        super(value);
    }

    public static MTSymbol intern(
            String value) {

        return INTERNED.computeIfAbsent(
                value,
                MTSymbol::new);
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
