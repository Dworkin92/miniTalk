package mt.runtime.bootstrap;

import mt.runtime.primitives.ArrayPrimitives;

@ClassDef(
    name = "Array",
    superclass = "Object",
    instancePrimitives = ArrayPrimitives.class
)
public final class ArrayClassDef {

    private ArrayClassDef() {
    }
}