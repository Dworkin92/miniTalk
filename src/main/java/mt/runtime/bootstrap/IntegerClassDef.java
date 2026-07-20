package mt.runtime.bootstrap;

/**
 * cette classe utilise une annotation pour générer la classe
 * Intger et sa metaclasse au mayen de la fonction ClassDefInstaller
 */

@ClassDef(
    name = "Integer",
    superclass = "Object"
)
public final class IntegerClassDef {

    private IntegerClassDef() {
    }
}
