# MiniTalk - PROJECT_STATE_V2

## OBJET DU DOCUMENT


Ce document décrit l'état conceptuel actuel du MOP (Meta Object
Protocol) de MiniTalk.

Il ne constitue pas une description complète du projet.

Son objectif est de consigner les décisions architecturales
actuellement retenues afin d'éviter les confusions avec les
modèles Smalltalk classiques.

Les points encore ouverts sont explicitement signalés.

## MOP V1.2

Le modèle actuel sépare explicitement deux relations :

    clazz
    metaclazz

Ces deux relations ne représentent pas la même chose.


### RELATION clazz

clazz représente la relation d'instanciation.

Exemple :

    42 clazz -> Integer

Un objet connaît la classe dont il est instance.

### RELATION metaclazz

metaclazz représente la relation :

    classe -> métaclasse

Exemple :

    Integer metaclazz -> IntegerClass

Cette relation est distincte de clazz.

## DECISION IMPORTANTE

Les relations :

    instance -> classe

et

    classe -> métaclasse

ne sont PAS représentées par le même champ.

Le MOP actuel repose explicitement sur :

    clazz
    metaclazz

Toute analyse inspirée directement du modèle Smalltalk
classique doit être vérifiée avant d'être appliquée à
MiniTalk.

## REPRESENTATION JAVA ACTUELLE

Deux classes Java distinctes existent actuellement :

    MTClass
    MTMetaclass

Mais le système ne semble pas vraiment utiliser
la distinction Class/Metaclass dans son fonctionnement

Décision de fusionner les deux (une Métaclasse est une
classe dont seul le rôle change)

Constat actuel :

Classes et métaclasses partagent une très grande partie
de leur comportement :

    - name
    - superclass
    - méthodes
    - lookup
    - réflexion

## BOOTSTRAP

Le bootstrap construit actuellement les objets fondamentaux :

    Object
    Class
    ObjectClass
    ClassClass

Du fait de la suppression du code spécifique pour
les métaclasse, les tests de non-régression du
bootstrap ne sont plus actuellement complétement
validés.

## AXES DE REFLEXION

### SELECTEURS DE BASE

Les sélecteurs suivants existent déjà :

    name
    name:
    superclass
    superclass:
    metaclass

Les tests montrent qu'ils fonctionnent sur les classes
bootstrapées et sur les classes construites via
ClassDefInstaller.

### CREATION DYNAMIQUE DE CLASSES

La primitive actuelle :

    Class new

construit :

    MTClass
    MTMetaclass

et crée explicitement les liens entre les deux objets.

Le comportement final attendu n'est pas encore stabilisé.

## QUESTIONS OUVERTES

1. Qui porte le message "new" ?

Actuellement :

    Class new

Questions :
* est-ce la bonne responsabilité ?
* ce comportement doit-il appartenir à Class ?
* à une métaclasse ?
* au runtime ?

---

2. Qui porte "new:" ?

Exemple :

    Class new: #Person

La responsabilité exacte n'est pas encore tranchée.

---

3. Création automatique des métaclasses

Lors de :

    Person := Class new.

déterminer précisément :
* quelles structures doivent être créées ;
* quelles relations doivent être établies ;
* quelles inscriptions doivent être réalisées dans
  le runtime.

## INVARIANTS DU MOP ACTUEL

IMPORTANT

Toujours garder à l'esprit :

    clazz != metaclazz

Les deux relations ont volontairement été séparées.

Le modèle actuel ne doit pas être interprété comme une
simple transposition d'un modèle Smalltalk classique.

Les tests de non-régression constituent la référence
comportementale du système.

Le MOP v1.2 reste un chantier actif.
