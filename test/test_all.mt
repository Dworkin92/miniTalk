"========================"
" MiniTalk Test Suite "
"========================"

"--- Littéraux ---"
'hello'.
123.
true.
false.

"--- Opérations ---"
1 + 2.
5 * 3.
10 - 4.

"--- Variables ---"
x := 10.
y := 20.
x + y.

"--- Blocs simples ---"
b := [ 'ok' ].
b value.

"--- Blocs avec paramètre ---"
inc := [ :x | x + 1 ].
inc value: 5.

"--- Blocs avec 2 paramètres ---"
sum := [ :a :b | a + b ].
sum value: 3 value: 4.

"--- Tableaux ---"
#(1 2 3).

"--- Collection fonctionnelle (si implémenté) ---"
#(1 2 3) collect: [ :x | x + 1 ].
#(1 2 3) select: [ :x | x > 1 ].
#(1 2 3) inject: 0 into: [ :a :x | a + x ].

"========================"
" CLASSES DYNAMIQUES "
"========================"

c := Class new.

c addMethod: 'hello' with: [
    'hello world'
].

obj := c new.
obj hello.

"--- Méthode avec paramètre ---"

c addMethod: 'double:' with: [ :x |
    x + x
].

obj double: 5.

"--- Méthode avec plusieurs arguments ---"

c addMethod: 'sum:with:' with: [ :a :b |
    a + b
].

obj sum: 3 with: 4.

"========================"
" CHAÎNAGE "
"========================"

1 + 2 + 3.

"========================"
" HERITAGE ET SUPER "
"========================"

'--- Création classe A ---'.

A := Class new: 'A'.

A addMethod: 'hello' with: [
  'A'
].

'Test A:'.
objA := A new.
objA hello.

'--- Création classe B (sous-classe de A) ---'.

B := A subclassNamed: 'B'.

B addMethod: 'hello' with: [
  super hello , ' -> B'
].

'Test B:'.
objB := B new.
objB hello.

'--- Création classe C (sous-classe de B) ---'.

C := B subclassNamed: 'C'.

C addMethod: 'hello' with: [
  super hello , ' -> C'
].

'Test C:'.
objC := C new.
objC hello.


"========================"
" RETURN ^ "
"========================"

[ ^42 ] value.
[
  1 + 2.
  ^99.
  123
] value.
[
  [ ^42 ] value.
  100
] value.

"========================"
" Structures de contrôle "
"========================"
true not.
false not.

false ifTrue: [ 'don\'t print' ].
true  ifTrue: [ 'will print' ].
false ifTrue: [ 'don\'t print' ] else: [ 'c\'est faux' ].
true  ifFalse: [ 'don\'t print' ].
false ifFalse: [ 'will print' ].
true  ifFalse: [ 'don\'t print' ] else: [ 'c\'est vrai que c\'est faux' ].

x := 0.
[ x < 3 ] whileTrue: [
  x := x + 1
].

y := 0.
[ y = 3 ] whileFalse: [
  y := y + 1
].

"========================"
" TEST FINAL "
"========================"

'OK - fin des tests'.

