/* resultat attendu : 120 */
[
    | result |

    result := 1.

    1 to: 5 do: [:i |
        result := result * i
    ].

    result
] value
