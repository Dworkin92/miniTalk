# Langage MiniTalk

## Vue d’ensemble

MiniTalk est un langage objet dynamique inspiré de Smalltalk.

Tout est message : on manipule des objets, auxquels on envoie des messages.



## Instructions

Toute instruction se termine par un point `.`.

Une exception : le point `.` est optionnel pour la dernière instruction
d'un bloc.

```smalltalk
1 + 2.
name := 'Andy'.
```

## Commentaires

Les commentaires sont des zones de textes délimités par des doubles guillemets.

```smalltalk
" ceci est un commentaire "
```

## Affectation

L’affectation se fait avec `:=`, ou `<-`, au choix. Personnellement je
trouve la seconde alternative plus parlante, mais je comprends que l'on
puisse préférer l'affectation à la mode Pascal.

```smalltalk
x := 10.
p <- 19.0 + 23.
```

## Messages

### Messages unaires

Ce sont des messages qui se suffisent à eux-mêmes et n'ont pas besoin d'arguments.

Les messages unaires suivent le pattern, en notation BNF : [a-z][a-zA-Z0-9_]* 

==traduction :== un caractère alphabétique en minuscule, suivi de 0 ou plus caractères alphanumériques de casse indifférentes ou de caractères `_` (prononcez 'underscore', ou 'souligné', et non 'tiret-du-8', SVP).

```smalltalk
obj name.
```

### Messages binaires

Ce sont, typiquement les opérateurs qui demandent 2 opérandes : le receveur et une valeur en argument : 
- `+`, `-`, `*`, `/`, `%`, `//` pour les opérations
- `<`, `<=`, `>`, `>=`, `=`, `!=` ou `<>` pour les comparaisons
- ',' qui permet de concaténer deux chaines de caractères

```smalltalk
1 + 2.
3 > 1.
```

==Note :== 
* `%` correspond au modulo, le reste
   de la division entière entre deux opérandes
* `//` correspond à la division entière entre les deux
  opérandes
  
### Messages keyword

Ce sont des messages qui réclament la présence d'un argument et peuvent être composés.

Les messages keyword ou à mots-clés suivant la formule, en notation BNF :
[a-z][[a-zA-Z0-9_]*':'.

==traduction :== un caractère alphabétique en minuscule, suivi de 0 ou plus caractères alphanumériques de casse indifférentes ou underscore `_`, et se termine par deux point.

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

Dans la classe est alors créé automatiquement un getter
avec le même  identifiant que la variable,
et un setter qui a pour identifiant
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

On peut définir plusieurs types de méthodes :
* des méthodes unaires,
* des opérateurs,
* des méthodes binaires et
* des méthodes traitant plusieurs arguments à la suite

==exemple de méthode unaire :==

```smalltalk
Person addMethod: 'birthday' with: [
  self age: self age + 1.
  self age
].
p <- Person new.
p age: 34.
p birthday.
```

on remarquera que le bloc composant le corps de la
méthodes est un bloc "simple" qui ne comporte qu'une
série d'instructions.

==exemple de méthode avec un mot clé composite attendant deux arguments :==
```smalltalk
Person addMethod: 'setName:age:' with: [ :n :a |
  self name: n.
  self age: a.
  self
].

p <- Person new.
p setName: 'Laurent' age: 34.
```
Deux points sont à noter :
1. le mot clé est en fait un assemblage de deux mots clés,
2. le bloc définissant le corps de la méthode est en fait
   ce qu'on appelle une 'closure'. Nous verrons plus
   loin ce qu'une une closure.

#### Méthodes de classe

Même chose que les méthodes d'instance, mais celle-ci s'appliquent aux classes.

```smalltalk
Person addClassMethod: 'speciesName' with: [
  self species
].
Person speciesName.
```

### Héritage et `super`

Dans une méthode de classe ou d'instance, le mot-clé 
`super` désigne l'objet parent dont descend l'objet
courant. Cela permet d'appeler explitement des 
méthodes qui sont surchargées dans la class ou l'objet
courant.

```smalltalk
A := Class new: 'A'.
A addMethod: 'hello' with: [ 'A' ].

B := A subclassNamed: 'B'.
B addMethod: 'hello' with: [ super hello , ' -> B' ].

b <- B new.
b hello.
```

affichera : "A -> B".

explications : b hello fait appel à la méthode hello
de l'instance parent, qui affiche 'A', et concatène cette
chaine avec la chaîne ' -> B'. 

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
