package mt.ast;

import java.util.List;

public record MTArrayLiteral(List<MTExpr> elements) implements MTExpr {
}