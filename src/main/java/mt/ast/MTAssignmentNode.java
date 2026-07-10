package mt.ast;

import mt.runtime.MTSymbol;

public class MTAssignmentNode
        extends MTNode {

    private final MTSymbol variable;

    private final MTNode value;

    public MTAssignmentNode(
            MTSymbol variable,
            MTNode value) {

        this.variable =
                variable;

        this.value =
                value;
    }

    public MTSymbol getVariable() {

        return variable;
    }

    public MTNode getValue() {

        return value;
    }
}
