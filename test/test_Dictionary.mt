"@module test_dictionary"


d := Dictionary new.

d put: 'a' value: 1.
d put: 'b' value: 2.

System print: (d at: 'a').      "→ 1"
System print: (d containsKey: 'b'). "→ true"
System print: d.


d := Dictionary new.
d put: 'x' value: #(1 2 3).
d put: 'f' value: [ :n | n + 1 ].

System print: (d at: 'x').
System print: (d at: 'f').


f := d at: 'f'.
System print: (f value: 5).
