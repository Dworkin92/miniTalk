package mt.runtime;

import mt.util.MTLibraryLoader;
import mt.interpreter.MTInterpreter;

import java.nio.file.Path;
import java.util.List;

public class MTSystem implements MTObject {
    private final MTInterpreter interpreter;
    private final List<Path> searchRoots;

    public MTSystem(MTInterpreter interpreter, List<Path> searchRoots) {
        this.interpreter = interpreter;
        this.searchRoots = List.copyOf(searchRoots);
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        //System.out.println("DEBUG selector=" + selector);
        return switch (selector) {

	    case "print", "print:" -> {
		//System.out.println("DEBUG selector=" + selector);

		MTObject obj = args.get(0);

		MTObject ps = obj.send("printString", List.of());

		if (ps instanceof MTString s) {
    			System.out.println(s.value());
		} else {
    			System.out.println(ps.toString());
		}

    		yield MTNil.INSTANCE;
	    }

            case "load:" -> {

    		String path = ((MTString) args.get(0)).value();

    		try {
        		Path p = Path.of(path);

        		// si chemin explicite → charger direct
        		if (p.toFile().exists()) {
            			yield MTLibraryLoader.executeFile(p, interpreter);
        		}

        		// sinon fallback sur searchRoots
        		for (Path root : searchRoots) {
            			Path candidate = root.resolve(path);
            			if (candidate.toFile().exists()) {
                			yield MTLibraryLoader.executeFile(candidate, interpreter);
            			}
        		}

        		throw new RuntimeException("Librairie introuvable: " + path);

    		} catch (Exception e) {
        		throw new RuntimeException("Erreur load: " + path + " -> " + e.getMessage());
    		}
            }
            default -> throw new RuntimeException("Message inconnu pour System: " + selector);
        };
    }
}