package mt.lexer;

public record MTToken(
        MTTokenType type,
        String text,
        int position
) {
}