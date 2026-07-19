# MiniTalk - Project State

Last update: refactor-v2

## Vision

MiniTalk est un langage de scripting orienté objet inspiré de Smalltalk.

Objectifs :

- syntaxe simple et lisible
- environnement de scripting plus agréable que le shell
- accès progressif à l'écosystème Java
- apprentissage des techniques d'implémentation des langages

## État actuel du projet

### Front-end

Implémenté :

- Lexer opérationnel
- Parser opérationnel
- AST opérationnel
- Interpréteur AST opérationnel
- MTLoader
- Exécution directe de fichiers .mt

Pipeline actuel :

File
-> Lexer
-> Parser
-> AST
-> Interpreter

### Littéraux

Supportés :

- Integer
- String
- Boolean
- nil
- Symbol
- Array

### Variables et affectations

Supportées :

- variables
- affectations (:=)
- séquences
- portée lexicale

### Messages

Supportés :

- unary messages
- binary messages
- keyword messages

### Blocks

Support complet :

- blocs sans arguments
- blocs à paramètres
- variables temporaires
- value
- value:
- value:value:
- valueArray:

### Closures

Support complet :

- capture lexicale
- mutation de variables capturées
- closures imbriquées

Exemples validés :

- closureCounter.mt
- nestedClosure.mt

### Non Local Return

Support complet.

Syntaxe :

^42

Implémentation :

- MTNonLocalReturnException

Le retour non local fonctionne à travers plusieurs niveaux de closures.

### Contrôle de flux

Implémenté :

- ifTrue:
- ifFalse:
- ifTrue:ifFalse:
- whileTrue:
- whileFalse:
- to:do:

Les itérations croissantes et décroissantes fonctionnent.

### META Directives

Supportées :

- @module
- @import

Le parser extrait :

- moduleName
- imports

API :

parser.getModuleName()
parser.getImports()

## Runtime

### Objets principaux

- MTObject
- MTClass
- MTMetaclass
- MTMethod
- MTProperty
- MTBlock
- MTScope

### MTObject

Objet fondamental du système.

Contient :

- objectId
- clazz
- propertyValues

Responsabilités :

- réception de messages
- stockage des propriétés
- rattachement à une classe
- dispatch dynamique

Le dispatch repose sur :

receiver.send(...)
clazz.lookupMethod(...)

### MTClass

Responsabilités :

- création des instances
- lookup des méthodes
- gestion des propriétés
- héritage

Contient :

- name
- superclass
- declaredProperties
- allProperties
- methods

Les instances sont créées via :

newInstance()

qui rattache automatiquement l'objet à sa classe.

### MTMetaclass

MTMetaclass hérite actuellement de MTClass.

Aucun comportement spécifique supplémentaire n'est encore implémenté.

### MTRuntime

Le runtime conserve :

- Object
- Class
- ObjectClass
- ClassClass

et un registre global des classes.

Services principaux :

- registerClass(...)
- classNamed(...)
- createClass(...)

## Bootstrap Objet

### Carré Magique

Le bootstrap actuel construit :

- Object
- Class
- ObjectClass
- ClassClass

Relations de superclasse :

```Text
Object superclass      = null
Class superclass       = Object
ObjectClass superclass = ClassClass
ClassClass superclass  = ObjectClass
```

Relations de classe :
```Text
Object class      = ObjectClass
Class class       = ClassClass
ObjectClass class = ClassClass
ClassClass class  = ClassClass
```
### Simplification récente du MOP

Le champ :

MTClass.metaclass

a été supprimé.

Le système repose désormais uniquement sur :

MTObject.clazz

pour représenter la relation :

instance -> classe

Cette simplification n'a provoqué aucune régression connue.

## Classes noyau actuellement bootstrapées

- Integer
- Boolean
- String
- Array
- Dictionary
- Block

Toutes héritent actuellement de :
```Text
Object
```

Ces classes ont pour classes, le métaclasses suivants :
```
Integer       class -> IntegerClass
Boolean       class -> BooleanClass
String        class -> StringClass
Array         class -> ArrayClass
Dictionary    class -> DictionaryClass
Block         class -> BlockClass
```

et sont enregistrées dans MTRuntime.

## Primitives disponibles

### Object

- print
- println

### Boolean

- not
- and:
- or:
- xor:

### Integer

- +
- -
- *
- /
- %
- =
- <>
- <
- >
- <=
- >=
- !=

### String

- size
- +
- =

### Dictionary

- at:
- at:put:
- includesKey:
- size

## Validation récente

Tests validés :

- lexer
- parser
- ast
- blocs
- closures
- non local return
- contrôle de flux
- modules
- primitives
- dictionnaires
- tableaux

Validation importante :

42 println.

Le message println est correctement trouvé via la chaîne d'héritage jusqu'à Object.

## Dette technique restante

### Bootstrap

- audit complet du graphe des classes
- identification des classes orphelines
- finalisation de l'unification Runtime / Kernel

### Bibliothèques

À concevoir :

- chargement de bibliothèques MiniTalk
- système d'import avancé

### REPL

Non implémentée.

### Interop Java

À concevoir :

- accès aux classes Java
- invocation de méthodes Java
- accès JDBC

## Évaluation globale

MOP                     ~90%
Runtime                 ~85%
Bootstrap               ~85%
Parser                 ~100%
AST                    ~100%
Closures               ~100%
Non Local Return       ~100%
Interpréteur AST       ~100%
Primitives              ~75%
REPL                     0%
Bibliothèques           ~10%
Interop Java             0%

Le principal risque conceptuel (objets, classes, métaclasses, dispatch et propriétés) est désormais largement maîtrisé.

Le chantier prioritaire devient la stabilisation définitive du bootstrap et l'achèvement du noyau objet.