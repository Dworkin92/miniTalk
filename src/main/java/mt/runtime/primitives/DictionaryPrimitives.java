package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTBoolean;
import mt.runtime.MTDictionary;
import mt.runtime.MTInteger;
import mt.runtime.MTObject;

public final class DictionaryPrimitives {

    private DictionaryPrimitives() {
    }

    @Primitive("size")
    public static MTObject size(
            MTObject receiver,
            MTArray arguments) {

        MTDictionary self =
                (MTDictionary) receiver;

        return new MTInteger(
                self.size());
    }

    @Primitive("at:")
    public static MTObject at(
            MTObject receiver,
            MTArray arguments) {

        MTDictionary self =
                (MTDictionary) receiver;

        MTObject key =
                arguments.at(0);

        return self.at(key);
    }

    @Primitive("at:put:")
    public static MTObject atPut(
            MTObject receiver,
            MTArray arguments) {

        MTDictionary self =
                (MTDictionary) receiver;

        MTObject key =
                arguments.at(0);

        MTObject value =
                arguments.at(1);

        self.atPut(
                key,
                value);

        return value;
    }

    @Primitive("includesKey:")
    public static MTObject includesKey(
            MTObject receiver,
            MTArray arguments) {

        MTDictionary self =
                (MTDictionary) receiver;

        MTObject key =
                arguments.at(0);

        return MTBoolean.valueOf(
                self.includesKey(key));
    }
}
