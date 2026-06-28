# 📚 MiniTalk – Standard Library Usage Guide

## 🧭 Overview

This document summarizes the complete usage of the MiniTalk standard library (stdlib).
It covers collections, blocks, and common message patterns.

---

## 🧱 Message Sending

General form:

```
receiver selector: argument
```

Chaining requires parentheses:

```
(receiver message1: ...) message2: ...
```

---

## 🧩 Blocks

### Definition

```
[ :x | x + 1 ]
```

### Usage

```
b := [ :x | x * 2 ].
b value: 5. "→ 10"
```

Multiple arguments:

```
[ :a :b | a + b ] value:value: 2 3.
```

---

## 📦 Collections

### Types

| Type        | Creation            |
|-------------|--------------------|
| Array       | #(1 2 3) / Array new |
| List        | List new           |
| Set         | Set new            |
| Dictionary  | Dictionary new     |

---

## 🔁 Iteration

```
collection do: [ :x | ... ].
```

Dictionary:

```
d do: [ :k :v | ... ].
```

---

## 🔄 Transformation

### collect / map

```
#(1 2 3) collect: [ :x | x + 1 ].
```

Dictionary:

```
d collect: [ :k :v | v + 1 ].
```

---

## 🔍 Filtering

```
#(1 2 3 4) select: [ :x | x > 2 ].
#(1 2 3 4) reject: [ :x | x = 2 ].
```

Dictionary:

```
d select: [ :k :v | v > 10 ].
```

---

## ➕ Reduction

```
#(1 2 3) inject: 0 into: [ :acc :x | acc + x ].
```

Dictionary:

```
d inject: 0 into: [ :acc :k :v | acc + v ].
```

---

## 🧪 Composition

```
(#(1 2 3)
    collect: [ :x | x + 1 ])
    select: [ :x | x > 2 ].
```

---

## 📌 Dictionary specific

### Insert

```
d put: 1 value: 10.
```

### Access

```
d at: 1.
```

### Keys / Values

```
d keys.
d values.
```

---

## ⚠️ Notes

- Set order is NOT guaranteed
- Dictionary blocks use two parameters (k, v)
- Parentheses required for chaining

---

## ✅ Summary

MiniTalk stdlib provides:

- polymorphic collections
- dynamic dispatch
- functional-style operations

---

End of document
