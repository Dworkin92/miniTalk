# 🔗 MiniTalk – Java Interoperability Guide


## 🧭 Avant-propos

MiniTalk n’expose pas directement l’API Java.

Il implémente un modèle d’interopérabilité contrôlée, dans lequel :
* Java agit comme moteur d’exécution
* miniTalk fournit le modèle objet et l’expressivité

```
MiniTalk
    ↓
MTObject (runtime Java)
    ↓
Structures Java (ArrayList, HashMap, …)
```

👉 Le langage reste cohérent, prédictible et extensible.

MiniTalk autorise à interagir avec le runtime Java. Cette interopérabilité est limité
mais puissante : elle permet d'accéder à des objets écrits en Java
(Array, List, Set, Dictionary, etc.) et à leurs comportements natifs.

Le présent document explique comment le code de miniTalk intéragit avec les classes Java,
comment les objets sont instanciés, et comment le traitement des messages relie
les deux mondes.

---

## 🧱 Principes fondamentaux

MiniTalk n'expose pas d'emblée des classes Java arbitraires.
Ainsi, on ne peut pas écrire directement :

```smalltalk
ArrayList new
System currentTimeMillis
```

Au lieu de cela, miniTalk s'appuie sur un **runtime d'objets Java déjà préparés** :

| MiniTalk Class | Java Implementation |
|----------------|---------------------|
| Array          | MTArray             |
| List           | MTListObject        |
| Set            | MTSetObject         |
| Dictionary     | MTDictionaryObject  |

👉 Ces classes agissent comme des passerelles entre miniTalk et Java.

---

## 🏗️ Architecture

Le coeur du langage est donc construit autour de ces classes
```
MiniTalk Class (Array, List, …)
        ↓
MTClass (meta)
        ↓
Runtime Object
   MTArray / MTListObject / MTSetObject / MTDictionaryObject
        ↓
Java delegate
   ArrayList / HashSet / HashMap
```

---


## 🏗️ Object Creation

### MiniTalk side

en miniTalk, on crée un objet de la façon suivante :

```smalltalk
r := Array new.
```

on adresse un message `new` à la classe souhaité, ce qui retourne ce qu'on appelle
une instance : un objet dont le comportement est régie par les méthodes présentes
dans la classe.

### Java side

du côté Java, cela se traduit par une reconnaissance de la classe prédéfinie adressée
(ici "Array") parmi toutes les classes prédéfinines, 
et la création de l'objet correspondant.

```java
if (name.equals("Array")) return new MTArray(new ArrayList<MTObject>());
if (name.equals("List")) return new MTListObject();
if (name.equals("Set")) return new MTSetObject();
if (name.equals("Dictionary")) return new MTDictionaryObject();
```

👉 Cette recherche de correspondance est donc à la fois **explicite et obligatoire**.

---

## 🔄 Message Dispatch Pipeline

Quand un message est envoyé a un objet :

```smalltalk
r add: 1.
```

Le chemin parcouru par le message durant son exécution est le suivant :

1. `MTArray.send("add:")` : envoie du message à la classe Java de l'instance
2. exécution du code Java correspondant, s'il existe et est trouvé.
3. s'il n'existe pas → `dispatchWithFallback`, un mécanisme de rattrapage
   est activé dans miniTalk,
4. lookup dans la classe parente, ici, `Collection`.

L'odre de résolution est donc :

| Niveau | Action |
|--------|--------|
| 1 | méthode Java |
| 2 | fallback miniTalk |
| 3 | erreur |

---

## 🔁 dispatchWithFallback

La méthode centrale du runtime est donc :

```java
dispatchWithFallback(selector, args)
```

fonctionnement :

1. tente les primitives Java (MTCollectionObject)
2. recherche dans Collection (`Collection` miniTalk)
3. exécute la méthode via  `callWithReceiver`

---

## ✅ Exécution hybride (clé du design)

### exemple MiniTalk

```smalltalk
#(1 2 3) collect: [ :x | x + 1 ]
```

### Pipeline réel

ci après, le pipeline hybride complet :
```
collect: → miniTalk
   ↓
do: → Java
   ↓
block value: → miniTalk
   ↓
add: → Java
```

---

## 🧩 Les Blocs et Java

Les blocs viennent de miniTalk, mais sont exécuté via Java.

Ainsi, le bloc suivant

```smalltalk
l do: [ :x | x + 1 ]
```

sera exécuté en Java, comme ceci :

```java
block.send("value:", List.of(each));
```

👉 Les blocs sont invoqués grâce à une utilisation via des messages,
    et non via des appels directs aux fonction

## ⚠️ Ne jamais faire

```java
block.call(...)
```

Pourquoi ?

* cela casse le modèle objet
* cela crée un bypass du dispatch
* cela empêche le mécanisme de fallback miniTalk de fonctionner
* cela introduit des bugs subtils dans tout le langage.

---

## 📦 Cas particulier : Dictionary

Les "Dictionary" diffèrent de plusieurs façons différentes des autres classes Colections :

- les blocs reçoivent plusieurs arguments
  ```smalltak
  [:k :v | ...]
  ```
  
- on a une prise en charge directe par Java des itérations. Ainsi,
  ```smalltalk
  d do: [ :k :v | System print: (k + v) ]
  ```
  est traité de la façon suivante :
  ```java
  for (entry : map) {
    block.call(List.of(key, value));
  }
  ```

---

## ⚠️ Constraintes importantes

### 1. Aucun accès direct à Java

You cannot do:

```smalltalk
JavaClass new
```

👉 Only registered MiniTalk classes are accessible.

---

### 2. Explicit mapping required

Every Java-backed type must be declared in MTClass:

```java
if (name.equals("List")) return new MTListObject();
```

---

### 3. Dispatch must be preserved

Never bypass:

```java
block.send("value:", ...)
```

❌ DO NOT call block.call(...) directly

---

### 4. Snapshot for iteration

```java
List<MTObject> snapshot = new ArrayList<>(delegate);
```

👉 Prevents concurrent modification issues

---

## 🧪 Example Session

```smalltalk
l := List new.
l add: 1.
l add: 2.

l collect: [ :x | x + 5 ].
```

Output:

```
#(6 7 8)
```

---

## 🧠 Design Summary

| Layer        | Responsibility |
|--------------|----------------|
| Java         | Performance, primitives |
| MiniTalk     | Behavior, abstraction |
| Dispatch     | Bridge between both |

---

## ✅ Conclusion

MiniTalk–Java interoperability is:

- controlled ✅
- explicit ✅
- predictable ✅

It enables a powerful hybrid model while keeping the language consistent.

---

End of document
