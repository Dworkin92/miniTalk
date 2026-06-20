package mt.ast;

public record MTAssignment(String name, MTExpr value) implements MTExpr {
}
