"@module test_process"


p := Process shell: 'dir'.
code := p wait.
out := p stdout.
err := p stderr.

('Resultat : CR = ', code printString) printString.


p := Process shell: 'ping localhost -n 2'.
p exitCode   "=> nil tant que pas fini"
p wait.
p exitCode   "=> 0 ou autre"


p := Process exec: #('cmd.exe' '/c' 'echo' 'Bonjour').
p wait.
p stdout
