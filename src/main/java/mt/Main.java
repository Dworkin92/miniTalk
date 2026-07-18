package mt;

import mt.tests.RuntimeRegressionTests;

import mt.debug.MTDebug;
import java.nio.file.Path;
//import mt.runtime.*;
import mt.runtime.MTObject;
import mt.runtime.MTScope;
import mt.runtime.MTLoader;
import mt.runtime.MTRuntime;
import mt.runtime.bootstrap.MTRuntimeBootstrap;

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

        MTRuntime runtime = MTRuntimeBootstrap.bootstrap();

        if (needsRegTests) {
            RuntimeRegressionTests.runAll();
        }
        else {
            MTScope global = new MTScope(runtime, null);
            MTLoader loader = new MTLoader(runtime,global);
            MTObject result = loader.loadFile(Path.of(myMtFile));

            System.out.println(result);
        }
    }
}
