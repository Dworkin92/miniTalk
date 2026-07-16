/* resultat attendu : 90 */
[
    | factor |

    factor := 3.

    (
        [:x |

            [:y |

                (x + y) * factor

            ]

        ] value: 10

    ) value: 20

] value