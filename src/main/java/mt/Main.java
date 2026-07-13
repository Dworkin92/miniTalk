package mt;

import mt.tests.RuntimeRegressionTests;
import mt.debug.MTDebug;

public class Main {

    public static void main(String[] args) {
        for (String arg : args) {

            if ("-debug".equals(arg)
                || "-v".equals(arg)) {
                MTDebug.setEnabled(true);
            }
        }

        RuntimeRegressionTests.runAll();
    }
}
