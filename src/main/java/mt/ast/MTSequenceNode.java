package mt.ast;

import java.util.ArrayList;
import java.util.List;

public class MTSequenceNode
        extends MTNode {

    private final List<MTNode> statements =
            new ArrayList<>();

    public void add(
            MTNode node) {

        statements.add(node);
    }

    public List<MTNode> getStatements() {

        return statements;
    }
}
