package mt.interop;

public class MTJavaInteropException extends RuntimeException {

    public MTJavaInteropException(String message) {
        super(message);
    }

    public MTJavaInteropException(String message, Throwable cause) {
        super(message, cause);
    }
}
