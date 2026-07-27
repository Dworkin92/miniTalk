# MiniTalk, manuel v 0.1

## Table des Matières

1. Introduction

2. Syntaxe de base

   2.1 Entiers
   
   2.2 Booléens
   
   2.3 Chaînes
   
   2.4 Symboles
   
   2.5 Tableaux

3. Variables

   3.1 Déclaration
   
   3.2 Affectation
   

4. Messages

   4.1 Unary
   
   4.2 Binary
   
   4.3 Keyword

5. Séquences

6. Blocs

7. Closures

8. Portées lexicales

9. Retours non locaux

10. Classes et réflexion

11. Modules

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