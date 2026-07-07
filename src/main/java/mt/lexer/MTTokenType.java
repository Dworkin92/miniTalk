package mt.lexer;

public enum MTTokenType {

    EOF,

    IDENTIFIER,
    INTEGER,
    STRING,

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

    LCOMMENT,          // /*
    RCOMMENT,          // */

    META,              // @module, @import, ...

    RETURN,            // ^

    BINARY_SELECTOR
}
