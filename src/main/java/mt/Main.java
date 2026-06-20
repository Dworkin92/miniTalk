package mt;

import mt.interpreter.MTInterpreter;
import mt.lexer.MTLexer;
import mt.parser.MTParser;
import mt.runtime.MTObject;
import mt.runtime.MTSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        MTInterpreter interpreter = new MTInterpreter();

        // ✅ injecter System (proprement)
        interpreter.getGlobalEnv().define(
                "System",
                new MTSystem(interpreter, List.of())
        );

        // ✅ charger stdlib (optionnel mais safe)
        loadStdlib(interpreter);

        runRepl(interpreter);
    }

    private static void runRepl(MTInterpreter interpreter) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("miniTalk REPL");
        System.out.println("Tape 'exit' pour quitter.");

        while (true) {
            System.out.print("mt> ");
            String line = reader.readLine();

            if (line == null || line.equals("exit")) {
                System.out.println("Bye.");
                break;
            }

            try {
                MTObject result = execute(line, interpreter);
                if (result != null) {
                    System.out.println(result);
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private static MTObject execute(String source, MTInterpreter interpreter) {
        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        return interpreter.eval(parser.parseProgram());
    }

    private static void loadStdlib(MTInterpreter interpreter) {
        try {
            Path path = Path.of("stdlib", "stdlib.mt");   // ✅ plus sûr

            if (Files.exists(path)) {
                execute(Files.readString(path), interpreter);
                System.out.println("[stdlib chargée]");
            } else {
                System.out.println("[stdlib absente]");
            }

        } catch (Exception e) {
            System.out.println("Erreur chargement stdlib: " + e.getMessage());
        }
    }
}
