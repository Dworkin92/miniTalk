# MiniTalk - Project State

Last update: beta5

## Vision

MiniTalk est un langage de scripting orienté objet inspiré de Smalltalk.

Objectifs :

- syntaxe simple et lisible
- environnement de scripting plus agréable que le shell
- accès progressif à l'écosystème Java
- apprentissage des techniques d'implémentation des langages

---

# État actuel du projet

## Front-end

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

---

## Littéraux

Supportés :

- Integer
- String
- Boolean
- nil

---

## Variables et affectations

Supportées :

- variables
- affectations (:=)
- séquences
- portée lexicale

---

## Messages

### Unary messages

Exemple :

    [42] value

### Binary messages

Exemple :

    3 + 4

### Keyword messages

Exemple :

    true
        ifTrue: [42]
        ifFalse: [99]

---

## Blocks

Supportés :

- blocs sans arguments
- blocs à paramètres
- temporaires
- value
- value:
- value:value:
- valueArray:

Exemple :

    [:x :y | x + y]

---

## Closures

Support complet :

- capture lexicale
- mutation de variables capturées
- closures imbriquées

Exemple validé :

    ([:x |
        [:y |
            (x + y) * factor
        ]
    ] value: 10) value: 20

Résultat :

    90

---

## Non Local Return

Supporté.

Exemple :

    ^42

Implémentation :

- MTNonLocalReturnException

---

## Contrôle de flux

Implémenté :

- ifTrue:
- ifFalse:
- ifTrue:ifFalse:
- whileTrue:
- whileFalse:
- to:do:

Les itérations croissantes et décroissantes fonctionnent.

---

## META directives

Supportées par le lexer et le parser.

Syntaxe :

    /*
    @module Core;
    @import Collections;
    */

Le parser extrait :

- moduleName
- imports

API actuelle :

- parser.getModuleName()
- parser.getImports()

---

## Runtime

Objets principaux :

- MTObject
- MTClass
- MTMetaclass
- MTInteger
- MTBoolean
- MTString
- MTArray
- MTDictionary
- MTNil
- MTBlock
- MTScope

---

## Primitives disponibles

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
- ~<
- >~

Alias :

- ==
- !=
- <=
- >=

### Boolean

- not
- and:
- or:
- xor:

### String

- size
- +
- =

### Dictionary

- at:
- at:put:
- includesKey:
- size

---

## Tests

Réorganisés en modules :

- ParserRegressionTests
- BlockRegressionTests
- PrimitiveRegressionTests
- ControlFlowRegressionTests
- ModuleRegressionTests
- NonLocalReturnRegressionTests

Utilitaire commun :

- TestUtils.assertResult(...)

---

## Exécution de scripts

Commande validée :

    java -jar miniTalk-2.0.0.jar exemples/fibonacci.mt

Exemples validés :

- fibonacci.mt -> 55
- closures.mt -> 60
- closureCounter.mt -> 3
- nestedClosure.mt -> 90

MiniTalk exécute désormais des fichiers .mt hors des tests Java.

---

# Bootstrap objet

## Situation actuelle

Le carré magique existe :

- Object
- Class
- ObjectClass
- ClassClass

Relations :

- Object superclass = null
- Class superclass = Object
- ObjectClass superclass = ClassClass
- ClassClass superclass = ObjectClass

---

## Dette technique identifiée

Le bootstrap noyau et le runtime ne sont pas encore totalement unifiés.

Les classes :

- Integer
- Boolean
- String
- Array
- Dictionary
- Block

sont encore créées indépendamment via MTKernelBootstrap.

L'interpréteur utilise encore des appels du type :

    MTKernelBootstrap.createIntegerClass();

lors de l'évaluation des littéraux.

Conséquence :

Les classes métier ne semblent pas encore réellement rattachées à Object par héritage.

Exemple révélateur :

    42 println

échoue actuellement avec :

    Unknown selector: #println

alors que println est installé sur Object.

---

# Priorités de la prochaine étape

1. Audit du bootstrap
2. Unification Runtime / Kernel
3. Raccordement :

   - Integer -> Object
   - Boolean -> Object
   - String -> Object
   - Array -> Object
   - Dictionary -> Object
   - Block -> Object

4. Remplacement progressif des createXXXClass() dans l'interpréteur par l'utilisation des classes enregistrées dans MTRuntime.

5. Validation finale avec :

    42 println

---

# Version

Tag recommandé :

    beta5

Jalon majeur :

MiniTalk est maintenant capable d'exécuter des scripts .mt réels depuis la ligne de commande.
