package mt.runtime.bootstrap;

import mt.runtime.primitives.BooleanPrimitives;

@ClassDef(
    name = "Boolean",
    superclass = "Object",
    instancePrimitives = BooleanPrimitives.class
)
public final class BooleanClassDef {

    private BooleanClassDef() {
    }
}
