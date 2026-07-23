# Bootstrap Architecture

## Introduction

MiniTalk repose sur un Meta Object Protocol (MOP) permettant
de représenter uniformément :

- les objets ;
- les classes ;
- les métaclasses.

Le bootstrap est responsable de la création des objets
fondamentaux du système et de la mise en place des relations
entre eux.

---

# Le quadrant magique

Le bootstrap commence toujours par créer quatre objets
fondamentaux :

    Object
    Class
    ObjectClass
    ClassClass

Relations de superclasse :

    Object superclass      = null
    Class superclass       = Object

    ObjectClass superclass = ClassClass
    ClassClass superclass  = ObjectClass

Relations de classe :

    Object class      = ObjectClass
    Class class       = ClassClass

    ObjectClass class = ClassClass
    ClassClass class  = ClassClass

Ce graphe constitue le noyau du système.

Toutes les autres classes sont construites à partir de lui.

---

# Modèle de typage

MiniTalk utilise une seule relation de typage :

    clazz

Cette relation est héritée de MTObject.

Exemple :

    42
      class -> Integer

    Integer
      class -> IntegerClass

    IntegerClass
      class -> ClassClass

Le champ historique :

    MTClass.metaclass

a été supprimé.

Le système repose désormais entièrement sur :

    MTObject.clazz

---

# Bootstrap manuel

Le bootstrap du quadrant magique est réalisé manuellement.

Les objets :

    Object
    Class
    ObjectClass
    ClassClass

sont créés explicitement dans :

    MTRuntimeBootstrap.bootstrapCore()

Cette phase est spéciale et ne peut pas être décrite à
l'aide de mécanismes plus évolués puisque les objets
fondamentaux n'existent pas encore.

A la fin de la création des classes, sont installée 
les méthodes pour les classes Object et Class grâce
à la fonction :

    MTRuntimeBootstrap.bootstrapCorePrimitives()

---

# Bootstrap déclaratif

Une fois le quadrant magique construit, les classes du
noyau et leur méthodes d'instances
peuvent être définies de façon déclarative.

Le mécanisme repose sur :

    @ClassDef

et :

    ClassDefInstaller

---

# Annotation ClassDef

Exemple :

    @ClassDef(
        name = "Integer",
        superclass = "Object",
        instancePrimitives = IntegerPrimitives.class
    )
    public final class IntegerClassDef {
    }

Cette annotation décrit :

- le nom de la classe ;
- sa superclasse.
- les méthodes d'instance

L'annotation ne contient aucun code d'exécution.

Elle représente uniquement une définition déclarative.

---

# ClassDefInstaller

L'installation d'une classe est réalisée par :

    ClassDefInstaller.install(
        runtime,
        IntegerClassDef.class);

L'installateur effectue automatiquement :

    1. Lecture de l'annotation
    2. Recherche de la superclasse
    3. Création de la classe
    4. Création de la métaclasse associée
    5. Liaison classe/métaclasse
    6. Enregistrement dans le runtime
    7. ajout des méthodes d'instance grâce à Primitiveinstaller

---

# Création automatique des métaclasses

Toute classe créée par le mécanisme ClassDef reçoit
automatiquement une métaclasse dédiée.

Exemple :

    Integer
        class -> IntegerClass

    Boolean
        class -> BooleanClass

    String
        class -> StringClass

    Array
        class -> ArrayClass

    Dictionary
        class -> DictionaryClass

    Block
        class -> BlockClass

---

# Héritage des métaclasses

La hiérarchie des métaclasses reflète celle des classes.

Règle :

    A superclass = B

implique :

    AClass superclass = BClass

Exemple :

    Employee superclass = Person

implique :

    EmployeeClass superclass = PersonClass

Toutes les métaclasses sont des instances de :

    ClassClass

Exemple :

    IntegerClass class = ClassClass

    BooleanClass class = ClassClass

    StringClass class = ClassClass

---

# Classes du noyau utilisant ClassDef

Les classes suivantes utilisent actuellement le bootstrap
déclaratif :

    Integer
    Boolean

    String

    Array
    Dictionary

    Block

---

# Installation des primitives

Les méthodes primitives sont installées automatiquement
lors du traitement de la définition de classe.

Exemples :

    IntegerPrimitives

    BooleanPrimitives

    StringPrimitives

    ArrayPrimitives

    DictionaryPrimitives

    BlockPrimitives

Le quadrant magique reste bootstrapé manuellement.

---

# Protocole minimal de Class

Toutes les classes partagent actuellement le protocole :

    name

    superclass

    new

Implémentation :

    ClassPrimitives

Exemples :

    Integer name

        => #Integer

    Integer superclass

        => #Object

    Integer new

        => instance d'Integer

---

# Évolution future

Le mécanisme ClassDef a été conçu pour permettre une
extension progressive du bootstrap.

Évolutions envisagées :

- méthodes de classe spécialisées ;
- propriétés déclaratives ;
- création dynamique de classes ;
- protocole complet de métaprogrammation ;
- remplacement progressif des anciens createXXXClass().

---

# Résumé

Le bootstrap MiniTalk repose sur deux niveaux :

1. Bootstrap manuel

       Object
       Class
       ObjectClass
       ClassClass

2. Bootstrap déclaratif

       @ClassDef
       +
       ClassDefInstaller

Le premier construit le noyau du MOP.

Le second permet de créer automatiquement une classe,
sa métaclasse associée et leur intégration complète dans
le runtime.
