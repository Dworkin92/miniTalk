# Process : exécuter des commandes

MiniTalk peut lancer des processus externes et récupérer leurs flux.

## Lancer une commande shell

```smalltalk
p := Process shell: 'echo Bonjour'.
code := p wait.
out := p stdout.
err := p stderr.
```

## Vérifier le code retour

```smalltalk
code = 0 ifTrue: [ 'OK' ] ifFalse: [ 'KO' ].
```

## Utiliser `exitCode`

`wait` bloque et retourne le code retour.

`exitCode` est utile pour savoir si un processus déjà lancé s’est terminé :

```smalltalk
p := Process shell: 'ping localhost -n 2'.
p exitCode.  " nil tant que le process tourne "
p wait.
p exitCode.  " maintenant: entier "
```

## Lire les flux

```smalltalk
p stdout.
p stderr.
```

## Écrire sur stdin

```smalltalk
p := Process shell: 'sort'.
p write: 'z
b
a
'.
p closeInput.
p wait.
p stdout.
```

## Contrôle du processus

```smalltalk
p alive.
p destroy.
p destroyForcibly.
```

## Exemple de script robuste

```smalltalk
p := Process shell: 'dir'.
code := p wait.

code = 0
    ifTrue: [ p stdout ]
    ifFalse: [ p stderr ].
```
