"@module Collections"

"Common collection utilities"

Array addMethod: 'average' with: [
  self size = 0 ifTrue: [ ^ nil ].
  self sum / self size
].
