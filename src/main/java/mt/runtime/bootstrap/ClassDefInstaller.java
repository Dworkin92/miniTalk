package mt.runtime.bootstrap;

import mt.runtime.bootstrap.ClassDef;
import mt.runtime.MTRuntime;
import mt.runtime.MTClass;
import mt.runtime.MTMetaclass;
import mt.runtime.MTSymbol;
import mt.runtime.primitives.PrimitiveInstaller;
import mt.exceptions.MTBootstrapException;

import java.lang.reflect.Method;

import java.lang.reflect.InvocationTargetException;
import mt.runtime.MTNonLocalReturnException;

public final class ClassDefInstaller {

    private ClassDefInstaller() {
    }

    public static MTClass install(
            MTRuntime runtime,
            Class<?> classDefClass) {

        ClassDef definition =
            classDefClass.getAnnotation(
                ClassDef.class);

        if (definition == null) {
            throw new MTBootstrapException(
                "Missing @ClassDef annotation");
        }

        String className =
            definition.name();

        String superclassName =
            definition.superclass();

        MTClass superclass =
            runtime.classNamed(
                superclassName);

        if (superclass == null) {
            throw new MTBootstrapException(
                "Unknown superclass: "
                + superclassName);
        }

        MTClass clazz =
            new MTClass(
                MTSymbol.intern(
                    className));
        clazz.setSuperclass(
            superclass);

        MTMetaclass metaclazz =
            new MTMetaclass(
                MTSymbol.intern(
                    className + "Class"));
        metaclazz.setSuperclass(
            (MTClass) superclass.getMetaclazz());


        metaclazz.setClazz(runtime.getClassMetaclass());
        metaclazz.setMetaclazz((MTMetaclass)null);

        clazz.setClazz(runtime.getClassClass());
        clazz.setMetaclazz(metaclazz);

        runtime.registerClass(clazz);
        runtime.registerClass(metaclazz);

        /* Installation automatique des primitives */
        Class<?> primitiveClass = definition.instancePrimitives();

        if (primitiveClass != Void.class) {
            PrimitiveInstaller.install(clazz, primitiveClass);
        }

        return clazz;
    }
}
