package mt.runtime.bootstrap;

import mt.runtime.primitives.IntegerPrimitives;
/**
 * cette classe utilise une annotation pour générer la classe
 * Intger et sa metaclasse au mayen de la fonction ClassDefInstaller
 */

@ClassDef(
    name = "Integer",
    superclass = "Object",
    instancePrimitives = IntegerPrimitives.class
)
public final class IntegerClassDef {

    private IntegerClassDef() {
    }
}
