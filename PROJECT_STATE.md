\# MiniTalk - Project State



Last update: 2025-07-05



\## Vision



MiniTalk est un langage de scripting orienté objet inspiré de Smalltalk.



Objectifs :



\- syntaxe simple et lisible

\- environnement de scripting plus agréable que le shell

\- accès progressif à l'écosystème Java

\- apprentissage des techniques d'implémentation des langages



\---



\# État du projet



\## Lexer



Implémenté :



\- Integer literals

\- String literals

\- Boolean literals

\- nil

\- Variables

\- Assignments (:=)

\- Binary selectors

\- Block syntax

\- Temporaries

\- Comments

\- META directives



META support :



```smalltalk

/\*

@module Core;

@import Collections;

\*/

```



Tokens spécifiques :



\- LCO\*MENT

\- RCOMMENT

\- META



\---



\##\*Parser



Implémenté :



\### Expressi\*ns



\- Variables

\- Assignments

\- Se\*uences

\- Parent\*esized expressions



\### Messages



\* Unary messages



```small\*alk

\[42] value

```



\- Binary messa\*es



```smalltalk

3 + 4

```



\- Keyw\*rd messages



```smalltalk

true\*ifTrue: \[42]

```



\### Blocks



\- pa\*ameters\*

```smalltalk

\[:x :y | ...]

```



\-\*temporaries\*

```smalltalk

\[

&#x20;  \*| z |

]

```



\### Control\*flow syntax



\- ifTrue:

\- ifFalse:

\* if\*rue:ifFalse:

\- whileTrue:

\- whileF\*lse:

\- to:do:



\### Returns



```sma\*ltalk

^42\*```



\### META directives



Parser i\*terpre\*s:



```smalltalk

@module\*Name;

@import OtherModule;

```



\*vailable\*through:



```java

parser.get\*odule\*ame()

parser.getImports()

```



\---\*

\## Runtime



Implemented :



\### Ob\*ects



\- MTObject

\- MTClass

\- MTInt\*ger

\- MTBoolean

\- MTString

\- MTArr\*y

\- MTDictionary

\- MTNil



\### Scop\*s



\- lexical\*scope chain

\- captured\*scopes\*

\### Closures



Supported.



Example\*:



```smalltalk

\[:x |

&#x20;   \[:y |

&#x20; \*     (x + y) \* factor

&#x20;   ]

]

```

\*### Non\*Local Returns



Implemented using :\*

```java

MTNonLocalReturnException\*```



Supports genuine\*Smalltalk-style non-local return.

\*---



\## Integer primitives



Implem\*nted :



```\*mall\*alk

\+

\-

\*

/

%

=

<>

<

>

\~<

>\~

```



\*liases :



```smalltalk

==

!=

\*=

>=

```



\---



\## Boolean primitiv\*s



Implemented :



```small\*alk

not

and:

or:

xor:

```



\---



\##\*Control Flow



Implemented :



```sm\*ll\*alk

ifTrue:

ifFalse:

ifTrue\*ifFalse:



whileTrue:

whileFalse:



\*o:do:

```



Behaviour :



```\*malltalk

1 to\* 5 do: \[...]

```



and



```smalltal\*

5\*to: 1 do: \[...]

```



are both\*supported.



Descending iterations\*automatically use a step of -1.



\-\*-



\## Module System



Partially imp\*emented.



Supported syntax :



```s\*alltalk

/\*

@module Core;

@import C\*llections;

\*/

```



Current capabil\*ties :



\- parser extracts module n\*me

\- parser extracts imports



Not \*et implemented :



\- module loader

\* dependency resolution

\- import ex\*cution



\---



\## File Execution



Im\*lemented.



Example :



```powershel\*

java -\*ar miniTalk.jar fibonacci.mt

```



\*cripts are loaded through:



```jav\*

MTLoader

```



\*ipeline :



```text

File

&#x20;-> Lexer

\*-> Parser

&#x20;-> Interpreter

```



\---\*

\## Examples



Currently working :

\*```text\*\*xamples/

&#x20;   fibonacci.mt

&#x20;   clos\*res.mt

&#x20;   closureCounter.mt

&#x20;   n\*stedClosure.mt

```



Results :



```\*ext

fibonacci.mt

\-> 55



closures.m\*

\-> 60



closureCounter.mt

\-> 3



ne\*tedClosure.mt

\-> 90

```



\---



\# Kn\*wn Issues



\## Bootstrap



Runtime b\*otstrap and kernel bootstrap are n\*t fully unified.



Current concern\*



\- Object\*exists in runtime bootstrap

\- Inte\*er\*Boolean/String are still created s\*parately



Consequence:



```smallta\*k

42 println

```



currently fails\*because\*classes are not yet connected to O\*ject through superclass relationsh\*ps.



A future bootstrap refactorin\* is required.



\---



\# Testing



Reg\*ession tests split into modules :

\*```text

ParserRegressionTests

Bloc\*RegressionTests

PrimitiveRegressio\*Tests

ControlFlowRegressionTests

M\*duleRegressionTests

NonLocalReturn\*egressionTests

```



Common helper \*



```java

TestUtils.assertResult(.\*.)

```



\---



\# Immediate Next Step\*



Priority 1



\- Complete bootstrap\*hierarchy

\- Connect Integer/Boolea\*/String/Array/Dictionary to Object\*- Make Object-level primitives inh\*rited



Priority 2



\- ObjectPrimiti\*es

&#x20;   - print

&#x20;   - println



Prio\*ity 3



\- Module loader

\- Import re\*olution

\- Dependency tracking



Pri\*rity 4



\- Library loading at start\*p



\---



\# Long-Term Ideas



\- REPL

\* Standard library

\- Java bridge

\- \*elf / Super

\- User-defined classes\*- Coroutines

\- Multitasking

\- Shar\*d object synchronization

