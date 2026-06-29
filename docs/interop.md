# 🔗 MiniTalk – Java Interoperability Guide

## 🧭 Overview

MiniTalk allows interaction with its Java runtime layer. This interoperability is limited but powerful: it enables access to Java-backed objects (Array, List, Set, Dictionary, etc.) and their native behaviors.

This document explains how MiniTalk code interacts with Java classes, how objects are instantiated, and how message dispatch bridges the two worlds.

---

## 🧱 Core Principle

MiniTalk does NOT directly expose arbitrary Java classes.

Instead, it relies on **Java-backed runtime objects**:

| MiniTalk Class | Java Implementation |
|----------------|---------------------|
| Array          | MTArray             |
| List           | MTListObject        |
| Set            | MTSetObject         |
| Dictionary     | MTDictionaryObject  |

👉 These classes act as a gateway between MiniTalk and Java.

---

## 🏗️ Object Creation

### MiniTalk side

```smalltalk
r := Array new.
```

### Java side

```java
if (name.equals("Array")) {
    return new MTArray(new ArrayList<MTObject>());
}
```

👉 This mapping is **explicit and mandatory**.

---

## 🔄 Message Dispatch Pipeline

When sending a message:

```smalltalk
r add: 1.
```

Execution path:

1. `MTArray.send("add:")`
2. Java implementation (if exists)
3. Otherwise → fallback to miniTalk (`Collection`)

---

## 🔁 Fallback Mechanism

Collections use a fallback system:

```java
dispatchWithFallback(selector, args)
```

Steps:

1. Try Java implementation (MTCollectionObject)
2. Lookup in MiniTalk class `Collection`
3. Execute method via `callWithReceiver`

---

## ✅ Example: Hybrid Behavior

### MiniTalk

```smalltalk
#(1 2 3) collect: [ :x | x + 1 ]
```

### Execution

- `collect:` → defined in MiniTalk
- calls `do:` → implemented in Java

👉 hybrid execution ✅

---

## 🧩 Blocks and Java

Blocks originate in MiniTalk but execute via Java:

```smalltalk
l do: [ :x | x + 1 ]
```

Java executes:

```java
block.send("value:", List.of(each));
```

👉 Blocks are invoked through message passing, not direct function calls.

---

## 📦 Dictionary Special Case

Dictionary differs:

- Java handles iteration
- Blocks receive multiple arguments

```smalltalk
d do: [ :k :v | System print: (k + v) ]
```

---

## ⚠️ Important Constraints

### 1. No direct Java access

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
