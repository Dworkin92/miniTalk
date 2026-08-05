package mt.runtime;

public final class MTRuntime {

    private final MTClass objectClass;

    private final MTClass classClass;

    private final MTClass objectMetaclass;

    private final MTClass classMetaclass;

    private final MTDictionary classes = new MTDictionary();

    public MTRuntime(
            MTClass objectClass,
            MTClass classClass,
            MTClass objectMetaclass,
            MTClass classMetaclass) {

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

    public MTClass getObjectMetaclass() {
        return objectMetaclass;
    }

    public MTClass getClassMetaclass() {
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

    public int classCount() {
        return classes.size();
    }

    public boolean includesClass(String name) {
        return classes.includesKey(
            MTSymbol.intern(name));
    }

    public MTArray getClasses() {

        return classes.values();
    }
}
