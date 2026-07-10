package mt.ast;

import mt.runtime.MTSymbol;

public class MTVariableNode
        extends MTNode {

    private final MTSymbol name;

    public MTVariableNode(
            MTSymbol name) {

        this.name = name;
    }

    public MTSymbol getName() {

        return name;
    }
}
