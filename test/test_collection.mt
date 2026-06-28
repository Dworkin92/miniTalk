"@module test_collection"


"========================"
" TEST COLLECTION "
"========================"

System print: '--- collect/map ---'.


System print: (#(1 2 3) collect: [ :x | x ]).
" attendu: #(1 2 3) "

System print: (#(1 2 3) collect: [ :x | x + 1 ]).
" attendu: #(2 3 4) "

"composition de collectes"
System print: (
    (#(1 2 3)
        collect: [ :x | x + 1 ])
        collect: [ :x | x * 2 ]
).
" attendu: #(4 6 8) "

System print: (#(1 2 3) map: [ :x | x * 2 ]).
" attendu: #(2 4 6) "


System print: '--- select ---'.

System print: (#(1 2 3) select: [ :x | x > 1 ]).
" attendu: #(2 3) "

System print: (#(1 2 3) filter: [ :x | x = 2 ]).
" attendu: #(2) "


System print: '--- reject ---'.

System print: (#(1 2 3) reject: [ :x | x = 2 ]).
" attendu: #(1 3) "


System print: '--- reduce / inject ---'.

System print: (#(1 2 3) inject: 0 into: [ :acc :x | acc + x ]).
" attendu: 6 "

System print: (#(1 2 3) reduce: 1 with: [ :acc :x | acc * x ]).
" attendu: 6 "


System print: '--- composition ---'.

System print: (
    (#(1 2 3 4)
        select: [ :x | x > 2 ])
        collect: [ :x | x * 10 ]
).
" attendu: #(30 40) "


System print: '--- test do: ---'.

sum := 0.
#(1 2 3) do: [ :x |
    sum := sum + x
].
System print: sum.
" attendu: 6 "

System print: '--- test Array ---'.

r := Array new.
System print: (r class).
" attendu: Array "

r add: 1.
r add: 2.
System print: r.
" attendu: #(1 2) "

r := Array new.
#(1 2 3) do: [ :x |
    r add: (x + 1)
].
System print: r.

System print: '--- do + add ---'.

r := Array new.
#(1 2 3) do: [ :x |
    r add: (x + 1)
].
System print: r.
" attendu: #(2 3 4) "


System print: '--- test list ---'.

l := List new.
l add: 1.
l add: 2.
l add: 3.

System print: (l collect: [ :x | x + 5 ]).
" attendu: #(6 7 8) "


System print: '--- test set ---'.

s := Set new.
s add: 1.
s add: 2.
s add: 2.

System print: (s collect: [ :x | x * 10 ]).
" ordre non garanti → #(10 20) "

s do: [ :x | System print: x ].

System print: (s select: [ :x | x > 1 ]).
" attendu: #(2)"

System print: ((s collect: [ :x | x + 1 ]) collect: [ :x | x * 2 ]).
" composition. attendu : #(4 6)"

System print: '--- test dictionnaires ---'.

d := Dictionary new.
d put: 1 value: 10.
d put: 2 value: 20.
System print: d.
" attendu: #{1 -> 10 2 -> 20}"


System print: (d at: 1).
" attendu: 10"
System print: (d at: 3).
" attendu: nil"

d do: [ :k :v | System print: (k + v) ].
" attendu: 11, 22"

System print: (d collect: [ :k :v | v + 1 ]).
" attendu: #{1 -> 11 2 -> 21}"

System print: (d reject: [ :k :v | v = 10 ]).
" attendu #{2 -> 20}"

System print: (d inject: 0 into: [ :acc :k :v | acc + v ]).
" attendu: 30"


System print: (
    (d collect: [ :k :v | v + 1 ])
        select: [ :k :v | v > 15 ]
).
" attendu: #{2 -> 21}"
System print: '--- FIN TEST COLLECTION ---'.
