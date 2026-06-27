# Langage MiniTalk

## Vue d’ensemble

MiniTalk est un langage objet dynamique inspiré de Smalltalk.

Dans miniTalk, tout est objets et messages : on manipule des objets,
auxquels on envoie des messages.

Même les structures de contrôles de flux sont en fait des messages que
l'on envoie à des booléens, des entiers, des nombres réels, des blocs
d'instructions.

## Instructions

Toute instruction se termine par un point : `.`

Une exception : `.` est optionnel pour la dernière instruction
d'un bloc.

```smalltalk
1 + 2.
name := 'Andy'.
[ 3 * 8 ].
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

Les messages keyword ou à mots-clés suivent la formule, en notation BNF :
[a-z][[a-zA-Z0-9_]*':'.

==traduction :== un caractère alphabétique en minuscule, suivi de 0 ou plus caractères alphanumériques de casse indifférentes ou underscore `_`, et se termine par deux point.

```smalltalk
person name: 'Andy'.
code = 0 ifTrue: [ 'OK' ] ifFalse: [ 'KO' ].
```

Il est possible de définir une cascade de messages à mots-clés.
Nous verrons cela dans le chapitre des méthodes.

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

le getter permet d'obtenir le contenu de la variable,
le setter, de le mettre à jour.

```smalltalk
Person addInstVar: 'age'.
p := Person new.
p age: 36.
p age.

```

#### Variables de classe

On ajoute une variable de classe grâce au mot-clé :
'addInstVar:' que l'on fera suivre d'une chaine de
caractères correspondant au nom de la variable à
créer.

Comme pour la variable d'instance, un getter et un setter
sont automatiquement créés en s'appuyant sur l'identifant
de la variable.

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

Une méthode unaire n'a pas besoin d'arguments. Elle
est généralement utilisée pour accéder à une variables,
ou pour implémenter une fonction sous la forme d'une
succession d'instructions n'ayant pas besoin d'arguments.

exemple :

```smalltalk
Person addMethod: 'birthday' with: [
  self age: self age + 1.
  self age
].
p <- Person new.
p age: 34.
p birthday.
```


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
1. le mot clé pour cette méthode est ici un assemblage
   de deux mots clés, car la méthode attends deux arguments
2. le bloc définissant le corps de la méthode, quant à lui,
   est ce qu'on appelle une 'closure'. Nous verrons plus
   loin ce qu'est une closure.

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

explications : la méthode d'instance "b hello" fait appel à
la méthode hello de l'instance parente, qui affiche 'A',
et concatène la chaine obtenue avec la chaîne ' -> B'. 

## Blocs et closures

### les Blocks
Les blocs sont des successions d'instructions entourées
de `[` et `]`.

Les blocs peuvent donc contenir plusieurs instructions.

```smalltalk
[
  x := 3.
  x + 1
] value.
```

### les Closures

Ce sont des blocs spéciaux au début desquels sont déclarées
une ou plusieurs variables, en plus des instructions, variables sur
lesquelles les traitements du bloc agiront.

Les variables d'une closures sont déclarées en début de closure,
chacune précédée d'un caractère `:`. Elles sont séparées de la
partie code du bloc au moyen d'un caractère pip : `|`.

Lors de son utilisation, on fera suivre le block d'une
succession de "value: <valeur>" en autant de fois que
de variables déclarées dans l'en-tête de la closure.

Une closure est donc en fait une fonction anonyme dont on ne
contrôle pas les identifiants.

exemple de closure avec un seul paramètre :
```smalltalk
[ :x | x + 1 ] value: 5.
```

exemple de closure avec plusieurs paramètres :
```smalltalk
fct := [ :x :y | x * y + 10 ] 
fct value: 10 value: 5.
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

On dispose dans miniTalk de l'équivalent du 
if ... then ... else qu'on retrouve dans d'autres
langages.

1. exécution d'un bloc si la condition est vraie :
```smalltalk
cond ifTrue: [ ... ].
```

2. exécution d'un bloc si la condition est fausse :
```smalltalk
cond ifFalse: [ ... ].
```

3. exécution du premier bloc si la condition est
   vraie, et du second bloc si elle est fausse :

```smalltalk
cond ifTrue: [ ... ] ifFalse: [ ... ].
cond ifTrue: [ ... ] else: [ ... ].
```

4. exécution du premier bloc si la condition est
   fausse, et du second bloc si elle est vraie :
   
```smalltalk
cond ifFalse: [ ... ] ifTrue: [ ... ].
cond ifFalse: [ ... ] else: [ ... ].
```

### Boucles

`whileTrue:` et `whileFalse:` sont des mots-clés dont le receveur
est obligatoirement un bloc qui doit avoir pour valeur
true ou false.

1. **[ cond ] whileTrue: [ block ].** :
   applique le bloc en argument tant que le
   bloc booléen receveur est vrai.

2. **[ cond ] whileFalse:" [ block ].** :
   est l'équivalent du "repeat ... until(cond);" dans des langages
   comme le pascal : on répète l'exécution du bloc, jusqu'à ce que
   le bloc de condition receveur soit vrai.

3. **[ block ] repeatWhile: [ cond ].** :
   exécute le bloc au moins une fois, et en répéte l'exécution
   tant que le bloc de condition est vrai.
   C'est l'équivalent, en Java, de la boucle :
   
   ```java
   do {
     body()
   } while (condition())

   ```

4. **[ block ] repeatUntil: [ cond ].** :
   exécute le bloc au moins une fois, et en répéte l'exécution
   jusqu'à ce que le bloc de condition soit vrai.
   C'est l'équivalent, en Java, de la boucle :
   
   ```java
   do {
     body()
   } until (condition())

   ```
   
Exemples :
```smalltalk
i := 0
[ i < 10 ] whileTrue: [ i := i + 1 ].

i := 0.
[ i > 10 ] whileFalse: [ i := i + 1 ].

i := 0.
[
    i := i + 1.
    i printString.
] repeatWhile: [ i < 5 ].

i := 0.
[
    i := i + 1.
    i printString.
] repeatUntil: [ i >= 5 ].
```

### Répétitions

Vous pouvez exécuter une boucle n fois.
Le receveur est obligatoirement un entier

```smalltalk
5 timesRepeat: [ ... ].
```

Pour travailler avec l'index, utiliser le mot clé 
`timesRepeatWithIndex:`, qui permet d'accéder à la valeur du compteur
au sein d'une closure.

==Attention :== le compteur commence à 0 et se termine à n - 1.

```smalltak
5 timesRepeatWithIndex: [ :i | System print: i printString ].
```


Ou vous pouvez également faire un calcul sur l'itérateur :

```smalltalk
1 to: 25 do: [ ... ].
1 to: 25 step: 4 do: [ ... ].
```

il est possible d'utiliser des réels au lieu d'entier, mais
dans ce cas, il faut obligatoirement préciser le step :

```smalltalk
1.0 to 3.5 step 0.75 do: [ ... ].
```

Vous pouvez récupérer la valeur de l'itérateur
en utilisant une closure :
```smalltalk
1 to: 25 step: 4 do: [ :i | 
   s <- i printString.
   'indice ', s
].
```

## Les classes prédéfinies

### Tableaux

#### Littéral

On peut définir un tableau de valeur in extenso dès sa création.
```smalltalk
#(1 2 3 4)
```

#### Accès

On peut déclarer un nouveau tableau en lui précisant la taille
puis initialiser chaque élément un à un.

```smalltalk
a := Array new: 7.
a at: 2 put: 99.
```
Pour accéder à la valeur d'un élément du tableau à l'indice i, on
ecrit :
```smalltalk
a at: i.
```

Les valeurs non renseignées sont obligatoirement à `nil`.

L’indexation des tableaux commence toujours à 1 et non à 0 comme
en C ou en Java.

## Nombres

MiniTalk supporte les entiers et les réels.

```smalltalk
1 + 2.
1 + 2.5.
```

Les nombres supportent toutes sortes d'oprateurs :
- l'addition et la soustraction : `+`et `-`
- la multiplication et la division : `*` et `/`
- la division entière, le modulo : `//` et `%`

Dans miniTalk, il n'y a pas de notion de précédence implicite
des opérateurs : il faut employer des parenthèses 
(oui, c'est un peu rustique ... d'un autre côté ce langage
fait déjà beaucoup de choses pour un langage expérimental, non ? ).

## Chaînes

Les chaînes sont délimitées par des quotes simples.

Si vous souhaitez employer une quote dans votre chaine, il
faut la définir avec une séquece d'échappement : `\'`
```smalltalk
'Bonjour'.
'It\'s working'.
```

le backslash indque que le caractère qui suit doit être interprété
comme un caractère normal de chaine, et non comme un élément du
langage.

Ne vous laissez pas submerger en voyant le nombre de méthodes
disponibles pour les chaînes de caractères : miniTalk ayant été
conçu comme un langage de script, il fallait accorder une attention
toute particulière aux possibilités de traitements des chaînes de
caractères, afin  de rendre le plus de services possibles pour
l'analyse des fichiers textes.

Les opérateurs sur les chaines sont :

1. `,` : permet de concaténer deux chaînes.
   ```smalltalk
   c <- 'une chaine ' , 'concaténée.'.
   → 'une chaine concaténée.'
   ```
   
2. `*` : crée une nouvelle chaine contenant n fois le receveur. cet
   opérateur est obligatoirement suivi d'un entier.
   ```smalltak
   s <- 'say '
   s * 3
   → say say say 
   ```
   
3. `asArray` : transforme une chaîne en tableaux de lettres :

   ```smalltalk
   a <- 'une chaine' asArray.
   → #('u' 'n' 'e' ' ' 'c' 'h' 'a' 'i' 'n' 'e')
   ```

4. `charsDo:` : applique une closure à chaque caractère d'une
    chaine.
   
   C’est un comportement utile à connaître : charsDo: ne retourne pas le receveur, mais la dernière valeur produite par le bloc.
   
   Voici quelques exemples d'utilisation :

   ```smalltak
   'une chaine' charsDo: [ :c |
     System print: c.
    ].
    → u
    n
    e
    
    c
    h
    a
    i
    n
    e
   ```
   permet d'afficher ligne à ligne chaque caractère de la chaine.
   
   ```smalltak
   result := ''.
   first := true.

   'abc' charsDo: [ :ch |
      first
          ifTrue: [
              result := ch.
              first := false
          ]
          ifFalse: [
              result := result , '-' , ch
          ]
   ].

   System print: result.
   → a-b-c
   ```

5. `toUpper` : permet de mettre toute la chaine en majuscules

6. `toLower` : permet de mettre toute la chaine en minuscules.

7. `isEmpty` : indique si une chaine est vide

8. `at:` : permet d'accéder à un caractère de la chaîne.

9. `copyFrom:to:` : permet d'extraine une sous-chaîne, borne incluses.

   ```smalltalk
   a <- 'une chaine moyenne' copyFrom: 5 to: 10.
   → 'chaine'
   ```

10. `substringTo:` : extrait une sous-chaine commençant à la position
   au début de la chaine initiale, et se terminant à la position
   indiquée, celle-ci inclue.
   
   ```smalltalk
   '123456789' substringTo: 5.
   → '12345'
   ```
   
11. `substringFrom:` : extrait une sous-chaine commençant à la position
   indiquée jusqu'à la fin de la chaine initiale.
   
   ```smalltalk
   '123456789' substringFrom: 5.
   → '56789'
   ```
   
12. `trim`, `trimLeft`, `trimRight` : la première méthode supprime tous 
   les caractères espaces d'une chaîne, la seconde, tous les caractères
   espaces en début de chaines, et la dernière tous les caractères
   espaces en fin de chaîne.

13. `startsWith:`, 'endsWith:` : teste si une chaine commence ou
   se termine avec une sous-chaine donnée.
   
   ```smalltalk   
   'hello' startsWith: 'he'   → true
   'hello' endsWith: 'lo'     → true
   ```
   
14. `indexOf:` : indique la position dans la chaine de la première
   occurence d'un caractère ou d'une sous-chaine.
   ```smalltalk
   a <- 'une tres longue chaine.'.
   → une tres longue chaine.
   
   a indexOf: 'longue'.
   → 10
   
   a indexOf: 'e'.
   → 3
   ```
 
15. `includes:` : indique si la chaine indiquée en paramètre
   est contenue dans le receveur. Cette
   méthode retourne un booléen.
 
16. `split:` : crée un tableaux avec les sous-chaines résultant
   du découpage de la chaîne d'origine avec la chaine fournie en
   paramètre.
   
   exemples :
   ```smalltak
   a <- 'une tres longue chaine'.
   a split: ' longue '.
   → ['une tres', 'chaine' ]
   ```
   
   typiquement, cette méthode rend possible le découpage d'une
   ligne de fichier CSV en ses éléments, en utilisant le séparateur
   comme élément de découpage.
 
17. `lines` : autorise le parsing d'un fichier texte ligne par ligne.
 
18. `isWhitespace`, `isDigit`, `isLetter`, `isLetterOrDigit` :
   permet la classification d'un caractère (et non d'une chaîne).
 
19. `asInteger`, `asFloat` : permet de traduire une chaine en entier
   ou réel. C'est l'équivalent du 'toInt()' ou 'toFloat()' du C ou du
   Java
   
   ```smalltalk
   a <- '2981' asInteger.
   → 2981
   a <- '12.87e-5' asFloat.
   → 0.000012870000000000000037821308584984336675915983505547046661376953125
   ```
   
   ==Note :== le fait d'avoir des décimales flottantes supplémentaires
   est normal : il provient de la représentation interne, en format
   binaire par le processeur, du nombre décimal.
   
20. `takeWhile:`, `dropWhile:` : la première méthode  lit une chaine 
   de caractères tant qu'une condition est vraie. La seconde laisse
   de coté les caractères tant que la condition est vraie.
   
   ```smalltalk
   digits := '123abc' takeWhile: [:ch | ch isDigit].
   System print: digits.
   → '123'
   
   
   rest := '   hello' dropWhile: [:ch | ch isWhitespace].
   System print: rest.
   → 'hello'
   ```
 
21. `readUntil:` : cette méthode permet de lire une chaîne jusqu'à 
   un séparateur particulier.
   
   ```smalltalk
   name := 'title:bonjour' readUntil: [:ch | ch = ':' ].
   System print: name.
   → 'title'
   ```

22. `matches:` : teste si la chaine receveur suit entèrement
   l'expression régulière donnée par la chaine en paramètre
   
   ```smalltak
   'abc123' matches: '[a-z]+[0-9]+'.
   → true
   ```
   
23. `containsMatch:` : teste si la chaine receveur contient une
   sous-chaine qui respecte l'expression régulière fournie en
   paramètre.
   
   ```smalltak
   'hello123world' containsMatch: '[0-9]+'.
   → true
   ```
   
24. `allMatches:`: retourne un tableau contenant toutes
     les sous-chaines de la chaine receveur respectant 
     l'expression régulière fournie
   
   ```smalltalk
   'abc123def456' allMatches: '[0-9]+'
   → [123, 456]
   ```
### les fonctions magiques sur les chaînes

Ci-après, les méthodes un peu "magiques" offertes par le langage.
Si vous n'en comprenez pas immédiatement l'usage, ce n'est pas
grave : regardez les exemples, et expérimentez par vous-même. Vous
finirez bien par trouver un usage à ces fonctions :)

1. `collectChars:`: applique une closure à chaque caractères de la
    chaine de départ et produit un array avec chaque résultat
    
    ```smalltalk
    'abc' collectChars: [ :c | c , c ]
    → #('aa' 'bb' 'cc')

    
    'abc' collectChars: [ :c | c toUpper ]
    → #('A' 'B' 'C')
    
    'abc' collectChars: [ :c | c asInteger ]
    → #(97 98 99)

    ```

2. `selectChars:` : retourne une chaine dans laquelle seuls
   restent les caractères ayant satisfait les conditions de la
   closure.
   
   ```smalltak
   "ici on ne garde que les voyelles"
   bonjour' selectChars: [ :c |
     c = 'a' or: [
     c = 'e' or: [
     c = 'i' or: [
     c = 'o' or: [
     c = 'u' ]]]]
   ]
   → 'ouo'
   
   "ici on ne garde que les chiffres"
   'a1b2c3' selectChars: [ :c | c isDigit ]
   → '123'

   ```

3. ``rejectChars:`` : c'est la fonction inverse de la précédente :
   on ne conserve dans la chaine finale que les lettres n'ayant
   pas satisfait les traitements de la closure.
   
   ```smalltalk
   "on retire tous les espaces de la chaine"
   'hello world' rejectChars: [ :c | c = ' ' ]
   → 'helloworld'
   ```
   
3. `reduce:with:` : permet d'appliquer un traitement itératif sur
   chaque caractère d'une chaine pour produire un nouvel objet.
   
   `reduce:` est suivi de la valeur initiale pour le nouvel objet.
   
   `with:`est suivi de la closure de traitement qui va mettre à jour
   le nouvel object en fonction de la valeur de chaque caractère.
   
   ```smalltak   
   'abc' reduce: 0 with: [ :acc :l | acc + 1 ]
   → 3
   ```
   
   dans l'exemple précédent, acc est initialisé à 0, et est incrémenté
   pour chaque lettre l de la chaine 'abc'.
   