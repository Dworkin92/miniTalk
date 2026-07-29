package mt.runtime.primitives;

import mt.runtime.MTArray;
import mt.runtime.MTBoolean;
import mt.runtime.MTDictionary;
import mt.runtime.MTInteger;
import mt.runtime.MTScope;
import mt.runtime.MTObject;

public final class DictionaryPrimitives {

    private DictionaryPrimitives() {
    }

    @Primitive("size")
    public static MTObject size(
            MTObject receiver,
            MTArray arguments,
            MTScope scope) {

        MTDictionary self =
                (MTDictionary) receiver;

        MTInteger result =
            new MTInteger(
                self.size());

        /*
         * Temporaire.
         * Plus tard, recuperer la classe Integer
         * depuis le runtime.
         */
        result.setClazz(receiver.getClazz());

        return result;
    }

    @Primitive("at:")
    public static MTObject at(
            MTObject receiver,
            MTArray arguments,
            MTScope scope) {

        MTDictionary self =
                (MTDictionary) receiver;

        MTObject key =
                arguments.at(0);

        return self.at(key);
    }

    @Primitive("at:put:")
    public static MTObject atPut(
            MTObject receiver,
            MTArray arguments,
            MTScope scope) {

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
            MTArray arguments,
            MTScope scope) {

        MTDictionary self =
                (MTDictionary) receiver;

        MTObject key =
                arguments.at(0);

        return MTBoolean.valueOf(
                self.includesKey(key));
    }
}
