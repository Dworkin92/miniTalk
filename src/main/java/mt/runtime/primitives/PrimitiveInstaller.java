package mt.runtime.primitives;

import java.lang.reflect.Method;

import mt.runtime.MTArray;
import mt.runtime.MTClass;
import mt.runtime.MTMethod;
import mt.runtime.MTMethodBody;
import mt.runtime.MTObject;
import mt.runtime.MTSymbol;

import java.lang.reflect.InvocationTargetException;
import mt.runtime.MTNonLocalReturnException;

public final class PrimitiveInstaller {

    private PrimitiveInstaller() {
    }

    public static void install(MTClass clazz, Class<?> primitiveClass) {
        for (Method method : primitiveClass.getDeclaredMethods()) {

            Primitive[] annotations = method.getAnnotationsByType(Primitive.class);

            if (annotations.length == 0) {
                continue;
            }

            for (Primitive annotation : annotations) {
                clazz.addMethod(
                    new MTMethod(
                        MTSymbol.intern(annotation.value()),
                        clazz,
                        (receiver, arguments) -> {

                            try {
                                return (MTObject)method.invoke(null,receiver,arguments);

                            }
                            catch (InvocationTargetException ex) {
                                Throwable cause = ex.getCause();
                                if (cause instanceof MTNonLocalReturnException nlr) {
                                    throw nlr;
                                }
                                throw new RuntimeException(cause);
                            }
                            catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    )
                );
            }
        }
    }
}

