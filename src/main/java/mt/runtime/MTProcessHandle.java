package mt.runtime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MTProcessHandle implements MTObject {

    private final Process process;
    private final StringBuilder stdoutBuffer = new StringBuilder();
    private final StringBuilder stderrBuffer = new StringBuilder();
    private final AtomicBoolean stdoutClosed = new AtomicBoolean(false);
    private final AtomicBoolean stderrClosed = new AtomicBoolean(false);
    private final BufferedWriter stdinWriter;

    private MTProcessHandle(Process process) {
        this.process = process;
        this.stdinWriter = new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8)
        );

        startDrainer(process.getInputStream(), stdoutBuffer, stdoutClosed);
        startDrainer(process.getErrorStream(), stderrBuffer, stderrClosed);
    }

    public static MTProcessHandle startShell(String command) {
        try {
            ProcessBuilder pb;
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                pb = new ProcessBuilder("cmd.exe", "/c", command);
            } else {
                pb = new ProcessBuilder("/bin/sh", "-lc", command);
            }

            return new MTProcessHandle(pb.start());
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lancer la commande shell: " + command, e);
        }
    }

    public static MTProcessHandle startExec(List<String> argv) {
        try {
            if (argv.isEmpty()) {
                throw new RuntimeException("exec: commande vide");
            }
            return new MTProcessHandle(new ProcessBuilder(argv).start());
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lancer la commande: " + argv, e);
        }
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {

            case "wait" -> {
                try {
                    int code = process.waitFor();
                    yield new MTInteger(code);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Attente interrompue", e);
                }
            }

            case "exitCode" -> {
                try {
                    yield new MTInteger(process.exitValue());
                } catch (IllegalThreadStateException e) {
                    yield MTNil.INSTANCE; // pas encore terminé
                }
            }

            case "alive" -> new MTBoolean(process.isAlive());

            case "stdout" -> new MTString(stdoutBuffer.toString());

            case "stderr" -> new MTString(stderrBuffer.toString());

            case "stdoutClosed" -> new MTBoolean(stdoutClosed.get());

            case "stderrClosed" -> new MTBoolean(stderrClosed.get());

            case "write:" -> {
                String text = ((MTString) args.get(0)).value();
                try {
                    stdinWriter.write(text);
                    stdinWriter.flush();
                    yield this;
                } catch (IOException e) {
                    throw new RuntimeException("Impossible d'écrire sur stdin du processus", e);
                }
            }

            case "closeInput" -> {
                try {
                    stdinWriter.close();
                    yield this;
                } catch (IOException e) {
                    throw new RuntimeException("Impossible de fermer stdin du processus", e);
                }
            }

            case "destroy" -> {
                process.destroy();
                yield this;
            }

            case "destroyForcibly" -> {
                process.destroyForcibly();
                yield this;
            }

            case "printString" -> new MTString(
                    "Process(alive=" + process.isAlive() + ")"
            );

            default -> throw new RuntimeException("Message inconnu pour ProcessHandle: " + selector);
        };
    }

    private void startDrainer(InputStream stream, StringBuilder buffer, AtomicBoolean closedFlag) {
        Thread t = new Thread(() -> {
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8))) {

                char[] chunk = new char[1024];
                int n;
                while ((n = r.read(chunk)) >= 0) {
                    synchronized (buffer) {
                        buffer.append(chunk, 0, n);
                    }
                }
            } catch (IOException e) {
                synchronized (buffer) {
                    buffer.append("[drain-error] ").append(e.getMessage());
                }
            } finally {
                closedFlag.set(true);
            }
        });

        t.setDaemon(true);
        t.start();
    }

    @Override
    public String toString() {
        return "Process(alive=" + process.isAlive() + ")";
    }
}
