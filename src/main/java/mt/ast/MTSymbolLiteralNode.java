package mt.ast;

import mt.runtime.MTSymbol;

public class MTSymbolLiteralNode
        extends MTNode {

    private final MTSymbol symbol;

    public MTSymbolLiteralNode(
            MTSymbol symbol) {

        this.symbol = symbol;
    }

    public MTSymbol getSymbol() {

        return symbol;
    }
}
