package mt.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public final class MTDictionaryObject implements MTObject {

    private final Map<MTObject, MTObject> map;

    public MTDictionaryObject() {
        this.map = new HashMap<>();
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        return switch (selector) {

            //--------------------------------------------------
            // accès
            //--------------------------------------------------

            case "at:" -> {
                MTObject key = args.get(0);
                yield map.getOrDefault(key, MTNil.INSTANCE);
            }

            case "put:value:" -> {
                MTObject key = args.get(0);
                MTObject value = args.get(1);
                map.put(key, value);
                yield value;
            }

            case "containsKey:" -> {
                yield new MTBoolean(map.containsKey(args.get(0)));
            }

            case "remove:" -> {
                map.remove(args.get(0));
                yield this;
            }


	    case "do:" -> {
    		MTBlockObject block = requireBlock(args, 0);

    		for (var entry : map.entrySet()) {
        		MTObject key = entry.getKey();
        		MTObject value = entry.getValue();

        		block.call(List.of(key, value));
    		}

    		yield this;
	    }

            //--------------------------------------------------
            // collections
            //--------------------------------------------------

            case "keys" -> {
                yield new MTArray(new ArrayList<>(map.keySet()));
            }

            case "values" -> {
                yield new MTArray(new ArrayList<>(map.values()));
            }

            case "size" -> {
                yield new MTInteger(map.size());
            }

            //--------------------------------------------------
            // affichage
            //--------------------------------------------------

            case "printString" -> {
                StringBuilder sb = new StringBuilder("#{");
                boolean first = true;

                for (var entry : map.entrySet()) {
                    if (!first) sb.append(" ");
                    first = false;

                    MTObject k = entry.getKey();
                    MTObject v = entry.getValue();

                    sb.append(k.send("printString", List.of()))
                      .append(" -> ")
                      .append(v.send("printString", List.of()));
                }

                sb.append("}");
                yield new MTString(sb.toString());
            }

            default -> throw new RuntimeException("Message inconnu pour Dictionary: " + selector);
        };
    }

    private MTBlockObject requireBlock(List<MTObject> args, int index) {
    	if (index >= args.size()) {
        	throw new RuntimeException("Argument manquant " + index);
    	}
    	if (!(args.get(index) instanceof MTBlockObject block)) {
        	throw new RuntimeException("Block attendu " + index);
    	}
    	return block;
    }
}
