package mt.exceptions;

public class MTParseException
        extends MTException {

    private final int line;

    private final int column;

    public MTParseException(
            String message,
            int line,
            int column) {

        super(message);

        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
