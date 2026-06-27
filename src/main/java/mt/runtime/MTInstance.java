package mt.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MTInstance implements MTObject {

    private final MTClass clazz;
    private final Map<String, MTObject> fields = new HashMap<>();

    public MTInstance(MTClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        if (selector.equals("printString") && args.isEmpty()) {
            //return new MTString("a " + clazz.toString());
	    MTObject nameObj = clazz.send("name", List.of());
	    String name = ((MTString) nameObj).value();

	    String article = startsWithVowel(name) ? "an " : "a ";

	    return new MTString(article + name);
        }

        // --- getters / setters automatiques pour variables d’instance déclarées ---

        if (args.isEmpty() && clazz.hasInstVar(selector)) {
            return fields.getOrDefault(selector, MTNil.INSTANCE);
        }

        if (selector.endsWith(":") && args.size() == 1) {
            String fieldName = selector.substring(0, selector.length() - 1);
            if (clazz.hasInstVar(fieldName)) {
                fields.put(fieldName, args.get(0));
                return args.get(0);
            }
        }

        // --- lookup de méthode normale ---
        MTMethod method = clazz.lookup(selector);

        if (method == null) {
            throw new RuntimeException("Message inconnu: " + selector);
        }

        return method.body().callWithReceiver(this, args, method.owner());
    }

    public void setField(String name, MTObject value) {
        fields.put(name, value);
    }

    public MTObject getField(String name) {
        return fields.get(name);
    }


    private boolean startsWithVowel(String s) {
    	if (s.isEmpty()) return false;
    	char c = Character.toLowerCase(s.charAt(0));
    	return "aeiou".indexOf(c) >= 0;
    }


    public MTClass clazz() {
        return clazz;
    }
}