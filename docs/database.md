# Database

Un lien d'interopérabilité a été ouvert dans minitalk
entre le langage et les classes JDBC. Il est donc possible
de se connecter à des bases, de créer/modifier/supprimer des
tables, d'y insérer/modifier/supprimer des lignes, au
moeyn de 3 commandes minitalk toutes simples.

## connection

Pour se connecter à une base local SQLite3 :
```smalltalk
db := Database new.
db connect: 'jdbc:sqlite:test.db'.
```

Pour se connecter à une base réseau PostgreSQL :
```smalltalk
db := Database new.
db connect: '
jdbc:postgresql://localhost/test?user=foo&password=bar
'.
```

## créer une table

Pour créer une table il suffit de demander l'exécution
du requête SQL :

```smalltalk

db execute: '
create table users(
    id integer,
    name text
)
'.
```

A travers la même requête `execute:`, vous pouvez lancer n'importe
quelle requête SQL : `Alter`, `drop`, `insert`, `delete`, etc.

## insérer une ligne

Malheureusement `'` étant le caractère encadrant les chaine en
minitalk, on a un conflit avec les chaines SQL. On est
obligé de placé un échapement sur tous les `'` SQL, ce qui alourdit
la syntaxe.

```smalltalk
db execute: 'insert into users values (1, \'Alice\')'.
```

## faire un select

un select retournant généralement autre chose qu'un état,
il a fallu réaliser une commande spécifique. C'est le rôle
de la commande `query:`.

```smalltalk
rows := db query: 'select * from users'.
System print: rows.
```

publiera une réponse similaire à celle-ci :
```smalltalk
#(
  #{ 'id' -> 1 'name' -> 'Alice' }
)
```

## disconnect

Si on veut être propre, il faut se déconnecter de la base
une fois le travail achevé.

```smalltalk
db disconnect.
```
