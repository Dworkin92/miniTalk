package mt.lexer;

public enum MTTokenType {

    EOF,

    IDENTIFIER,
    INTEGER,
    STRING,
    SYMBOL,

    ASSIGN,            // :=

    NIL,
    TRUE,
    FALSE,
    SUPER,
    SELF,

    DOT,               // .
    SEMICOLON,         // ;

    COLON,             // :
    PIPE,              // |

    LPAREN,            // (
    RPAREN,            // )

    LBRACKET,          // [
    RBRACKET,          // ]

    META_DIRECTIVE,    // @[ meta-directive ]

    RETURN,            // ^

    BINARY_SELECTOR
}
