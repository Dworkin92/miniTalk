# MiniTalk, manuel v 0.1

## Table des Matières

1. Introduction

2. Installation

   2.1 Installation de Java

   2.2 Installation de Maven
   
   2.3 Installation de miniTalk
   
   2.4 Lancement de miniTalk
   
3. Syntaxe de base

   3.1 Qu'est-ce qu'un script miniTalk ?
   
   3.2 Tout est objet

   3.3 Les types de base
   
       3.3.1 Entiers
   
       3.3.2 Booléens
   
       3.3.3 Chaînes
   
       3.3.4 Symboles
   
       3.3.5 Tableaux

4. Variables

   4.1 Déclaration
   
   4.2 Affectation
   

5. Messages

   5.1 Unary
   
   5.2 Binary
   
   5.3 Keyword

6. Séquences

7. Blocs

8. Closures

9. Portées lexicales

10. Retours non locaux

11. Classes et réflexion

12. Modules

---

## 1. Introduction

**miniTalk** a été conçu comme une expérimentation pour répondre à plusieurs besoins qui se sont révélés de plus en plus impérieux au fil de toutes ces années au cours desquelles j'ai pu acquérir une certaine expérience en matière d'administration de systèmes Unix, et d'intégrateur de produits.

Au cours de ma carrière, j'ai programmé dans de multiples langages : C, shells divers, awk, un peu de perl, python, java, et même lisp/scheme, Smalltalk, ou encore, cela va faire rire certains, Logo, etc. Petite parenthèse sur le Logo, d'ailleurs, car celui-ci ne se limite absolument pas, comme bon beaucoup le pensent, au seul contrôle d'un pointeur sur un écran (pu, pd, rot 89, draw 30, etc.). Ce langage est en lui-même un petit bijou qui permet de réaliser en quelques instructions des programmes complexes avec arrays, tableaux associatifs, fonctions imbriqués, récursivité, etc.

Tout cela pour vous dire que chaque langage avait ses qualités et ses défaults, mais aucun n'avait ni la simplicité, ni la versatilité du Smalltalk, langage magique à mes yeux, tout à la fois langage et système d'exploitation complet, et surtout implémentant pour le première fois un vrai paradigme Objet, que j'avais découverts avec émerveillement dans les années 80 durant ces fantastiques années où j'étais étudiant aux Mines de Nancy.

Je me suis demandé, donc, s'il me serait possible de créer mon propre langage de scripting inspiré de Smalltalk, afin de bénéficier de sa concision et de son expressivité pour remplacer les script shells que je trouvais souvent trop frustres, notamment dans la manipulations des données à niveaux multiples décrivant des configuration complexes. Le cahier des charges était relativement court :

- une syntaxe concise et facile à apprendre
- un langage dynamique, au typage libre (cela va faire hurler certains)
- un langage qui permette de lancer des commandes Unix (Unix, windows, etc)  et d'en recueillir le résultat
- un langage disponible sur tous les O/S

Partant de là, une évidence se fait tout de suite jour : s'appuyer sur un autre langage pour assurer le bas niveau, et là, quatre candidats émergent :
- perl
- ruby
- python
- java

J'élimine Perl : un peu trop ésotérique, de moins en moins utilisé de nos jours, des problèmes de compatibilité de modules CPAN en permanence...

J'élimine Ruby : langage très sympathique mais plus lent que d'autres et surtout, peu connu.

Je retire Python : pas pour les qualité intrinsèques de ce langage (généraliste, concis, dynamique, pascal-like, propreté intrinsèque du code, énorme librairie de modules, etc.), mais par simple soucis de performances par rapport à Java.

Reste Java : langage généraliste C-like, compilable en bytecode, mécanisme JIT, performant désormais, avec un immense écosystème de modules.

Franchement, j'ai longtemps hésité entre Java et Python, mais bon, Java a été mon choix final.

Quoi qu'il en soit, je vous souhaite bonne route dans l'apprentissage de miniTalk ainsi que beaucoup de joie et de bonheur à l'utiliser, à l'expérimenter, à l'étendre.

## 2. Installation

### 2.1 installation de Java

La première chose à faire est de disposer d'un Java Development kit. La question est : quelle version ?

MiniTalk a été conçu en Java 21, et devrait également fonctionner sans problème en Java 25. Le langage n'a pas été testé avec les versions antérieures, mais certaines des tournures de programmation employées ne fonctionneront pas en Java 8.

Je vous conseille donc d'utiliser un OpenJDK 21, que vous pourrez trouver gratuitement sur [Adoptium.net](https://adoptium.net/fr/temurin/releases?version=21&os=any&arch=any) pour votre operating system de prédilection. N'oubliez pas de remplir les variables d'environnement suivantes de votre système :
* JAVA_HOME, qui doit indiquer le chemin de votre JDK
* PATH, à compléter avec $JAVA_HOME/bin, ou %JAVA_HOME%\bin selon que vous êtes en *NIX, ou sous Windows.

### 2.2 installation de Maven

Le seconde chose à faire, si vous souhaiter ajouter à minitalk des fonctionnalités Java (comme l'usage des accès JDBC aux databases, le traitement de fichiers JSON, etc.), c'est d'installer Apache Maven pour tout recompiler. Vous pourrez le trouver sur le site [Apache](https://maven.apache.org)

Installer Maven dans un répertoire ne comprenant pas de blancs ou de caractères non ASCII (exemple sous Windows : C:\Local\Tools) et surtout n'oubliez pas de compléter les variables d'environnement :
* MVN_HOME, qui contiendra le répertoire dans lequel vous avez placé maven
* PATH, pour y ajouter $MVN_HOME/bin ou %MV_HOME%\bin

### 2.3 installation de miniTalk

Vous pouvez télécharger tout le contenu de la branche allant sur le site [miniTalk](https://github.com/Dworkin92/miniTalk), puis en cliquant le bouton "Code", et enfin "Download gzip". Vous pourrez désarchiver miniTalk où vous voulez.

Pour compiler miniTalk, ouvrez une fenêtre cmd ou powershell et tapez :

```shell
mvn clean package
```

### 2.4 lancement de miniTalk

Pour lancer minitalk :

```shell
java -jar target/miniTalk-2.0.0.jar ...
```

Note : vous pouvez tout à fait déplacer l'archive ailleurs et la lancer avec un fichier de commande shell, CMD ou Powershell.

plusieurs choix :
* **-test** : pour lancer les tests de non régression
* **chemin/fichier.mt** : pour charger et exécuter un fichier miniTalk

prochainement :
* **chemin/fichier.img** : pour charger un fichier image

## 3. Syntaxe de base

Maintenant que vous disposez d'un miniTalk fonctionnel, il est temps d'entrer dans le vif du sujet.

### 3.1 Qu'est-ce qu'un script miniTalk ?

Tout script miniTalk n'est en fait qu'une suite d'instructions que l'on donne au langage.

Une instruction peut être complexe et s'étaler sur plusieurs lignes.

Dans miniTalk, tout comme en C ou en Java, le retour chariot de fin de ligne ne marque
pas la fin d'une instruction : il est considéré comme un simple espace au même
titre que les caractères SPACE, ou TAB.

En fait, une instruction doit **toujours** être terminée par un caractère `.` ... sauf exceptions. Ce ne serait pas drôle s'il n'y en avait pas !

Les exceptions sont les suivantes :

* si l'instruction est la dernière instruction d'un bloc, le `.` est optionnel 
  (vous pouvez en placer un ... ou pas : c'est votre choix).
  
* la déclaration de variables temporaires n'a pas besoin de `.` à la fin : le
  caractère `|` marquant la fin de déclaration des variables est suffisant. Il
  ne faut pas mettre de point à la fin.
  
* si l'instruction est une méta-directive (nous reviendrons plus loin
  sur ce que sont les méta-directives), le caractère `]` fermant l'instruction
  suffit à marquer la fin de de celle-ci.
  
* cette exception est totalement implicite à la nature même de l'instruction en question :
  le commentaire ne doit pas avoir de point pour marquer sa fin car les commentaires
  sont éléminés du flux syntaxique par l'analyseur lexical de miniTalk avant même leur interprétation.

### 3.2 Tout est objet

Dans miniTalk, tout est objet. Qu'est ce que cela signifie ? 

* un objet est une structure qui possède des champs d'attributs aussi appelé **propriétés** qui contiennent les données propres à l'objet en question

* un objet, avec ses données,  est un cas particulier, ou instance, d'un objet générique que l'on nomme **classe** supportant toutes les propriétés communes à toutes les instances qui en descendent ainsi que les méthodes qui s'y appliquent. Dans une classe, vous allez donc trouver, les propriétés communes à tous les éléments instance de cette classe, ainsi que tous les traitement qui s'y appliquent.

* une classe peut elle-même avoir besoin de supporter des propriétés et des méthodes qui n'affectent qu'elle-même. une catégorie de classes spéciales existe pour cela dans miniTalk : les métaclasses.

exemples :
* "Jean", est un "objet" avec pour caractéristiques :
  - age
  - prénom
  - nom
  - adresse
  - etc.
  
* "Jean" est instance de la classe "Etre_humain" qui va supporter plusieurs méthodes de traitements pour altérer le status de Jean :
  - incrementeAge()
  - changeAdresse()
  - etc.

* "Etre_humain" possède une métaclasse "Etre_hmainClass" qui va supporter par exemple la
propriété "ordre" du règne animal dans lequel on placera : "mammifère", ainsi que la méthode pour interroger cette propriété.

