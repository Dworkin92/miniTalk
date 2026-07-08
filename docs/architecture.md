# MiniTalk - État actuel de l'architecture

## 1. Vision générale

MiniTalk est actuellement organisé en 4 couches :

+-----------------------------------+
|         MiniTalk Source           |
+-----------------------------------+
|               AST                 |
+-----------------------------------+
|          Runtime / MOP            |
+-----------------------------------+
|            Java VM                |
+-----------------------------------+

Pour l'instant, nous avons principalement développé :

```
Runtime
+
MOP
+
Bootstrap
+
Primitives
```
Le lexer, parser et interpréteur AST restent à faire.

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

ClassClass class = ClassClass
```

Diagramme

```
            Object
               ^
               |
             Class


          ObjectClass
               ^
               |
          ClassClass

Object.class      -> ObjectClass
Class.class       -> ClassClass
ClassClass.class  -> ClassClass
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
=
<
>
~<
>~
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
Block
Scope
Closure
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
MOP                ~90%
Runtime            ~65%
Bootstrap          ~70%
Primitives         ~60%
AST                ~90%
Blocks/Closures    ~0%
Parser             ~0%
Interpréteur AST   ~0%
```

Le plus gros risque conceptuel (métaclasses + dispatch + propriétés + bootstrap minimal) est maintenant derrière nous.

Le prochain gros sujet sera très probablement :
```Plain Text
MTBlock
   +
MTScope
   +
Closures lexicales
   +
Non-local return (^)
```

car c'est ce qui permettra finalement d'exécuter l'AST.
