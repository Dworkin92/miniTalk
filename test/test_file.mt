"@module test_file"

assert := [:cond :msg |
    msg printString.
    cond ifFalse: [
        'FAIL' printString.
        ^ false
    ].
    'OK' printString.
    true
].

'===== FILE TESTS =====' printString.

'-- Test 1: write/read --' printString.

File delete: 'test.txt'.

f <- File open: 'test.txt' mode: 'write'.
f write: 'hello'.
f newLine.
f write: 'world'.
f close.

f <- File open: 'test.txt' mode: 'read'.

assert value: (f readLine = 'hello') value: 'readLine 1'.
assert value: (f readLine = 'world') value: 'readLine 2'.
assert value: (f readLine = nil) value: 'readLine EOF'.

f close.

'-- Test 2: append --' printString.

f <- File open: 'test.txt' mode: 'append'.
f newLine.        "IMPORTANT"
f write: 'again'.
f close.

f <- File open: 'test.txt' mode: 'read'.

f readLine.  "hello"
f readLine.  "world"
line <- f readLine.

assert value: (line = 'again') value: 'append ok'.

f close.

'-- Test 3: exists/delete --' printString.

assert value: (File exists: 'test.txt') value: 'exists true'.

File delete: 'test.txt'.

assert value: ((File exists: 'test.txt') not) value: 'exists false'.

'-- Test 4: readAll --' printString.

f <- File open: 'test.txt' mode: 'write'.
f write: 'a'.
f newLine.
f write: 'b'.
f close.

f <- File open: 'test.txt' mode: 'read'.

content <- f readAll.

"On évite String value: → on compare directement"
assert value: (content = 'a
b') value: 'readAll'.

f close.

'-- Test 5: binary read/write --' printString.

f <- File open: 'bin.dat' mode: 'writeb'.
f writeBytes: #(65 66 67 10).
f close.

f <- File open: 'bin.dat' mode: 'readb'.

bytes <- f readBytes: 4.

assert value: (bytes size = 4) value: 'byte size'.
assert value: ((bytes at: 1) = 65) value: 'byte 1'.
assert value: ((bytes at: 2) = 66) value: 'byte 2'.

f close.

'-- Test 6: eof --' printString.

f <- File open: 'bin.dat' mode: 'readb'.

f readBytes: 4.

assert value: (f eof) value: 'EOF true'.

f close.

'-- Cleanup --' printString.

File delete: 'test.txt'.
File delete: 'bin.dat'.

'===== END FILE TESTS =====' printString.