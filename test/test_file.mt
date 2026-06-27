"@module test_file"

System print: 'HELLO TEST'.

assert := [:cond :msg |
    System print: ('[TEST] ' , msg).
    cond ifFalse: [
        System print: '   -> FAIL'.
        ^ false
    ].
    System print: '   -> OK'.
    true
].

System print: '===== FILE TESTS ====='.

System print: '-- Test 1: write/read --'.

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

System print: '-- Test 2: append --'.

f <- File open: 'test.txt' mode: 'append'.
f newLine.
f write: 'again'.
f close.

f <- File open: 'test.txt' mode: 'read'.

f readLine.
f readLine.
line <- f readLine.

assert value: (line = 'again') value: 'append ok'.

f close.

System print: '-- Test 3: exists/delete --'.

assert value: (File exists: 'test.txt') value: 'exists true'.

File delete: 'test.txt'.

assert value: ((File exists: 'test.txt') not) value: 'exists false'.

System print: '-- Test 4: readAll --'.

f <- File open: 'test.txt' mode: 'write'.
f write: 'a'.
f newLine.
f write: 'b'.
f close.

f <- File open: 'test.txt' mode: 'read'.

content <- f readAll.

assert value: (content = 'a
b') value: 'readAll'.

f close.

System print: '-- Test 5: binary read/write --'.

f <- File open: 'bin.dat' mode: 'writeb'.
f writeBytes: #(65 66 67 10).
f close.

f <- File open: 'bin.dat' mode: 'readb'.

bytes <- f readBytes: 4.

assert value: (bytes size = 4) value: 'byte size'.
assert value: ((bytes at: 1) = 65) value: 'byte 1'.
assert value: ((bytes at: 2) = 66) value: 'byte 2'.

f close.

System print: '-- Test 6: eof --'.

f <- File open: 'bin.dat' mode: 'readb'.

f readBytes: 4.

assert value: (f eof) value: 'EOF true'.

f close.

System print: '-- Cleanup --'.

File delete: 'test.txt'.
File delete: 'bin.dat'.

System print: '===== END FILE TESTS ====='.
System print: 'ALL TESTS COMPLETED'.
