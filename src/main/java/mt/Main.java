package mt;

import mt.interpreter.MTInterpreter;
import mt.runtime.MTObject;
import mt.runtime.MTSystem;
import mt.runtime.MTString;
import mt.util.MTLibraryLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
public static void main(String[] args) throws Exception {

    List<String> cliPaths = parseLibraryPaths(args);
    List<Path> searchRoots = MTLibraryLoader.resolveSearchRoots(cliPaths);

    MTInterpreter interpreter = new MTInterpreter();

    interpreter.getGlobalEnv().define(
            "System",
            new MTSystem(interpreter, searchRoots)
    );

    try {
        MTLibraryLoader.loadAllAtStartup(interpreter, searchRoots);
    } catch (RuntimeException e) {
        System.err.println("[stdlib:error] " + e.getMessage());
        System.exit(1);
    }

    // NOUVEAU : exécution des fichiers passés en argument
    runFilesFromCLI(args, interpreter);

    runRepl(interpreter);
}

    private static List<String> parseLibraryPaths(String[] args) {
        List<String> cliPaths = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if ("-L".equals(args[i])) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Option -L sans chemin");
                }
                cliPaths.add(args[++i]);
            }
        }

        return cliPaths;
    }

private static void runRepl(MTInterpreter interpreter) throws Exception {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("miniTalk REPL");
    System.out.println("Tape 'exit' pour quitter.");

    StringBuilder buffer = new StringBuilder();
    int parenDepth = 0;
    int bracketDepth = 0;

    while (true) {
        System.out.print(buffer.isEmpty() ? "mt> " : "..> ");
        String line = reader.readLine();

        if (line == null) {
            System.out.println("Bye.");
            break;
        }

        if (buffer.isEmpty() && line.equals("exit")) {
            System.out.println("Bye.");
            break;
        }

        buffer.append(line).append("\n");

        parenDepth += count(line, '(');
        parenDepth -= count(line, ')');
        bracketDepth += count(line, '[');
        bracketDepth -= count(line, ']');

        boolean expressionClosed =
                parenDepth == 0 &&
                bracketDepth == 0 &&
                line.trim().endsWith(".");

        if (!expressionClosed) {
            continue;
        }

        String source = buffer.toString();
        buffer.setLength(0);

        try {
            MTObject result = MTLibraryLoader.executeSource(source, interpreter);
            if (result != null) {
                //System.out.println(result);
		MTObject printable = result.send("printString", List.of());

		if (printable instanceof MTString s) {
    			System.out.println(s.value());
		} else {
    			System.out.println(printable.toString());
		}
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}

private static int count(String s, char c) {
    int n = 0;
    for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) == c) {
            n++;
        }
    }
    return n;
}


private static void runFilesFromCLI(String[] args, MTInterpreter interpreter) {
    for (int i = 0; i < args.length; i++) {

        // ignorer les options -L
        if ("-L".equals(args[i])) {
            i++; // skip path argument
            continue;
        }

        String arg = args[i];

        // heuristique simple : fichier .mt
        if (arg.endsWith(".mt")) {
            try {
                System.out.println("[exec] " + arg);
                MTLibraryLoader.executeFile(Path.of(arg), interpreter);
            } catch (Exception e) {
                System.err.println("Erreur exécution fichier : " + arg);
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }
}
}
