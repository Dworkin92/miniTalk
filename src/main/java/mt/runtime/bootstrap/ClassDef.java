package mt.runtime.bootstrap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ClassDef {

    /**
     * Nom de la classe MiniTalk.
     *
     * Exemple :
     *     Integer
     *     String
     *     Person
     */
    String name();

    /**
     * Nom de la superclasse MiniTalk.
     *
     * Exemple :
     *     Object
     *     Integer
     *     Person
     */
    String superclass() default "Object";

    //Class<?> instancePrimitives() default Void.class;

    //Class<?> classPrimitives() default Void.class;
}
