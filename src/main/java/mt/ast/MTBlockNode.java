package mt.ast;

import mt.runtime.MTArray;

public class MTBlockNode
        extends MTNode {

    private final MTArray parameters;

    private final MTSequenceNode body;

    public MTBlockNode(
            MTArray parameters,
            MTSequenceNode body) {

        this.parameters =
                parameters;

        this.body =
                body;
    }

    public MTArray getParameters() {

        return parameters;
    }

    public MTSequenceNode getBody() {

        return body;
    }
}
