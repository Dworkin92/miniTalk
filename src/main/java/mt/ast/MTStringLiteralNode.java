package mt.ast;

public final class MTStringLiteralNode
        extends MTNode {

    private final String value;

    public MTStringLiteralNode(
            String value) {

        this.value = value;
    }

    public String getValue() {

        return value;
    }
}
