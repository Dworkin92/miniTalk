package mt.exceptions;

public class MTFileException
        extends MTException {

    public MTFileException(
            String message) {

        super(message);
    }

    public MTFileException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}
