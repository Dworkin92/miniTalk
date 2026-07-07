package mt.runtime.primitives;

import java.lang.reflect.Method;

import mt.runtime.MTArray;
import mt.runtime.MTClass;
import mt.runtime.MTMethod;
import mt.runtime.MTMethodBody;
import mt.runtime.MTObject;
import mt.runtime.MTSymbol;

public final class PrimitiveInstaller {

    private PrimitiveInstaller() {
    }

    public static void install(
            MTClass clazz,
            Class<?> primitiveClass) {

        for (Method method :
                primitiveClass.getDeclaredMethods()) {

            Primitive annotation =
                    method.getAnnotation(
                            Primitive.class);

            if (annotation == null) {
                continue;
            }

            clazz.addMethod(

                new MTMethod(
                    MTSymbol.intern(
                            annotation.value()),
                    clazz,
                    (receiver, arguments) -> {

                        try {

                            return (MTObject)
                                    method.invoke(
                                            null,
                                            receiver,
                                            arguments);

                        } catch (Exception e) {

                            throw new RuntimeException(
                                    e);
                        }
                    }));
        }
    }
}

