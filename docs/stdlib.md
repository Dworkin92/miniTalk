# Bibliothèque standard

## Principe

La bibliothèque standard est chargée au démarrage depuis `stdlib/stdlib.mt`.

Ce fichier peut lui-même charger d’autres fichiers de la stdlib.

Exemple de `stdlib.mt` :

```smalltalk
System load: 'array'.
```

## Organisation conseillée

```text
stdlib/
  stdlib.mt
  array.mt
  process.mt
  file.mt
```

## Fonctions sur les tableaux

MiniTalk fournit des opérations fonctionnelles sur les tableaux.

### Itération

```smalltalk
#(1 2 3) do: [ :x | x ].
```

### Transformation

```smalltalk
#(1 2 3) collect: [ :x | x * 2 ].
```

### Filtrage

```smalltalk
#(1 2 3 4) select: [ :x | x > 2 ].
#(1 2 3 4) reject: [ :x | x > 2 ].
```

### Réduction

```smalltalk
#(1 2 3 4) inject: 0 into: [ :acc :x | acc + x ].
```

## Helpers de haut niveau

Exemple d’extension dans `array.mt` :

```smalltalk
Array addMethod: 'sum' with: [
  self inject: 0 into: [ :acc :x | acc + x ]
].

Array addMethod: 'max' with: [
  self size = 0 ifTrue: [ ^ nil ].
  self inject: (self at: 1) into: [ :acc :x |
    (x > acc) ifTrue: [ x ] else: [ acc ]
  ]
].
```

## Chargement explicite

Le chargement d’un fichier de bibliothèque se fait avec :

```smalltalk
System load: 'array'.
```
