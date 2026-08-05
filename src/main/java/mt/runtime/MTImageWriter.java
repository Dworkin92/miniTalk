package mt.runtime;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import mt.runtime.*;

public final class MTImageWriter {

    private MTImageWriter() {
    }

    public static void save(
            MTRuntime runtime,
            Path file)
            throws IOException {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(file)) {

            writer.write(
                    "imageVersion = 1");
            writer.newLine();
            writer.newLine();

            MTArray classes =
                    runtime.getClasses();

            for (int i = 0;
                 i < classes.size();
                 i++) {

                MTClass clazz =
                        (MTClass)
                                classes.at(i);

                boolean isMetaclass =
                        clazz.getName()
                             .toString()
                             .endsWith(
                                     "Class");

                writer.write(
                        isMetaclass
                                ? "[[metaclass]]"
                                : "[[class]]");

                writer.newLine();

                writer.write(
                        "name = \""
                        + clazz.getName()
                        + "\"");
                writer.newLine();

                writer.write(
                        "superclass = \""
                        + (clazz.getSuperclass()
                                == null
                           ? ""
                           : clazz.getSuperclass()
                                  .getName())
                        + "\"");

                writer.newLine();

                writer.write(
                        "metaclass = \""
                        + (clazz.getMetaclazz()
                                == null
                           ? ""
                           : clazz.getMetaclazz()
                                  .getName())
                        + "\"");

                writer.newLine();

                writeProperties(writer, clazz);
                /*
                writer.write(
                        "instanceVariables = [");

                MTArray vars =
                        clazz.getInstanceVariables();
*/

                writer.newLine();
                writer.newLine();
            }
        }
    }

    private static  void writeProperties(
        BufferedWriter writer,
        MTClass clazz)
        throws IOException {

    Map<MTSymbol, MTProperty> properties =
            clazz.getDeclaredProperties();

    for (MTProperty property
            : properties.values()) {

        writer.write(
                "[[class.property]]");
        writer.newLine();

        writer.write(
                "name = \""
                + property.getName()
                + "\"");
        writer.newLine();

        MTObject value =
                property.getValue();

        if (!(value instanceof MTNil)) {

            writer.write(
                    "value = \""
                    + value
                    + "\"");

            writer.newLine();
        }

        writer.newLine();
    }
}
}
