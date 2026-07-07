package mt.runtime;

public final class MTRuntime {

    private final MTClass objectClass;

    private final MTClass classClass;

    private final MTMetaclass objectMetaclass;

    private final MTMetaclass classMetaclass;

    private final MTDictionary classes = new MTDictionary();

    public MTRuntime(
            MTClass objectClass,
            MTClass classClass,
            MTMetaclass objectMetaclass,
            MTMetaclass classMetaclass) {

        this.objectClass = objectClass;
        this.classClass = classClass;
        this.objectMetaclass = objectMetaclass;
        this.classMetaclass = classMetaclass;
    }

    public MTClass getObjectClass() {
        return objectClass;
    }

    public MTClass getClassClass() {
        return classClass;
    }

    public MTMetaclass getObjectMetaclass() {
        return objectMetaclass;
    }

    public MTMetaclass getClassMetaclass() {
        return classMetaclass;
    }


    public void registerClass(MTClass clazz) {
        classes.atPut(
            clazz.getName(),
            clazz);
    }


    public MTClass classNamed(String name) {
        return (MTClass) classes.at(
                MTSymbol.intern(name));
    }

    public MTClass createClass(
        String name,
        MTClass superclass) {

        MTClass clazz =
            new MTClass(
                    MTSymbol.intern(name));

        clazz.setSuperclass(
            superclass);

        clazz.setClazz(
            classClass);

        return clazz;
    }
}
