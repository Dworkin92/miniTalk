"Array helpers"

Array addMethod: 'sum' with: [
  self inject: 0 into: [ :acc :x | acc + x ]
].

Array addMethod: 'product' with: [
  self inject: 1 into: [ :acc :x | acc * x ]
].

Array addMethod: 'max' with: [
  self size = 0 ifTrue: [ ^ nil ].

  self inject: (self at: 1) into: [ :acc :x |
    (x > acc) ifTrue: [ x ] else: [ acc ]
  ]
].

Array addMethod: 'min' with: [
  self size = 0 ifTrue: [ ^ nil ].

  self inject: (self at: 1) into: [ :acc :x |
    (x < acc) ifTrue: [ x ] else: [ acc ]
  ]
].
