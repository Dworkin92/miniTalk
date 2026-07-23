package mt.runtime.bootstrap;

import mt.runtime.primitives.StringPrimitives;

@ClassDef(
    name = "String",
    superclass = "Object",
    instancePrimitives = StringPrimitives.class
)
public final class StringClassDef {

    private StringClassDef() {
    }
}