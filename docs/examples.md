# Exemples MiniTalk

## Somme d’un tableau

```smalltalk
#(1 2 3 4) inject: 0 into: [ :acc :x | acc + x ].
```

## Définir une classe

```smalltalk
Person := Class new: 'Person'.
Person addInstVar: 'name'.
Person addInstVar: 'age'.

p := Person new.
p name: 'Andy'.
p age: 36.
```

## Héritage

```smalltalk
A := Class new: 'A'.
A addMethod: 'hello' with: [ 'A' ].

B := A subclassNamed: 'B'.
B addMethod: 'hello' with: [ super hello , ' -> B' ].
```

## Boucle

```smalltalk
i := 0.
[ i < 5 ] whileTrue: [ i := i + 1 ].
i.
i := 0.
[ i > 4 ] whileFalse: [ i := i + 1 ].
i.
```

## Répétition

```smalltalk
i := 0.
5 timesRepeat: [ i := i + 1 ].
i.

i := 0.
1 to: 5 do: [ i := i + 1 ].
i.

i := 0.
1 to: 20 step: 5 do: [ i := i + 2 ].
i.
```

## Processus

```smalltalk
p := Process shell: 'echo Bonjour'.
code := p wait.
code = 0 ifTrue: [ p stdout ] ifFalse: [ p stderr ].
```

## Fichiers

```smalltalk
f := File open: 'test.txt'.
content := f readAll.
f close.
```
