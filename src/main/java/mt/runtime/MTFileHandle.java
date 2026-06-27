package mt.runtime;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class MTFileHandle implements MTObject, AutoCloseable {

    private enum Mode {
        READ, READB, WRITE, WRITEB, APPEND, APPENDB;

        static Mode fromString(String s) {
            return switch (s) {
                case "read" -> READ;
                case "readb" -> READB;
                case "write" -> WRITE;
                case "writeb" -> WRITEB;
                case "append" -> APPEND;
                case "appendb" -> APPENDB;
                default -> throw new RuntimeException("Mode fichier inconnu: " + s);
            };
        }
    }

    private final String path;
    private final Mode mode;
    private boolean closed = false;

    private BufferedReader reader;
    private InputStream input;
    private BufferedWriter writer;
    private OutputStream output;

    public MTFileHandle(String path, String modeString) {
        try {
            this.path = path;
            this.mode = Mode.fromString(modeString);

            switch (this.mode) {
                case READ -> reader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8);
                case READB -> input = new BufferedInputStream(new FileInputStream(path));
                case WRITE -> writer = Files.newBufferedWriter(Path.of(path), StandardCharsets.UTF_8);
                case APPEND -> writer = new BufferedWriter(new FileWriter(path, true));
                case WRITEB -> output = new BufferedOutputStream(new FileOutputStream(path, false));
                case APPENDB -> output = new BufferedOutputStream(new FileOutputStream(path, true));
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'ouvrir le fichier: " + path + " [" + modeString + "]", e);
        }
    }

    public static boolean exists(String path) {
        return Files.exists(Path.of(path));
    }

    public static void delete(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException("Suppression impossible: " + path, e);
        }
    }

    public static long size(String path) {
        try {
            return Files.size(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException("Taille inaccessible: " + path, e);
        }
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        checkClosed();

        try {
            return switch (selector) {
                case "readLine" -> readLine();
                case "readAll" -> readAll();
                case "readByte" -> readByte();
                case "readBytes:" -> readBytes(((MTInteger) args.get(0)).value());

                case "write:" -> {
                    writeString(((MTString) args.get(0)).value());
                    yield this;
                }
                case "newLine" -> {
                    writeString(System.lineSeparator());
                    yield this;
                }
                case "writeByte:" -> {
                    writeByte(((MTInteger) args.get(0)).value());
                    yield this;
                }
                case "writeBytes:" -> {
                    writeBytes((MTArray) args.get(0));
                    yield this;
                }
                case "flush" -> {
                    flushInternal();
                    yield this;
                }
                case "close" -> {

    		    try {
        		closeInternal();
        		yield MTNil.INSTANCE;
    		    } catch (IOException e) {
        	    throw new RuntimeException("Erreur close: " + path, e);
    		    }

                }
                case "closed" -> new MTBoolean(closed);
                case "mode" -> new MTString(mode.name().toLowerCase());
                case "path" -> new MTString(path);
                case "eof" -> eof();
                case "printString" -> new MTString("File(" + path + ", " + mode.name().toLowerCase() + ")");
                default -> throw new RuntimeException("Message inconnu pour FileHandle: " + selector);
            };
        } catch (IOException e) {
            throw new RuntimeException("Erreur I/O sur " + path + ": " + e.getMessage(), e);
        }
    }

    private MTObject readLine() throws IOException {
        requireMode(Mode.READ);
        String line = reader.readLine();
        return line == null ? MTNil.INSTANCE : new MTString(line);
    }

    private MTObject readAll() throws IOException {
        requireMode(Mode.READ);
        StringBuilder sb = new StringBuilder();
        String line;
        boolean first = true;
        while ((line = reader.readLine()) != null) {
            if (!first) sb.append(System.lineSeparator());
            sb.append(line);
            first = false;
        }
        return new MTString(sb.toString());
    }

    private MTObject readByte() throws IOException {
        requireMode(Mode.READB);
        int b = input.read();
        return b < 0 ? MTNil.INSTANCE : new MTInteger(b);
    }

    private MTObject readBytes(int n) throws IOException {
        requireMode(Mode.READB);
        byte[] buf = input.readNBytes(n);
        List<MTObject> values = new ArrayList<>();
        for (byte b : buf) {
            values.add(new MTInteger(Byte.toUnsignedInt(b)));
        }
        return new MTArray(values);
    }

    private void writeString(String s) throws IOException {
        requireTextWriteMode();
        writer.write(s);
    }

    private void writeByte(int n) throws IOException {
        requireBinaryWriteMode();
        if (n < 0 || n > 255) {
            throw new RuntimeException("writeByte: attend un entier entre 0 et 255");
        }
        output.write(n);
    }

    private void writeBytes(MTArray bytes) throws IOException {
        requireBinaryWriteMode();
        for (int i = 1; i <= ((MTInteger) bytes.send("size", List.of())).value(); i++) {
            MTObject obj = bytes.send("at:", List.of(new MTInteger(i)));
            int n = ((MTInteger) obj).value();
            if (n < 0 || n > 255) {
                throw new RuntimeException("writeBytes: contient une valeur hors intervalle 0..255");
            }
            output.write(n);
        }
    }

    private MTObject eof() throws IOException {
        return switch (mode) {
            case READ -> {
                reader.mark(1);
                int ch = reader.read();
                if (ch < 0) {
                    yield new MTBoolean(true);
                }
                reader.reset();
                yield new MTBoolean(false);
            }
            case READB -> {
                input.mark(1);
                int b = input.read();
                if (b < 0) {
                    yield new MTBoolean(true);
                }
                input.reset();
                yield new MTBoolean(false);
            }
            default -> throw new RuntimeException("eof n'est valide qu'en mode read/readb");
        };
    }

    private void flushInternal() throws IOException {
        if (writer != null) writer.flush();
        if (output != null) output.flush();
    }

    private void closeInternal() throws IOException {
        if (closed) return;

        if (reader != null) reader.close();
        if (input != null) input.close();
        if (writer != null) writer.close();
        if (output != null) output.close();

        closed = true;
    }

    private void checkClosed() {
        if (closed) {
            throw new RuntimeException("Fichier déjà fermé: " + path);
        }
    }

    private void requireMode(Mode expected) {
        if (mode != expected) {
            throw new RuntimeException("Opération invalide en mode " + mode.name().toLowerCase());
        }
    }

    private void requireTextWriteMode() {
        if (!(mode == Mode.WRITE || mode == Mode.APPEND)) {
            throw new RuntimeException("Écriture texte invalide en mode " + mode.name().toLowerCase());
        }
    }

    private void requireBinaryWriteMode() {
        if (!(mode == Mode.WRITEB || mode == Mode.APPENDB)) {
            throw new RuntimeException("Écriture binaire invalide en mode " + mode.name().toLowerCase());
        }
    }


@Override
public void close() {

    try {
        closeInternal();
    } catch (IOException e) {
        throw new RuntimeException("Erreur close: " + path, e);
    }

}

}