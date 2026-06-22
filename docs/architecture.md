# Architecture du runtime

## Vue générale

MiniTalk repose sur deux couches :

1. un runtime Java ;
2. un modèle objet MiniTalk exécuté au-dessus de ce runtime.

## Éléments principaux

### Runtime Java

- `MTInterpreter` : évalue l’AST ;
- `MTEnvironment` : gère les variables lexicales ;
- `MTObject` : interface commune des objets runtime ;
- `MTClass` : classes MiniTalk ;
- `MTInstance` : instances utilisateur ;
- `MTBlockObject` : blocs et closures ;
- `MTArray`, `MTInteger`, `MTFloat`, `MTString`, `MTBoolean` : primitives.

## Dispatch

Le dispatch se fait par envoi de messages (`send(selector, args)`).

Les objets natifs comme `MTArray` peuvent faire un fallback vers leur classe MiniTalk (`Array`) pour exécuter des méthodes ajoutées dynamiquement dans la stdlib.

## Environnement lexical

Les blocs capturent leur environnement de création.

C’est ce qui permet les closures, les boucles et le retour non local `^`.

## Modèle objet

Arbre minimal actuel :

```text
Object
├── Class
└── Array
```

Ce modèle reste volontairement simple : MiniTalk ne met pas en œuvre un système complet de métaclasses.
