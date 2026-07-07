package mt.runtime.bootstrap;

import mt.runtime.*;

public final class MTRuntimeBootstrap {

    private MTRuntimeBootstrap() {
    }

    public static MTRuntime bootstrap() {

        /*
         * Construction brute
         */

        MTClass objectClass =
                new MTClass(
                        MTSymbol.intern("Object"));

        MTClass classClass =
                new MTClass(
                        MTSymbol.intern("Class"));

        MTMetaclass objectMetaclass =
                new MTMetaclass(
                        MTSymbol.intern("ObjectClass"));

        MTMetaclass classMetaclass =
                new MTMetaclass(
                        MTSymbol.intern("ClassClass"));

        /*
         * Relier la hierarchie
         */

        objectClass.setSuperclass(
                null);

        classClass.setSuperclass(
                objectClass);

        objectMetaclass.setSuperclass(
                classMetaclass);

        classMetaclass.setSuperclass(
                objectMetaclass);

        /*
         * Relier les classes
         */

        objectClass.setClazz(
                objectMetaclass);

        classClass.setClazz(
                classMetaclass);

        objectMetaclass.setClazz(
                classMetaclass);

        classMetaclass.setClazz(
                classMetaclass);

        return new MTRuntime(
                objectClass,
                classClass,
                objectMetaclass,
                classMetaclass);
    }
}
