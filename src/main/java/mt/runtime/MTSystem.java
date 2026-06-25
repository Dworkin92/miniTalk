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
        return switch (selector) {

	    case "print:" -> {
    		if (!(args.get(0) instanceof MTObject obj)) {
        		throw new RuntimeException("Argument invalide pour print:");
    		}

    		System.out.println(obj.toString());
    		yield MTNil.INSTANCE;
	    }

            case "load:" -> {
                String name = ((MTString) args.get(0)).value();
                try {
                    yield MTLibraryLoader.loadNamedLibrary(name, interpreter, searchRoots);
                } catch (Exception e) {
                    throw new RuntimeException("Erreur load: " + name + " -> " + e.getMessage(), e);
                }
            }
            default -> throw new RuntimeException("Message inconnu pour System: " + selector);
        };
    }
}
