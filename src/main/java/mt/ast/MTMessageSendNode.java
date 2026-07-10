package mt.ast;

import mt.runtime.MTSymbol;

public class MTMessageSendNode
        extends MTNode {

    private final MTNode receiver;

    private final MTSymbol selector;

    private final MTArrayNode arguments;

    public MTMessageSendNode(
            MTNode receiver,
            MTSymbol selector,
            MTArrayNode arguments) {

        this.receiver =
                receiver;

        this.selector =
                selector;

        this.arguments =
                arguments;
    }

    public MTNode getReceiver() {

        return receiver;
    }

    public MTSymbol getSelector() {

        return selector;
    }

    public MTArrayNode getArguments() {

        return arguments;
    }
}
