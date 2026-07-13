package mt.ast;

import java.util.ArrayList;
import java.util.List;

public class MTArrayNode
        extends MTNode {

    private final List<MTNode> elements =
            new ArrayList<>();

    public void add(
            MTNode node) {

        elements.add(node);
    }

    public List<MTNode> getElements() {

        return elements;
    }

    public int size() {

        return elements.size();
    }
}
