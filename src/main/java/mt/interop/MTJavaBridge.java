package mt.interop;

import mt.runtime.MTObject;
import mt.runtime.MTString;

import java.util.List;

public final class MTJavaBridge implements MTObject {

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {

            case "type:" -> {
                String className = ((MTString) args.get(0)).value().trim();

                System.out.println("DEBUG className = [" + className + "]");

                try {
                    Class<?> clazz = Class.forName(className);
                    yield new MTJavaClass(clazz);
                } catch (ClassNotFoundException e) {
                    throw new MTJavaInteropException(
                            "Classe introuvable: '" + className + "'",
                            e
                    );
                }
            }

            default -> throw new MTJavaInteropException(
                    "Message Java inconnu: " + selector
            );
        };
    }

    @Override
    public String toString() {
        return "Java";
    }
}
