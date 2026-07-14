package mt.runtime.primitives;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Primitives {

    Primitive[] value();
}
