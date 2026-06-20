package mt.ast;

import java.util.List;

public record MTSequence(List<MTExpr> statements) implements MTExpr {
}
