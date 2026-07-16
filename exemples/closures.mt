/* resultat attendu : 60 */
[
    | factor doubler tripler |

    factor := 2.

    doubler := [:x |
        x * factor
    ].

    factor := 3.

    tripler := [:x |
        x * factor
    ].

    (doubler value: 10) + (tripler value: 10)

] value