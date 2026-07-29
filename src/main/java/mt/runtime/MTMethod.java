package mt.runtime;

public class MTMethod
        extends MTObject {

    private final MTSymbol selector;

    private final MTClass ownerClass;

    //private final Object body;
    private final MTMethodBody body;

    public MTMethod(
            MTSymbol selector,
            MTClass ownerClass,
            MTMethodBody body) {
            //Object body) {

        this.selector = selector;
        this.ownerClass = ownerClass;
        this.body = body;
    }

    public MTSymbol getSelector() {
        return selector;
    }

    public MTClass getOwnerClass() {
        return ownerClass;
    }

    public MTObject invoke(MTObject receiver, MTArray arguments, MTScope scope) {

        return body.execute(receiver, arguments, scope);
    }

    public Object getBody() {
        return body;
    }
}
