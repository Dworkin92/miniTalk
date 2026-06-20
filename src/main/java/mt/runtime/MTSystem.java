package mt.runtime;

import mt.interpreter.MTInterpreter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MTSystem implements MTObject {

    private final MTInterpreter interpreter;

private final List<String> searchPaths = new ArrayList<>();

public MTSystem(MTInterpreter interpreter, List<String> cliPaths) {
    this.interpreter = interpreter;

    // -L
    searchPaths.addAll(cliPaths);

    //  env
    String env = System.getenv("MT_STDLIB_DIR");
    if (env != null && !env.isEmpty()) {
        searchPaths.add(env);
    }

    // fallback
    searchPaths.add("./stdlib");
}


    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {

            case "load:" -> {
                String name = ((MTString) args.get(0)).value();

                MTObject result;

                try {
                    String source = findLibraryFile(name);
                    result = interpreter.eval(
                        new mt.parser.MTParser(
                            new mt.lexer.MTLexer(source).tokenize()
                        ).parseProgram()
                    );
                } catch (Exception e) {
                    throw new RuntimeException("Erreur load: " + name + " -> " + e.getMessage());
                }

                yield result;
            }

            default -> throw new RuntimeException("Message inconnu pour System: " + selector);
        };
    }

    private String findLibraryFile(String name) throws Exception {

    	List<String> searchPaths = new ArrayList<>();

    	// variable d'environnement
    	String env = System.getenv("MT_STDLIB_DIR");

    	if (env != null && !env.isEmpty()) {
        	searchPaths.add(env);
    	}

    	// fallback local (ton projet)
    		searchPaths.add("./stdlib");
    		searchPaths.add("./lib");

    		// recherche du fichier
    	for (String dir : searchPaths) {
        	Path path = Path.of(dir, name + ".mt");

        	if (Files.exists(path)) {
            		return Files.readString(path);
        	}
    	}

    	throw new RuntimeException("Librairie introuvable: " + name);
    }
}
