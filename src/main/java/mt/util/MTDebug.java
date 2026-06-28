package mt.util;

public final class MTDebug {

    public static void log(String msg) {
        if (MTConfig.DEBUG) {
            System.out.println(msg);
        }
    }
}
