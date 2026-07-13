package mt.ast;

public final class MTBooleanLiteralNode
        extends MTNode {

    private final boolean value;

    public MTBooleanLiteralNode(
            boolean value) {

        this.value = value;
    }

    public boolean getValue() {

        return value;
    }
}
