package mt.runtime.bootstrap;

import mt.runtime.primitives.DictionaryPrimitives;

@ClassDef(
    name = "Dictionary",
    superclass = "Object",
    instancePrimitives = DictionaryPrimitives.class
)
public final class DictionaryClassDef {

    private DictionaryClassDef() {
    }
}