## Architecture du runtime

### Vue générale

MiniTalk repose sur deux couches :
- un runtime Java ;
- un modèle objet MiniTalk exécuté au-dessus de ce runtime.

---

## Éléments principaux

### Runtime Java

- MTInterpreter : évalue l’AST
- MTEnvironment : gère les variables lexicales
- MTObject : interface commune
- MTClass : classes MiniTalk
- MTInstance : instances utilisateur
- MTBlockObject : blocs / closures

---

### Types natifs

- MTCollectionObject : base des collections (logique partagée)
- MTArray : tableau indexé
- MTListObject : liste mutable
- MTSetObject : ensemble sans doublons
- MTDictionaryObject : dictionnaire clé -> valeur

- MTInteger, MTFloat, MTString, MTBoolean, MTNil

---

## Modèle objet

```
Object
├── Class
├── Collection
│   ├── Array
│   ├── List
│   ├── Set
│   └── Dictionary
├── String
├── Integer
├── Float
└── Boolean
```

---

## Dispatch

Chaque objet répond à :

    send(selector, args)

Fallback possible vers la classe MiniTalk associée.

---

## Environnement lexical

Les blocs capturent leur environnement de création.
Permet : closures, boucles, retour non local (^).

---

## Collections

Factorisées dans MTCollectionObject :

- do:
- collect: / map:
- select: / filter:
- reject:
- detect:
- anySatisfy:
- allSatisfy:
- inject:into: / reduce:with:

Dictionary ajoute :
- at:
- put:value:
- containsKey:
- keys / values
- do: (clé, valeur)

---

## Héritage

- Toute classe hérite de Object
- Sous-classes enregistrées automatiquement

---

## État actuel

Langage dynamique avec :
- objets
- closures
- collections complètes
- dispatch par message

---

## Évolutions possibles

- Association (clé/valeur)
- Dictionary collect:
- introspection complète
- métaclasses
