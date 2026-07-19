package mt.runtime.bootstrap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface KernelClass {

    String name();

    String superclass() default "Object";
}
