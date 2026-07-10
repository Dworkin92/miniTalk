package mt.ast;

public class MTIntegerLiteralNode
        extends MTNode {

    private final long value;

    public MTIntegerLiteralNode(
            long value) {

        this.value = value;
    }

    public long getValue() {

        return value;
    }
}
