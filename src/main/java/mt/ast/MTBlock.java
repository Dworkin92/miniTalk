package mt.ast;

import java.util.List;

public record MTBlock(List<String> params, MTExpr body) implements MTExpr {
}