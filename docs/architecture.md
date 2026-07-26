# MiniTalk - État actuel de l'architecture

## 1. Vision générale

MiniTalk est actuellement organisé en 4 couches :

|                                   |
|:---------------------------------:|
|         MiniTalk Source           |
|               AST                 |
|          Runtime / MOP            |
|            Java VM                |


Pour l'instant, nous avons principalement développé :

```
Runtime
+
MOP
+
Bootstrap
+
Primitives
+
Lexer
+
AST
```

La REPL et la gestion des modules restent à développer +
un grand nombre de méthodes utiles pour les objets

## 2. MOP (Meta Object Protocol)

Les principes retenus sont :

1. tout est objet
1. les classes sont des objets
1. les métaclasses sont des objets
1. héritage simple
1. closures lexicales
1. retours non locaux

Objet fondamental :
```
MTObject
```

## 3. Bootstrap minimal validé

Nous avons maintenant les 4 objets fondateurs :

```
Object
Class

ObjectClass
ClassClass
```

Relations validées :

```smalltalk
Object superclass = null

Class superclass = Object

Object class = ObjectClass

Class class = ClassClass

ObjectClass superclass = ClassClass

ClassClass superclass = ObjectClass

ObjectClass class = ClassClass

ClassClass class = ClassClass
```

Diagramme

```Plain Text
           class
   Object ----------> ObjectClass ---------------+
      ^                   ^                      |
      | superclass        | superclass           |
      |                   |                class |
    Class ----------> ClassClass ---+            |
             class        ^         | class      |
                          |         |            |
                          +---------+------------+


```

## 4. Runtime

### MTObject

Base de tous les objets.

Contient :

```
objectId
clazz
propertyValues
```

où

```
propertyValues :
    MTDictionary<MTSymbol, MTObject>
```

### MTClass

Contient :

```
name
superclass
metaclass
declaredProperties
allProperties
methods
```

Responsabilités :
```
création d'instances
lookup des méthodes
gestion des propriétés
héritage
```

### MTMetaclass

```
extends MTClass
```
Utilisée pour les méthodes et propriétés de classe.

### MTProperty

Contient :
```
name
ownerClass
defaultValue
```

### MTMethod

Contient :
```
selector
ownerClass
body
```
Actuellement :
```BNF
body : MTMethodBody
```

Plus tard :
```BNF
body : MTBlockNode
```

## 5. Dispatch de messages

Validation complète.

API actuelle :
```Java
receiver.send(selector)

receiver.send(
    selector,
    arguments)

```
où
```BNF
arguments : MTArray
```

Lookup :
```
classe courante
   ↓
superclasse
   ↓
...

```

Tests validés :

- invocation
- héritage
- surcharge
- arguments


## 6. Système des propriétés
Fonctionnel.

Création :

```Smalltalk
Person addProperty: #age.
```

est  représenté par

```Java
personClass.addProperty(...)
```

Supporte :
* héritage
* ajout dynamique
* rebindProps()

un système d'annotations Java permet de créer rapidement des
synonymes pour les opérateurs et les messages

Stratégie retenue : migration paresseuse

Après ajout d'une propriété :

```Smalltalk
Person addProperty: #name
```

les instances existantes ne sont pas migrées immédiatement
mais :
```Java
rebindProps()
```
synchronise les propriétés manquantes.

## 7. Primitives disponibles

### MTNil
Singleton :
```Plain text
nil
```

### MTBoolean

Singletons :
```Plain Text
true
false
```

Méthodes :
```Smalltalk
not
and:
or:
xor:
```

### MTInteger
Méthodes :
```Smalltalk
+
-
*
/
%
=
!=, <>
<
>
~<, <=
>~, >=
```



### MTString
Méthodes :
```Smalltalk
size
+
=
```

### MTSymbol
Hiérarchie actuelle :
```Plain Text
MTObject
   ^
MTString
   ^
MTSymbol
```

Internage global :
```Java
MTSymbol.intern(...)
```
Validé.

### MTArray
Structure séquentielle minimale.

Méthodes Java actuelles :
```Java
add()
at()
atPut()
size()
```

### MTDictionary
Méthodes MiniTalk validées :
```Smalltalk
at:
at:put:
includesKey:
size
```

## 8. Bootstrap des primitives
Nous avons commencé la migration vers :
```Java
@Primitive
```

et :
```Java
PrimitiveInstaller
```

Architecture :
```Plain Text
IntegerPrimitives
BooleanPrimitives
StringPrimitives
DictionaryPrimitives
```

Le bootstrap devient :
```Java
PrimitiveInstaller.install(
   integerClass,
   IntegerPrimitives.class);
```

au lieu de :
```Java
integerClass.addMethod(...)
```
partout.

Migration validée pour :
```Plain Text
Integer ✅
Boolean ✅
```

## 9. AST

Déjà défini dans le YAML.

Points importants :
```Plain Text
MTCompilationUnitNode
MTProgramNode
MTMessageSendNode
MTBlockNode
MTNonLocalReturnNode
```

Commentaires conservés dans l'AST :
```Plain Text
MTCommentNode
```

Meta-directives conservées :
```Plain Text
@module
@import
...
```

## 10. Ce qui reste à faire

### Runtime

Encore à concevoir ou compléter :
```Plain Text
Collection
SequenceableCollection
Set
Magnitude
```

### MOP
Automatisation future :
```Plain Text
createClass()
registerClass()

chargement du noyau
```

### AST Execution
À implémenter :

```Plain Text
MTBlock
MTScope

capture lexicale

retours non locaux (^)
```

### Front-end
À faire :
```Plain Text
Lexer
Parser
Interpréteur AST
```

### État d'avancement
Je dirais :
```Plain Text
MOP                   ~90%
Runtime               ~75%
Bootstrap             ~75%
Primitives            ~60%
AST                   ~90%
Blocks/Closures       ~100%
Parser                ~100%
Scope                 ~95%
Closures lexicales    ~100%
Interpréteur AST      ~100%
Non-local return(^)   ~100%
REPL                   0%
Gestion fichiers      ~50%
Gestion bibliothèques ~0%
```

Le plus gros risque conceptuel (métaclasses + dispatch + propriétés + bootstrap minimal) est maintenant derrière nous.
mais le bootstrap n'est pas complet ( il manque les metaclass pour le classe hors du quadrant magique)

Le prochain gros sujet sera très probablement :
```Plain Text
Bootstrap correct
chargement de librairies minitalk
interopérabilité avec Java (exemple, utiliser JDBC pour accéder à des DB)
```

