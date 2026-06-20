package mt.ast;

import java.util.List;

public record MTMessageSend(
        MTExpr receiver,
        String selector,
        List<MTExpr> args
) implements MTExpr {
}
