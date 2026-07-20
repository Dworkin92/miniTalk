package mt.exceptions;

public class MTBootstrapException
        extends RuntimeException {

    public MTBootstrapException(
            String message) {

        super(message);
    }

    public MTBootstrapException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}
