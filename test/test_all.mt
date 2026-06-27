"========================"
System print: ' MiniTalk Test Suite '.
"========================"

"--- Littéraux ---"
System print: 'hello'.
System print: 123.
System print: true.
System print: false.

"--- Opérations ---"
System print: (1 + 2).
System print: (5 * 3).
System print: (10 - 4).

"--- Variables ---"
x := 10.
y := 20.
System print: (x + y).

"--- Blocs simples ---"
b := [ 'ok' ].
System print: (b value).

"--- Blocs avec paramètre ---"
inc := [ :x | x + 1 ].
System print: (inc value: 5).

"--- Blocs avec 2 paramètres ---"
sum := [ :a :b | a + b ].
System print: (sum value: 3 value: 4).

"--- Tableaux ---"
System print: (#(1 2 3)).

"--- Collection fonctionnelle ---"
System print: (#(1 2 3) collect: [ :x | x + 1 ]).
System print: (#(1 2 3) select: [ :x | x > 1 ]).
System print: (#(1 2 3) inject: 0 into: [ :a :x | a + x ]).

"========================"
System print: ' CLASSES DYNAMIQUES '.
"========================"

c := Class new.

c addMethod: 'hello' with: [
    'hello world'
].

obj := c new.
System print: (obj hello).

"--- Méthode avec paramètre ---"
c addMethod: 'double:' with: [ :x |
    x + x
].
System print: (obj double: 5).

"--- Méthode avec plusieurs arguments ---"
c addMethod: 'sum:with:' with: [ :a :b |
    a + b
].
System print: (obj sum: 3 with: 4).

"========================"
System print: ' CHAÎNAGE '.
"========================"

System print: (1 + 2 + 3).

"========================"
System print: ' HERITAGE ET SUPER '.
"========================"

System print: '--- Création classe A ---'.

A := Class new: 'A'.

A addMethod: 'hello' with: [
  'A'
].

System print: 'Test A:'.
objA := A new.
System print: (objA hello).

System print: '--- Création classe B (sous-classe de A) ---'.

B := A subclassNamed: 'B'.

B addMethod: 'hello' with: [
  super hello , ' -> B'
].

System print: 'Test B:'.
objB := B new.
System print: (objB hello).

System print: '--- Création classe C (sous-classe de B) ---'.

C := B subclassNamed: 'C'.

C addMethod: 'hello' with: [
  super hello , ' -> C'
].

System print: 'Test C:'.
objC := C new.
System print: (objC hello).

"========================"
System print: ' RETURN ^ '.
"========================"

System print: ([ ^42 ] value).

System print: ([
  1 + 2.
  ^99.
  123
] value).

System print: ([
  [ ^42 ] value.
  100
] value).

"========================"
System print: ' Structures de contrôle '.
"========================"

System print: (true not).
System print: (false not).

System print: (false ifTrue: [ 'don\'t print' ]).
System print: (true  ifTrue: [ 'will print' ]).
System print: (false ifTrue: [ 'don\'t print' ] else: [ 'c\'est faux' ]).
System print: (true  ifFalse: [ 'don\'t print' ]).
System print: (false ifFalse: [ 'will print' ]).
System print: (true  ifFalse: [ 'don\'t print' ] else: [ 'c\'est vrai que c\'est faux' ]).

x := 0.
[ x < 3 ] whileTrue: [
  x := x + 1
].
System print: x.

y := 0.
[ y = 3 ] whileFalse: [
  y := y + 1
].
System print: y.

"========================"
System print: ' TEST FINAL '.
"========================"

System print: 'OK - fin des tests'.
