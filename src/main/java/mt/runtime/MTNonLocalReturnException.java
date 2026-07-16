package mt.runtime;

import mt.exceptions.MTRuntimeException;

public class MTNonLocalReturnException
        extends RuntimeException {

    private final MTObject value;

    private final MTBlock targetBlock;

    public MTNonLocalReturnException(MTObject value) {

        this(value, null);
    }

    public MTNonLocalReturnException(
            MTObject value, MTBlock targetBlock) {

        this.value = value;
        this.targetBlock = targetBlock;
    }

    public MTObject getValue() {

        return value;
    }

    public MTBlock getTargetBlock() {
        return targetBlock;
    }
}
