# MiniTalk v2

## Introduction

MiniTalk est un langage de scripting orienté objet inspiré de la famille Smalltalk.

L'objectif du projet est double :

- proposer un langage simple, concis, lisible et agréable à utiliser pour des tâches quotidiennes de scripting ;

- permettre la construction d'applications plus ambitieuses grâce à un véritable modèle objet et à l'accès à l'écosystème Java.

MiniTalk est entièrement implémenté en Java et s'exécute sur la JVM.

---

## Installer miniTalk

Pour compiler et installer miniTalk, vous aurez besoin de :
* un Open JDK 21 ou supérieur. Celui avec lequel je travaille est : [Adoptium Temurin JDK 21](https://adoptium.net/temurin/releases?version=21&os=any&arch=any)
* Maven, que vous pouvez downloader depuis : [ site apache Maven ](https://maven.apache.org/download.cgi#CurrentMaven)
* télécharger une version de minitalk depuis ce site.

Vous aurez besoin de renseigner les variables d'environnements suivantes :
* JAVA_HOME
* MAVEN_HOME
* MT_PATH (accès aux librairies minitalk)
* PATH complété avec $JAVA_HOME/bin (pour windows ce sera %JAVA_HOME%\bin), avec $MAVEN_HOME/bin (pour winbdows ce sera %MAVEN_HOME%\bin)

Pour windows 11, taper "environnement" dans la barre de recherche à coté du petit carré bleu windows. Vous aurez le choix entre 

* "modifier les variables d'environnement utilisateur",
* "modifier les variables d'environnement système".

Si vous n'êtes pas admin de votre poste, choisissez le premier choix :)

Un fois les variable renseignées, ouvrez une fenêtre cmd, et faites un "cd" vers le répertoire où vous avez désarchivé le projet. Lancez :

```bash
mvn clean package
```
pour compiler. Vous pourrez ensuite lancer minitalk avec la commande

```bash
java -jar target/miniTalk-2.0.0.jar chemin/fichier.mt
```


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
- les métaclasses sont des classes et, donc, des objets ;
- les closures sont des objets de premier ordre ;
- le modèle objet est visible et manipulable.

Le projet ne cherche cependant pas à reproduire Smalltalk à l'identique : nous en profitons pour explorer des notions plus modernes qui n'existaient pas dans les années 80, ou utiliser un vocabuler un peu différents du Smalltalk originel.

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
## Exemples


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
- MTProperty (correspond aux attributes de Smalltalk)
- MTBlock
- MTScope    (correspond au Context de Smalltalk)
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

