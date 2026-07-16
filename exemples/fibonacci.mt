/* resultat attendu : 55 */
[
    | a b temp |

    a := 0.
    b := 1.

    1 to: 10 do: [:i |

        temp := b.
        b := a + b.
        a := temp.
    ].

    a
] value
