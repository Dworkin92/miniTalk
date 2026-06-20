package mt.parser;

public class MTParseException extends RuntimeException {

    public MTParseException(String message) {
        super(message);
    }

    public MTParseException(String message, int position) {
        super(message + " (position " + position + ")");
    }
}

