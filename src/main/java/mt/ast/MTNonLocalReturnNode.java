package mt.ast;

public class MTNonLocalReturnNode
        extends MTNode {

    private final MTNode expression;

    public MTNonLocalReturnNode(
            MTNode expression) {

        this.expression =
                expression;
    }

    public MTNode getExpression() {

        return expression;
    }
}
