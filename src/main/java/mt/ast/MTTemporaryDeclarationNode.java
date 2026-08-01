package mt.ast;

import mt.runtime.MTArray;

public final class MTTemporaryDeclarationNode
    extends MTNode {

    private final MTArray temporaries;

    public MTTemporaryDeclarationNode(
            MTArray temporaries) {

        this.temporaries = temporaries;
    }

    public MTArray getTemporaries() {
        return temporaries;
    }
}
