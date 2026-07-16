package mt;

import mt.tests.RuntimeRegressionTests;

import mt.debug.MTDebug;
import java.nio.file.Path;
import mt.runtime.*;

public class Main {

    public static void main(String[] args) {
        String   myMtFile = "";
        boolean  needsRegTests = false;

        if (args.length == 0) {
            System.err.println("Usage: minitalk <file.mt>");
            return;
        }

        for (String arg : args) {
            if ("-debug".equals(arg)
                || "-v".equals(arg)) {
                MTDebug.setEnabled(true);
            }
            else if ("-test".equals(arg)) {
                needsRegTests = true;
            }
            else {
                myMtFile = arg;
            }
        }

        if (needsRegTests) {
            RuntimeRegressionTests.runAll();
        }
        else {
            MTScope global = new MTScope(null);
            MTLoader loader = new MTLoader(global);
            MTObject result = loader.loadFile(Path.of(myMtFile));

            System.out.println(result);
        }
    }
}
