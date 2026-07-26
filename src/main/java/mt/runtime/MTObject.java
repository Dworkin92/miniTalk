package mt.runtime;

import java.util.HashMap;
import java.util.Map;
import mt.exceptions.MTRuntimeException;
import mt.debug.MTDebug;

/**
 * la classe Java MTObjet définit  un objet basique minimal
 * dans miniTalk.
 */
public class MTObject {

    /**
     * NEXT_ID est une variable de classe, fournissant un ID unique
     */
    private static long NEXT_ID = 1;

    /**
     * objectId est l'identifiant numérique de l'objet. Il est complété à la création de l'objet
     */
    private final long objectId;

    /**
     * clazz est un pointeur vers la classe de l'objet ("class" étant un mot-cle réservé Java)
     */
    private MTClass clazz;

    /**
     * propertyValues est en fait un dictionnaire Java dont les clés sont des MTSymbol et les valeurs, des MTObject
     */
    private final Map<MTSymbol, MTObject> propertyValues =
            new HashMap<>();

    /* ===============================
     *    méthodes
     * =============================== */

    /**
     * initie un nouvel objet et remplit automatiquement objectId avec un identifiant unique
     */
    public MTObject() {
        this.objectId = NEXT_ID++;
    }

    /**
     * getter pour l'idention objectId
     */
    public long getObjectId() {
        return objectId;
    }

    /**
     * méthode pour envoyer un message de type unary désigné par le MTSymbol selector
     */
    public MTObject send(MTSymbol selector) {
        return send(
            selector,
            new MTArray());
    }

    /**
     * méthode pour envoyer un message binaire (exemple "at:") ou
     * binaire composé d'un ou plusieurs arguments (exemple "at:put:")
     */
    public MTObject send(MTSymbol selector,MTArray arguments) {
        MTDebug.log("[SEND] receiver="
            + this
            + " selector="
            + selector
            + " clazz="
            + clazz);

        MTMethod method = clazz.lookupMethod(selector);


        if (method == null) {
            throw new MTRuntimeException(
                "Unknown selector: " + selector);
        }

        return method.invoke(this, arguments);
    }

    /**
     * getter pour obtenir la classe à laquelle appartien le MTObject
     */
    public MTClass getClazz() {
        return clazz;
    }

    /**
     * setter pour rattacher un MTObject à une classe
     */
    public void setClazz(MTClass clazz) {
        this.clazz = clazz;
    }

    /**
     * méthode pour obtenir la variable de l'objet correspondant au symbole fourni
     */
    public MTObject getProperty(MTSymbol symbol) {

        MTObject value =
                propertyValues.get(symbol);

        return value != null
                ? value
                : MTNil.instance();
    }

    public void setProperty(
            MTSymbol symbol,
            MTObject value) {

        propertyValues.put(symbol, value);
    }

    public void rebindProps() {

        if (clazz == null) {
            return;
        }

        for (MTProperty property :
                clazz.getAllProperties().values()) {

            propertyValues.putIfAbsent(
                    property.getName(),
                    MTNil.instance());
        }
    }


    /*
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    */

    /* methode temporaire : propertyCount */
    public int propertyCount() {
        return propertyValues.size();
    }

}
