"@module Collections"

"Common collection utilities"

Array addMethod: 'average' with: [
  self size = 0 ifTrue: [ ^ nil ].
  self sum / self size
].

"============================="
" Collection core methods "
"============================="

Collection addMethod: 'collect:' with: [ :block |
    result := Array new.
    self do: [ :each |
        result add: (block value: each)
    ].
    result
].

Collection addMethod: 'map:' with: [ :block |
    self collect: block
].

Collection addMethod: 'select:' with: [ :block |
    result := Array new.
    self do: [ :each |
        (block value: each) ifTrue: [
            result add: each
        ]
    ].
    result
].

Collection addMethod: 'filter:' with: [ :block |
    self select: block
].

Collection addMethod: 'reject:' with: [ :block |
    result := Array new.
    self do: [ :each |
        (block value: each) ifFalse: [
            result add: each
        ]
    ].
    result
].

Collection addMethod: 'inject:into:' with: [ :initial :block |
    acc := initial.
    self do: [ :each |
        acc := block value: acc value: each
    ].
    acc
].

Collection addMethod: 'reduce:with:' with: [ :initial :block |
    self inject: initial into: block
].
