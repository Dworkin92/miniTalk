package mt.ast;

public sealed interface MTExpr permits
        MTLiteral,
        MTVariableRef,
        MTAssignment,
        MTMessageSend,
        MTCascade,
        MTSequence,
        MTArrayLiteral,
        MTBlock,
        MTReturnExpr {
}
