package mt.runtime;

import mt.interpreter.MTInterpreter;

import java.sql.*;
import java.util.*;

public class MTDatabase implements MTObject {

    private Connection conn;

    // ----------------------------
    // constructeur
    // ----------------------------
    public MTDatabase() {
        this.conn = null;
    }

    private MTDatabase(Connection conn) {
        this.conn = conn;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        return switch (selector) {


            case "class" -> {
                MTClass cls = (MTClass) MTInterpreter.GLOBAL.lookup("Database");
                yield cls;
            }

            // ----------------------------
            // connect:
            // ----------------------------
            case "connect:" -> {
                String url = ((MTString) args.get(0)).value().trim();

                try {
                    Connection c = DriverManager.getConnection(url);
                    yield new MTDatabase(c);
                } catch (SQLException e) {
                    throw new RuntimeException("DB connect error: " + e.getMessage());
                }
            }

            // ----------------------------
            // query:
            // ----------------------------
            case "query:" -> {
                ensureConnected();

                String sql = ((MTString) args.get(0)).value();

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    MTArray result = new MTArray(new ArrayList<>());

                    ResultSetMetaData meta = rs.getMetaData();
                    int cols = meta.getColumnCount();

                    while (rs.next()) {

                        MTDictionaryObject row = new MTDictionaryObject();

                        for (int i = 1; i <= cols; i++) {
                            String col = meta.getColumnLabel(i);
                            Object val = rs.getObject(i);

                            row.send("put:value:", List.of(
                                new MTString(col),
                                convert(val)
                            ));
                        }

                        result.send("add:", List.of(row));
                    }

                    yield result;

                } catch (SQLException e) {
                    throw new RuntimeException("DB query error: " + e.getMessage());
                }
            }

            // ----------------------------
            // execute:
            // ----------------------------
            case "execute:" -> {
                ensureConnected();

                String sql = ((MTString) args.get(0)).value();

                try (Statement stmt = conn.createStatement()) {
                    int affected = stmt.executeUpdate(sql);
                    yield new MTInteger(affected);
                } catch (SQLException e) {
                    throw new RuntimeException("DB execute error: " + e.getMessage());
                }
            }

            // ----------------------------
            // disconnect
            // ----------------------------
            case "disconnect" -> {
                if (conn != null) {
                    try {
                        conn.close();
                        conn = null;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                yield MTNil.INSTANCE;
            }


            case "printString" -> {
                yield new MTString("<Database>");
            }

            default -> throw new RuntimeException("Unknown Database message: " + selector);
        };
    }

    // ----------------------------
    // helpers
    // ----------------------------

    private void ensureConnected() {
        if (conn == null) {
            throw new RuntimeException("Database not connected");
        }
    }

    private MTObject convert(Object val) {
        if (val == null) return MTNil.INSTANCE;

        if (val instanceof Integer i) return new MTInteger(i);
        if (val instanceof Long l) return new MTInteger(l.intValue());
        if (val instanceof Double d) return new MTFloat(d);

        return new MTString(val.toString());
    }
}
