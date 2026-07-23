package mt.runtime.bootstrap;

import mt.runtime.primitives.BlockPrimitives;

@ClassDef(
    name = "Block",
    superclass = "Object",
    instancePrimitives = BlockPrimitives.class
)
public final class BlockClassDef {

    private BlockClassDef() {
    }
}