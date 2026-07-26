package mt.ast;

public final class MTMetaDirectiveNode
        extends MTNode {

    private final String text;

    public MTMetaDirectiveNode(
            String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
