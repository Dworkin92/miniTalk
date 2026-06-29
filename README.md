# MiniTalk

MiniTalk est un petit langage objet dynamique, largement inspiré de Smalltalk et implémenté en Java. Il est distribué sous licence GPLv3 afin de garantir
que ses améliorations restent libres et accessibles à tous.


L’objectif du projet est triple :

- explorer la conception d’un langage orienté objet ;
- disposer d’un langage de script simple et expressif ;
- expérimenter une façon de travailler outillée et itérative sur un runtime de langage.

Vous l'aurez compris : il s'agit d'un outil expérimental, que vous utiliserez à vos risques et périls, sans que l'auteur de ces lignes ne puisse être tenu pour responsable des dommages éventuellement causés par l'outil.

## Points forts

- objets, classes et héritage ;
- variables et méthodes d’instance **et** de classe ;
- blocs avec closures ;
- `super` et retour non local `^` ;
- tableaux, itérations et opérations de style fonctionnel ;
- nombres entiers et réels ;
- accès aux fichiers et exécution de processus ;
- chargement modulaire d’une bibliothèque standard.

## Démarrage rapide

### Compiler

Si vous connaisez Maven, lancez :

```bash
mvn clean package
```
pour créer un jar exécutable

sinon, récupérez la dernière version du fichier miniTalk*.jar depuis github. 

1. Installez le jar dans un répertoire,

2. créez un sous-répertoire stdlib dans lequel vous placerez tous les
   fichiers stdlib/*.mt du projet. MiniTalk en a besoin pour son
   fonctionnement
   
3. créez un sous-répertoire test dans lequel vous placerez tous les
   fichiers test/test_*.mt du projet. Ces tests vous seront utiles
   en tant qu'exemples pour apprendre les méthodes et leur utilisation

### Lancer le REPL

La commande à lancer est la suivante :

```bash
java -jar <répertoire>/miniTalk-1.5.jar
```


### Charger la bibliothèque standard

Par défaut, le runtime cherche et charge tous les fichiers `stdlib/*.mt` dans le répertoire courant.

Vous pouvez ajouter des chemins de recherche de fichiers minitalk en définissant une variable d'environnement :

- sous windows :
```bash
set MT_STDLIB_PATH="<chemin1>;<chemin 2>;..."
```
- sous Linux/MacOS :
```bash
export MT_STDLIB_PATH="<chemin1>:<chemin 2>:..."
```

miniTalk détecte les références circulaires de paquetages.

## Exemple rapide

```smalltalk
#(1 2 3 4) inject: 0 into: [ :acc :x | acc + x ].
" => 10 "
```

```smalltalk
p := Process shell: 'echo Bonjour'.
code := p wait.
code = 0 ifTrue: [ 'OK' ] ifFalse: [ 'KO' ].
```

## Documentation

La documentation détaillée est disponible dans le dossier [`docs/`](docs/language.md) :


- [`docs/language.md`](docs/language.md) — syntaxe et concepts du langage ;
- [`docs/stdlib.md`](docs/stdlib.md) — bibliothèque standard et chargement ;
- [`docs/process.md`](docs/process.md) — exécution de commandes et gestion des flux ;
- [`docs/interop.md`](docs/interop.md) — comment inclure des
  classes java dans miniTalk;
- [`docs/architecture.md`](docs/architecture.md) — vue d’ensemble du runtime Java + MiniTalk ;
- [`docs/design.md`](docs/design.md) — vue d’ensemble de la conception en Java de MiniTalk ;

- [`docs/examples.md`](docs/examples.md) — exemples pratiques et idiomes.

## État du projet

MiniTalk est aujourd’hui utilisable comme langage de script orienté objet. La version actuelle privilégie :

- la lisibilité ;
- une base runtime simple ;
- une sémantique cohérente plutôt qu’une reproduction complète de Smalltalk.

## Philosophie

MiniTalk s’inspire de Smalltalk, mais n’essaie pas d’en être une copie parfaite. Certains choix ont été faits pour garder le runtime simple et le langage agréable à utiliser, notamment :

- pas de système complet de métaclasses ;
- une stdlib modulaire chargée au démarrage ;
- un REPL volontairement simple ;
- une séparation nette entre primitives runtime et logique de haut niveau en MiniTalk.

## Feuille de route possible

- documentation utilisateur et exemples supplémentaires ;
- enrichissement de la stdlib (`average`, `first`, `last`, etc.) ;
- amélioration du CLI (`-L`, chargement de libs, options) ;
- raffinement du REPL multi‑ligne ;


## Licence

Ce projet est distribué sous licence GNU GPL v3.

Voir le fichier [LICENSE.txt](LICENSE.txt) pour le texte complet.

