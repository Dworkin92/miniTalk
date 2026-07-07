package mt.lexer;

public record MTToken(
        MTTokenType type,
        String text,
        int line,
        int column
) {
}
