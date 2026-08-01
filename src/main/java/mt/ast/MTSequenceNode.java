package mt.ast;

import java.util.ArrayList;
import java.util.List;

import mt.runtime.MTSymbol;

public class MTSequenceNode extends MTNode {

    private final List<MTSymbol> temporaries = new ArrayList<>();
    private final List<MTNode> statements = new ArrayList<>();


    public void addTemporary(MTSymbol name) {
        temporaries.add(name);
    }

    public List<MTSymbol> getTemporaries() {
        return temporaries;
    }

    public boolean hasTemporaries() {
        return !temporaries.isEmpty();
    }

    public void add(MTNode node) {
        statements.add(node);
    }

    public List<MTNode> getStatements() {
        return statements;
    }
}
