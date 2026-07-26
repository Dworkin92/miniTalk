package mt.runtime.bootstrap;

import mt.runtime.primitives.SymbolPrimitives;
/**
 * Definition of the Symbol class.
 * Symbols are used for method selectors and variable names.
 */
@ClassDef(
    name = "Symbol",
    superclass = "Object",
    instancePrimitives = SymbolPrimitives.class
)
public final class SymbolClassDef {

    private SymbolClassDef() {
    }
}
