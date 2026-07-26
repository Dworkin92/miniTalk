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

META DIRECTIVES

✅ Le lexer produit META_DIRECTIVE
✅ Le parser produit MTMetaDirectiveNode
✅ MTMetaResolver implémenté
✅ Fusion d'AST opérationnelle
✅ Imports récursifs testés
✅ Imports circulaires testés
✅ Compilation OK


Bootstrap des objets fondamentaux
---------------------------------

Contexte
~~~~~~~~

L'introduction de la classe Symbol et des primitives :

    =
    asString

a révélé un problème plus général dans le bootstrap du runtime.

Plusieurs objets fondamentaux sont créés avant que leurs classes
MiniTalk n'existent :

    - MTSymbol
    - MTBoolean.TRUE
    - MTBoolean.FALSE
    - MTString

Ils se retrouvaient donc avec :

    clazz == null

ce qui provoquait des NullPointerException lors des envois
de messages :

    #Integer = #Integer
    #Integer asString
    Integer name

Corrections
~~~~~~~~~~~

1. MTSymbol

Fichier ~~
    mt/runtime/MTSymbol.java

Aj~~ts :

    - variable statique sym~~lClass
    - méthode setSymbolCla~~(...)
    - méthode allInterned()~~Le bootstrap appelle :

    bindI~~ernedSymbols(symbolClass)

qui :
~~   - mémorise la classe Symbol
  ~~- rattache tous les symboles déjà~~nternés
    - permet aux futurs s~~boles créés via intern(...)
     ~~e recevoir automatiquement leur c~~sse

2. MTBoolean

Fichier :

   ~~t/runtime/MTBoolean.java

Ajout :~~    setBooleanClass(...)

Le boot~~rap rattache :

    TRUE
    FALS~~
à la classe Boolean.

3. MTStrin~~
Fichier :

    mt/runtime/MTStri~~.java

Ajouts :

    - variable s~~tique stringClass
    - méthode s~~StringClass(...)

Le constructeur~~TString rattache automatiquement
~~s nouvelles instances à la classe String lorsque
celle-ci existe.

4. MTRuntimeBootstrap

Fichier :

    mt/runtime/bootstrap/MTRuntimeBootstrap.java

Ajouts :

    bindInternedSymbols(symbolClass)

et

    MTBoolean.setBooleanClass(booleanClass)

    MTString.setStringClass(stringClass)

Résultat
~~~~~~~~

Le script suivant fonctionne désormais :

    (#Integer = #Integer) println.
    (#Integer = #Boolean) println.
    (#Integer asString) println.
    Integer name println.

Résultat :

    true
    false
    Integer
    #Integer

L'API de réflexion minimale sur les classes est donc
désormais opérationnelle.

Points à surveiller
~~~~~~~~~~~~~~~~~~~

Vérifier ultérieurement si d'autres objets fondamentaux
créés pendant le bootstrap nécessitent le même traitement :

    - nil
    - Array
    - Dictionary
    - Block

Actuellement, aucun problème connu n'a encore été observé
sur ces classes.

MOP v1.2
=========

Statut
------

Phase d'exploration/documentation.

Le modèle MOP v1.0 basé exclusivement sur :

    clazz
    superclass

a montré plusieurs limites lors des tests :

    nil class
    nil class name
    nil class println

ainsi que lors de la mise en place des primitives
de réflexion :

    class
    name

Une version expérimentale MOP v1.2 est documentée
dans :

    docs/MOP-minitalk-v1.2.yaml

Principes explorés
------------------

Séparation explicite de trois relations :

    clazz
        objet -> classe

    metaclazz
        classe -> métaclasse

    superclass
        héritage

Objectif :

    - supprimer les cycles de lookup
    - simplifier le bootstrap du MOP
    - distinguer les relations conceptuellement
      différentes actuellement fusionnées dans
      "clazz"

Points encore ouverts
---------------------

    - fermeture du sommet de la hiérarchie
      des métaclasses

    - rôle exact de ClassClass

    - validation des chemins de lookup :

        nil class
        nil class name
        nil class println

Aucune modification du runtime n'a encore été
effectuée à partir du modèle v1.2.

