package mt.ast;

import java.util.List;

public record MTCascade(
        MTExpr receiver,
        List<MTCascadeMessage> messages
) implements MTExpr {
}
