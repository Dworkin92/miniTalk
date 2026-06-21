"@module Math"

"Math helpers"

Math := Class new: 'Math'.

Math addClassMethod: 'abs:' with: [ :x |
  (x < 0) ifTrue: [ x * -1 ] else: [ x ]
].
