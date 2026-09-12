/* test-properties.mt */

| Person p |

Person <- Class new.
p <- Person new.

Person addInstProperty: #firstName.
Person addInstProperty: #age.

p <- Person new.

p firstName: 'Jeremy'.

Transcript println: (p name).
