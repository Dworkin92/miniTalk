# 📐 Design Notes

## 🧭 Objectif

MiniTalk est un langage dynamique inspiré de Smalltalk, implémenté en Java.
L’objectif est de fournir :

- un modèle objet uniforme
- un dispatch dynamique de messages
- un système de collections cohérent et extensible
- une capacité à déplacer progressivement la logique du runtime vers le langage lui-même (bootstrap)

---

## 🧠 Modèle d’exécution

Tout repose sur l’envoi de messages :

    receiver send selector with arguments

Le dispatch est dynamique et suit plusieurs niveaux :

1. Implémentation native Java
2. Hiérarchie miniTalk (lookup dans MTClass)
3. Fallback vers Collection (pour les collections)

---

## 🏗️ Architecture hybride

Java runtime objects
        ↓
MTArray / MTList / MTSet / MTDictionary
        ↓ fallback
miniTalk classes (Collection, etc.)

### ✅ Avantages

- performances sur les primitives
- flexibilité du langage côté miniTalk
- introspection possible

### ⚠️ Limites

- héritage non automatique pour les objets natifs
- nécessité d’un fallback manuel

---

## 🔁 Dispatch et fallback

Les objets collections utilisent un mécanisme commun :

    dispatchWithFallback(selector, args)

Ce mécanisme :

1. tente les méthodes Java (ex: do:)
2. sinon cherche dans Collection (miniTalk)

---

## 📦 Collections

| Type | Implémentation | Particularité |
|------|----------------|--------------|
| Array | MTArray | indexé |
| List | MTListObject | dynamique |
| Set | MTSetObject | unicité |
| Dictionary | MTDictionaryObject | clé/valeur |

---

## ✅ API commune

do:
collect:
select:
reject:
inject:into:

---

## ⚠️ Particularités Dictionary

Blocs multi-paramètres :

    [:k :v | ...]

---

## 🧱 Blocks

- capture d’environnement
- value:, value:value:
- callWithReceiver()

---

## 🧩 Bootstrap partiel

Certaines méthodes sont définies en miniTalk :

    Collection addMethod: 'collect:' ...

---

## ⚠️ Points importants

- mapping Array new → MTArray obligatoire
- snapshot pour do:
- fallback uniforme obligatoire

---

## 🧪 Tests

Le fichier test_collection.mt valide :

- Array
- List
- Set
- Dictionary

---

## 🚀 Évolutions

- keyword chaining
- bootstrap complet
- amélioration REPL
- debug structuré

---

## ✅ Conclusion

MiniTalk repose sur un compromis : Java pour les primitives, miniTalk pour l’expressivité.
