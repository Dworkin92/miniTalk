package mt.interop;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public final class MTSelectorMapper {

    private MTSelectorMapper() {}

    public static List<String> candidates(String selector, int arity) {
        LinkedHashSet<String> result = new LinkedHashSet<>();

        if ("printString".equals(selector)) {
            result.add("toString");
        }

        if (!selector.contains(":")) {
            result.add(selector);
            return List.copyOf(result);
        }

        List<String> parts = keywordParts(selector);
        if (!parts.isEmpty()) {
            result.add(parts.get(0));
            result.add(String.join("", parts));
        }

        return List.copyOf(result);
    }

    public static List<String> keywordParts(String selector) {
        String[] raw = selector.split(":");
        List<String> parts = new ArrayList<>();
        for (String s : raw) {
            if (!s.isBlank()) {
                parts.add(s);
            }
        }
        return parts;
    }
}
