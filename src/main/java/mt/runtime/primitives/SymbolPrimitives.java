package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTBoolean;
import mt.runtime.MTObject;
import mt.runtime.MTString;
import mt.runtime.MTSymbol;

public final class SymbolPrimitives {

    private SymbolPrimitives() {
    }

    @Primitive("asString")
    public static MTObject asString(
            MTObject receiver,
            MTArray arguments) {

        MTSymbol symbol =
                (MTSymbol) receiver;

        return new MTString(
                symbol.getValue());
    }

    @Primitive("=")
    public static MTObject equals(
            MTObject receiver,
            MTArray arguments) {

        MTSymbol left =
                (MTSymbol) receiver;

        MTSymbol right =
                (MTSymbol) arguments.at(0);

        return MTBoolean.valueOf(
                left == right);
    }
}
