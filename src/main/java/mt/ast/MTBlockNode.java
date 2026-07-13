package mt.ast;

import mt.runtime.MTArray;

public class MTBlockNode
        extends MTNode {

    private final MTArray parameters;

    private final MTArray temporaries;

    private final MTSequenceNode body;

    public MTBlockNode(
        MTArray parameters,
        MTSequenceNode body) {
        this(
            parameters,
            new MTArray(),
            body);
    }

    public MTBlockNode(
            MTArray parameters,
            MTArray temporaries,
            MTSequenceNode body) {

        this.parameters =
                parameters;

        this.temporaries =
                temporaries;

        this.body =
                body;
    }

    public MTArray getParameters() {

        return parameters;
    }

    public MTArray getTemporaries() {

        return temporaries;
    }

    public MTSequenceNode getBody() {

        return body;
    }
}
