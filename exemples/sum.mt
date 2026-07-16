/* resultat attendu : 55 */
[
    | sum |

    sum := 0.

    1 to: 10 do: [:i |
        sum := sum + i
    ].

    sum
] value
