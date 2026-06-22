# Langage MiniTalk

## Vue d’ensemble

MiniTalk est un langage objet dynamique inspiré de Smalltalk.

Tout est message : on manipule des objets, auxquels on envoie des messages.



## Instructions

Les instructions se terminent par un point `.`.

```smalltalk
1 + 2.
name := 'Andy'.
```

## Commentaires

Les commentaires sont délimités par des doubles guillemets.

```smalltalk
" ceci est un commentaire "
```

## Affectation

L’affectation se fait avec `:=`, ou `<-`, au choix.

```smalltalk
x := 10.
p <- 19.0 + 23.
```

## Messages

### Messages unaires

Ce sont des messages qui se suffisent à eux-mêmes.

Les messages unaires suivent le pattern BNF : [a-z][a-zA-Z0-9_]* 

traduction : un caractère alphabétique en minuscule, suivi de 0 ou plus caractères alphanumériques de casse indifférentes.

```smalltalk
obj name.
```

### Messages binaires

```smalltalk
1 + 2.
3 > 1.
```

### Messages keyword

Ce sont des messages qui réclament la présence d'un argument et peuvent être composés.

Les messages keyword suivant la formule BNF : [a-z][[a-zA-Z0-9_]*':'.

traduction : un caractère alphabétique en minuscule, suivi de 0 ou plus caractères alphanumériques de casse indifférentes, et se termine par deux point.

```smalltalk
person name: 'Andy'.
code = 0 ifTrue: [ 'OK' ] ifFalse: [ 'KO' ].
```

## Classes

Les classes sont des objets spéciaux.

```smalltalk
Person := Class new: 'Person'.
Person addInstVar: 'name'.
Person addClassVar: 'species'.
```

On peut y définir des variables et de méthode d'instances
qui ne pourront être utilisé qu'avec une instance pour receveur, 
et des variable et méthodes de classe qui ne seront utilisables
qu'avec la classe pour receveur.

### variables

#### Variables d’instance

On ajoute une variable d'instance grâce au mot-clé :  'addInstVar:' que l'on 
fera suivre d'une chaine de caractères correspondant au nom de la variable à
créer.

Dans la classe est alors créé automatiquement un getter avec le même 
identifiant que la variable, et un setter qui a pour identifiant
l'identifiant de la variable suivi de ':'.

le getter permet d'obtenir le contenu de la variable, le setter, de le
mettre à jour.

```smalltalk
Person addInstVar: 'age'.
p := Person new.
p age: 36.
p age.

```

#### Variables de classe

On ajoute une variable d'instance grâce au mot-clé :  'addInstVar:' que l'on 
fera suivre d'une chaine de caractères correspondant au nom de la variable à
créer.

Comme pour la variable d'instance, un getter et un setter sont automatiquement
créés en s'appuyant sur l'identifant de la variable.

```smalltalk
Person addClassVar: 'species'.
Person species: 'Homo sapiens'.
Person species.
```

### Méthodes

Comme pour les variables, on peut ajouter des méthodes pour manipuler
le contenu d'une classe ou celui de ses instances.

#### Méthodes d’instance

Les méthodes d'instances sont utilisées pour définir des fonctions
qui vont manipuler le contenu des instances.

On peut définir plusieurs types de méthodes : des méthodes unaires,
des opérateurs, des méthodes binaires et même des méthodes traitant
plusieurs arguments à la suite

un exemple de méthode unaire :

```smalltalk
Person addMethod: 'birthday' with: [
  self age: self age + 1.
  self age
].
p <- Person new.
p age: 34.
p birthday.
```

un exemple de méthode avec un mot clé composite attendant deux arguments :
```smalltalk
Person addMethod: 'setName:age:' with: [ :n :a |
  self name: n.
  self age: a.
  self
].

p <- Person new.
p setName: 'Laurent' age: 34.
```
on remarque que le mot clé est en fait un assemblage de deux mot clé, et que
le bloc définissant la méthode est en fait une 'closure' qui comprend
en entrée deux variables sur lesquelles seront mappés
les argments passés à la méthodes.

#### Méthodes de classe

Même chose que les méthodes d'instance, mais celle-ci s'appliquent aux classes.

```smalltalk
Person addClassMethod: 'speciesName' with: [
  self species
].
Person speciesName.
```

### Héritage et `super`

```smalltalk
A := Class new: 'A'.
A addMethod: 'hello' with: [ 'A' ].

B := A subclassNamed: 'B'.
B addMethod: 'hello' with: [ super hello , ' -> B' ].
```

## Blocs et closures

Les blocs sont délimités par `[` et `]`.

```smalltalk
[ :x | x + 1 ] value: 5.
```

Les blocs peuvent contenir plusieurs instructions.

```smalltalk
[
  x := 3.
  x + 1
] value.
```

## Retour non local `^`

```smalltalk
[
  1 + 2.
  ^99.
  123
] value.
```

## Contrôle de flux

### Conditions

```smalltalk
cond ifTrue: [ ... ].
cond ifFalse: [ ... ].
cond ifTrue: [ ... ] ifFalse: [ ... ].
```

### Boucles

```smalltalk
[ i < 10 ] whileTrue: [ i := i + 1 ].
[ done ] whileFalse: [ ... ].
```

### Répétitions

```smalltalk
5 timesRepeat: [ ... ].
```

## Tableaux

### Littéral

```smalltalk
#(1 2 3 4)
```

### Accès

```smalltalk
a at: 1.
a at: 2 put: 99.
```

L’indexation est basée sur 1.

## Nombres

MiniTalk supporte les entiers et les réels.

```smalltalk
1 + 2.
1 + 2.5.
```

## Chaînes

Les chaînes sont délimitées par des quotes simples.

```smalltalk
'Bonjour'.
'It's working'.
```
