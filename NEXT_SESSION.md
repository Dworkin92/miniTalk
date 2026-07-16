\# MiniTalk - Next Session



Last update: 2025-07-05



\## Situation actuelle



MiniTalk est désormais capable :



\- d'interpréter des fichiers `.mt`

\- de charger un fichier via `MTLoader`

\- d'exécuter des scripts indépendamment des tests Java



Exemple :



```powershell

java -jar miniTalk-2.0.0.jar exemples/fibonacci.mt

```



Résultat :



```text

55

```



\---



\## Dernière découverte importante



Ajout de :



```java

ObjectPrimitives

```



avec :



```smalltalk

print

println

```



Compilation OK.



Mais :



```smalltalk

42 println

```



produit :



```text

Unknown selector: #println

```



\---



\## Diagnostic



Le problème ne vient pas des primitives.



Le problème vient du bootstrap.



Actuellement :



```java

createIntegerClass()

```



crée :



```java

Integer

```



sans :



```java

integerClass.setSuperclass(objectClass);

```



Donc :



```text

Integer

&#x20;   superclass = null

```



et les méthodes installées sur :



```text

Object

```



ne sont jamais héritées.



\---



\## Prochaine tâche



Analyser le bootstrap actuel.



Objectif :



```text

Object

&#x20;   ↑

Integer



Object

&#x20;   ↑

Boolean



Object

&#x20;   ↑

String



Object

&#x20;   ↑

Array



Object

&#x20;   ↑

Dictionary

```



\---



\## Vérifications à effectuer



\### Runtime bootstrap



Vérifier :



```java

MTRuntimeBootstrap.bootstrap()

```



et identifier :



```java

Object

Class



ObjectClass

ClassClass

```



utilisés réellement par le runtime.



\---



\### Kernel bootstrap



Vérifier :



```java

createIntegerClass()

createBooleanClass()

createStringClass()

createArrayClass()

createDictionaryClass()

```



et déterminer comment les connecter au véritable :



```java

Object

```



créé par :



```java

MTRuntimeBootstrap

```



\---



\## Premier test attendu



Créer :



```text

examples/println.mt

```



```smalltalk

42 println

```



Résultat attendu :



```text

42

42

```



\- premier 42 : primitive println

\- second 42 : valeur retournée par le script



\---



\## Exemples fonctionnels



Actuellement validés :



```text

fibonacci.mt       -> 55

closures.mt        -> 60

closureCounter.mt  -> 3

nestedClosure.mt   -> 90

```



\---



\## Modules



META directives implémentées :



```smalltalk

/\*

@module Core;

@import Collections;

\*/

```



Tests validés :



\- récupération du module

\- récupération des imports

\- exécution du module



Pas encore implémenté :



```text

chargement réel des imports

résolution des dépendances

```



\---



\## Ne PAS faire tout de suite



\- REPL

\- coroutines

\- multitâche

\- synchronisation des objets



\---



\## Objectif principal de la prochaine session



Faire fonctionner :



```smalltalk

42 println

```



au travers du véritable hérit\*ge :



```text

Integer ->\*Object

```



afin de commencer la construction de la bibliothèque stan\*ard MiniTalk.

