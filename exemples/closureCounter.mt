/* resultat attendu : 3 */
[
    | counter increment |

    counter := 0.

    increment := [

        counter := counter + 1.
        counter

    ].

    increment value.
    increment value.
    increment value.

] value
