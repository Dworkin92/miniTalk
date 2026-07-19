# MiniTalk v2

## Introduction

MiniTalk est un langage de scripting orienté objet inspiré de la famille Smalltalk.

L'objectif du projet est double :

- proposer un langage simple, lisible et agréable à utiliser pour des tâches quotidiennes de scripting ;

- permettre la construction d'applications plus ambitieuses grâce à un véritable modèle objet et à l'accès à l'écosystème Java.

MiniTalk est entièrement implémenté en Java et s'exécute sur la JVM.

---

## Pourquoi MiniTalk ?

Les shells traditionnels sont extrêmement puissants mais deviennent rapidement difficiles à maintenir lorsque les scripts prennent de l'ampleur.

À l'inverse, les langages modernes offrent souvent de nombreuses fonctionnalités mais au prix d'une complexité importante.

MiniTalk cherche à trouver un équilibre :

- syntaxe simple ;
- tout est objet ;
- envoi de messages ;
- closures lexicales ;
- héritage simple ;
- métamodèle objet explicite ;
- accès progressif aux bibliothèques Java lorsque nécessaire.

L'idée est de permettre à un utilisateur de commencer avec quelques lignes de script, puis d'évoluer progressivement vers des programmes plus complexes sans changer d'environnement.

---

## Philosophie

MiniTalk reprend plusieurs idées fondamentales de Smalltalk :

- tout est objet ;
- la communication se fait par envoi de messages ;
- les classes sont elles-mêmes des objets ;
- les métaclasses sont des objets ;
- les closures sont des objets de premier ordre ;
- le modèle objet est visible et manipulable.

Le projet ne cherche cependant pas à reproduire Smalltalk à l'identique.

L'objectif est plutôt de construire un langage moderne, léger et facilement intégrable à l'écosystème Java.

---


## État actuel


MiniTalk dispose déjà des éléments suivants :

- Lexer
- Parser
- AST
- Interpréteur AST
- Runtime orienté objet
- Dispatch dynamique de messages
- Closures lexicales
- Retours non locaux (^)
- Classes et métaclasses
- Système de propriétés dynamiques
- Chargement et exécution de fichiers `.mt`

Les classes fondamentales du système sont :

- Object
- Class
- ObjectClass
- ClassClass

Le runtime implémente un véritable Meta Object Protocol (MOP) permettant de représenter :

- les objets ;
- les classes ;
- les métaclasses.

---
## Exemple


Addition :
```smalltalk
3 + 4
```

Block :
```smalltalk
[:x :y | x + y] value: 10 value: 20
```

Condition :
```smalltalk
true
```

```smalltalk
true ifTrue: [42]
```

```smalltalk
true ifFalse: [99]
```

Boucle :

```smalltalk
1 to: 10 do: [:i |
    i println
]
```

---



## Architecture


Le projet est organisé autour de plusieurs couches :

``` Text
 Source File
     |
     v
   Lexer
     |
     v
   Parser
     |
     v
    AST
     |
     v
 Interpreter
     |
     v
  Runtime
     |
     v
    MOP
```


Le runtime repose notamment sur les objets suivants :

- MTObject
- MTClass
- MTMetaclass
- MTMethod
- MTProperty
- MTBlock
- MTScope
- MTRuntime

---

## Relation avec Java

L'un des objectifs majeurs de MiniTalk est de pouvoir utiliser l'immense écosystème Java existant.

Le langage doit permettre :

- l'accès aux bibliothèques Java ;
- l'invocation d'API Java ;
- l'utilisation de JDBC ;
- l'intégration progressive avec les frameworks de la JVM.

L'idée n'est pas de réinventer des milliers de bibliothèques déjà disponibles mais de fournir une syntaxe plus agréable pour y accéder.

---

## Exemple de vision

Un script MiniTalk simple pourrait manipuler :

- des fichiers ;
- des données JSON ;
- une base de données ;
- des services HTTP ;

tout en restant concis et lisible.

Lorsque des fonctionnalités avancées sont nécessaires, MiniTalk pourra s'appuyer directement sur les bibliothèques Java existantes.

---

## Roadmap

Travaux en cours :

- stabilisation du bootstrap objet ;
- finalisation du noyau de classes ;
- audit du graphe des classes ;
- chargement de bibliothèques MiniTalk ;
- système de modules ;
- REPL interactive ;
- interopérabilité Java.


---

## Objectif final

MiniTalk vise à devenir un langage de scripting orienté objet :

- simple pour les petits scripts ;
- robuste pour les applications plus ambitieuses ;
- fidèle à l'esprit Smalltalk ;
- capable de tirer parti de toute la puissance de la plateforme Java.

