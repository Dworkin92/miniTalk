package mt.ast;

import java.util.List;

public record MTCascadeMessage(
        String selector,
        List<MTExpr> args
) {
}
