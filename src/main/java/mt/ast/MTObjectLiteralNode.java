package mt.ast;

import mt.runtime.MTObject;

public class MTObjectLiteralNode
        extends MTNode {

    private final MTObject object;

    public MTObjectLiteralNode(
            MTObject object) {

        this.object = object;
    }

    public MTObject getObject() {

        return object;
    }
}
