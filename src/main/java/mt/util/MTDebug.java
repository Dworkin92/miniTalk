package mt.util;

public final class MTDebug {

    private static int indent = 0;


    public static void enter(String msg) {
        if (MTConfig.DEBUG) {
            printIndent();
            System.out.println("-> " + msg);
            indent++;
        }
    }

    public static void exit(String msg) {
        if (MTConfig.DEBUG) {
            indent--;
            printIndent();
            System.out.println("<- " + msg);
        }
    }

    private static void printIndent() {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
    }


    public static void log(String msg) {
        if (MTConfig.DEBUG) {
            printIndent();
            System.out.println(msg);
        }
    }

}
