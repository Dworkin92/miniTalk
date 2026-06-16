# MiniTalk

MiniTalk est un langage objet inspiré de Smalltalk, implémenté en C avec l'aide de Copilot.

Le but de cette construction est multiple :
1. me remettre au C et apprendre à utiliser codeblocks
2. créer un petit langage de scripting facile à utiliser.
3. apprendre à utiliser l'IA pour mes développements

Je connais les à-priori de certains vis à vis de l'IA mais honnêtement,
travailler avec elle est un vrai gain de temps : au lieu de passer 3 semaines
pour obtenir un premier prototype fonctionnel, j'ai seulement mis 2 jours !
Par contre, tenir en laisse une IA pour qu'elle fasse ce que vous voulez
n'est pas de tout repos.

## Features de miniTalk

- objets dynamiques
- classes et héritage
- blocs avec closures
- super + return (^)
- collections
- traitement des chaines de caractères

## syntaxe du langage
## Exemple

```minitalk
a := Array new: 5;
a map: [ :x | x * 2 ] dbg;
