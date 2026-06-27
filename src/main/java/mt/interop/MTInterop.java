package mt.interop;

import mt.runtime.*;

import java.lang.reflect.Method;
import java.util.List;

public final class MTInterop {

    private MTInterop() {}

    public static MTObject sendToInstance(Object receiver, String selector, List<MTObject> args) {
        try {
            for (Method m : receiver.getClass().getMethods()) {
                if (m.getName().equals(selector) && m.getParameterCount() == args.size()) {
                    Object[] javaArgs = args.stream().map(MTInterop::unwrap).toArray();
                    Object result = m.invoke(receiver, javaArgs);
                    return wrap(result);
                }
            }

            throw new MTJavaInteropException("Méthode introuvable: " + selector);

        } catch (Exception e) {
            throw new MTJavaInteropException("Erreur appel Java", e);
        }
    }

public static MTObject sendToClass(Class<?> clazz, String selector, List<MTObject> args) {
    try {
        // constructeur sans argument
        if ("new".equals(selector) && args.isEmpty()) {
            Object instance = clazz.getConstructor().newInstance();
            return wrap(instance);
        }

        // méthodes statiques
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(selector)
                    && m.getParameterCount() == args.size()
                    && java.lang.reflect.Modifier.isStatic(m.getModifiers())) {

                Object[] javaArgs = args.stream().map(MTInterop::unwrap).toArray();
                Object result = m.invoke(null, javaArgs);
                return wrap(result);
            }
        }

        throw new MTJavaInteropException(
                "Méthode statique introuvable: " + selector + " sur " + clazz.getName()
        );

    } catch (MTJavaInteropException e) {
        throw e;
    } catch (Exception e) {
        throw new MTJavaInteropException(
                "Erreur d'invocation sur classe Java: " + clazz.getName() + ", selector=" + selector,
                e
        );
    }
}

    public static Object unwrap(MTObject obj) {
        if (obj instanceof MTInteger i) return i.value();
        if (obj instanceof MTString s) return s.value();
        if (obj instanceof MTBoolean b) return b.value();
        if (obj instanceof MTJavaObject jo) return jo.value();
        return obj;
    }

    public static MTObject wrap(Object value) {
        if (value == null) return MTNil.INSTANCE;
        if (value instanceof Integer i) return new MTInteger(i);
        if (value instanceof String s) return new MTString(s);
        if (value instanceof Boolean b) return new MTBoolean(b);
        return new MTJavaObject(value);
    }
}