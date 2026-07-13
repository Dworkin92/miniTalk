package mt.debug;

public final class MTDebug {

    private static boolean enabled;

    private MTDebug() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(
            boolean enabled) {

        MTDebug.enabled = enabled;
    }

    public static void log(
            String message) {

        if (enabled) {
            System.out.println(message);
        }
    }
}
