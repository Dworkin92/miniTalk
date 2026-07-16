/* resultat attendu : 15 */
[
    | sum |

    sum := 0.

    5 to: 1 do: [:i |
        sum := sum + i
    ].

    sum
] value
