package mt.runtime;

import java.util.ArrayList;
import java.util.List;

public final class MTString implements MTObject {
    private final String value;

    public MTString(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {
            // ------------------------------------------------------------
            // Base
            // ------------------------------------------------------------
            case "size" -> new MTInteger(value.length());

            case "isEmpty" -> new MTBoolean(value.isEmpty());

            case "notEmpty" -> new MTBoolean(!value.isEmpty());

            case "," -> {
                MTString other = requireString(args, 0, selector);
                yield new MTString(value + other.value());
            }

            case "*" -> {
                MTInteger count = requireInteger(args, 0, selector);
                int n = count.value();
                if (n < 0) {
                    throw new RuntimeException("Multiplication par un entier négatif interdite");
                }

                StringBuilder result = new StringBuilder(value.length() * n);
                for (int i = 0; i < n; i++) {
                    result.append(value);
                }
                yield new MTString(result.toString());
            }

            case "=" -> {
                if (args.get(0) instanceof MTString s) {
                    yield new MTBoolean(value.equals(s.value()));
                }
                yield new MTBoolean(false);
            }

            case "!=", "<>" -> {
                MTBoolean eq = (MTBoolean) send("=", args);
                yield new MTBoolean(!eq.value());
            }

            // ------------------------------------------------------------
            // Accès / extraction
            // Index miniTalk 1-based, comme MTArray>>at:
            // ------------------------------------------------------------
            case "at:" -> {
                int index = requireInteger(args, 0, selector).value();
                checkIndex1Based(index);
                yield new MTString(String.valueOf(value.charAt(index - 1)));
            }

            case "first" -> {
                if (value.isEmpty()) {
                    yield MTNil.INSTANCE;
                }
                yield new MTString(String.valueOf(value.charAt(0)));
            }

            case "last" -> {
                if (value.isEmpty()) {
                    yield MTNil.INSTANCE;
                }
                yield new MTString(String.valueOf(value.charAt(value.length() - 1)));
            }

            case "copyFrom:to:" -> {
                int from = requireInteger(args, 0, selector).value();
                int to = requireInteger(args, 1, selector).value();

                if (from < 1 || to < from - 1 || to > value.length()) {
                    throw new RuntimeException(
                            "copyFrom:to: bornes invalides: " + from + " à " + to
                    );
                }

                // Autorise une tranche vide conventionnelle : copyFrom: n to: n-1
                if (to == from - 1) {
                    yield new MTString("");
                }

                yield new MTString(value.substring(from - 1, to));
            }

            case "substringFrom:" -> {
                int from = requireInteger(args, 0, selector).value();

                if (from < 1 || from > value.length() + 1) {
                    throw new RuntimeException("substringFrom: borne invalide: " + from);
                }

                yield new MTString(value.substring(from - 1));
            }

            case "substringTo:" -> {
                int to = requireInteger(args, 0, selector).value();

                if (to < 0 || to > value.length()) {
                    throw new RuntimeException("substringTo: borne invalide: " + to);
                }

                yield new MTString(value.substring(0, to));
            }

            // ------------------------------------------------------------
            // Trim / casse
            // ------------------------------------------------------------
            case "trim" -> new MTString(value.trim());

            case "trimLeft" -> new MTString(stripLeft(value));

            case "trimRight" -> new MTString(stripRight(value));

            case "toLower" -> new MTString(value.toLowerCase());

            case "toUpper" -> new MTString(value.toUpperCase());

            // ------------------------------------------------------------
            // Recherche
            // ------------------------------------------------------------
            case "startsWith:" -> {
                MTString prefix = requireString(args, 0, selector);
                yield new MTBoolean(value.startsWith(prefix.value()));
            }

            case "endsWith:" -> {
                MTString suffix = requireString(args, 0, selector);
                yield new MTBoolean(value.endsWith(suffix.value()));
            }

            case "indexOf:" -> {
                MTString needle = requireString(args, 0, selector);
                int index = value.indexOf(needle.value());

                // Convention Smalltalk-like : 0 si absent, sinon index 1-based
                yield new MTInteger(index < 0 ? 0 : index + 1);
            }

            case "includes:" -> {
                MTString needle = requireString(args, 0, selector);
                yield new MTBoolean(value.contains(needle.value()));
            }

	    //-------------------------------------------------------------
	    // méthodes avec expressions régulières
	    //-------------------------------------------------------------
	    case "matches:" -> {
    		MTString pattern = requireString(args, 0, selector);
    		yield new MTBoolean(value.matches(pattern.value()));
	    }

	    case "containsMatch:" -> {
    		// --- validation argument ---
    		if (args.size() < 1) {
        		throw new RuntimeException("containsMatch: argument manquant");
    		}

    		if (!(args.get(0) instanceof MTString patternArg)) {
        		throw new RuntimeException(
                		"containsMatch: String attendu, reçu: " + args.get(0)
        		);
    		}

    		String patternText = patternArg.value();

    		// --- compilation regex ---
    		java.util.regex.Pattern pattern;
    		try {
        		pattern = java.util.regex.Pattern.compile(patternText);
    		} catch (java.util.regex.PatternSyntaxException e) {
        		throw new RuntimeException(
                	"containsMatch: regex invalide: " + patternText, e );
    		}

    		// --- matching ---
    		java.util.regex.Matcher matcher = pattern.matcher(value);

    		// find() = au moins une occurrence
    		boolean found = matcher.find();

    		yield new MTBoolean(found);
	    }


	    case "allMatches:" -> {
    		MTString pattern = requireString(args, 0, selector);
    		java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern.value());
    		java.util.regex.Matcher m = p.matcher(value);

    		List<MTObject> result = new ArrayList<>();

    		while (m.find()) {
        		result.add(new MTString(m.group()));
    		}

    		yield new MTArray(result);
	    }



	    case "reduce:with:" -> {
    		MTObject initial = args.get(0);
    		MTBlockObject block = requireBlock(args, 1, selector);

    		MTObject acc = initial;

    		for (int i = 0; i < value.length(); i++) {
        		String ch = String.valueOf(value.charAt(i));
        		acc = block.call(List.of(acc, new MTString(ch)));
    		}

    		yield acc;
	    }

	    case "firstMatch:" -> {
    		MTString patternArg = requireString(args, 0, selector);

    		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patternArg.value());
    		java.util.regex.Matcher matcher = pattern.matcher(value);

    		if (matcher.find()) {
        		yield new MTString(matcher.group());
    		}

    		yield MTNil.INSTANCE;
	    }

            // ------------------------------------------------------------
            // Découpage
            // ------------------------------------------------------------
            case "split:" -> {
                MTString separator = requireString(args, 0, selector);
                String sep = separator.value();

                if (sep.isEmpty()) {
                    throw new RuntimeException("split: séparateur vide interdit");
                }

                String[] parts = value.split(java.util.regex.Pattern.quote(sep), -1);
                List<MTObject> result = new ArrayList<>();
                for (String part : parts) {
                    result.add(new MTString(part));
                }
                yield new MTArray(result);
            }

            case "lines" -> {
                String[] parts = value.split("\\R", -1);
                List<MTObject> result = new ArrayList<>();
                for (String part : parts) {
                    result.add(new MTString(part));
                }
                yield new MTArray(result);
            }

            // ------------------------------------------------------------
            // Itération / transformation
            // ------------------------------------------------------------
            case "charsDo:" -> {
                MTBlockObject block = requireBlock(args, 0, selector);
                MTObject last = MTNil.INSTANCE;

                for (int i = 0; i < value.length(); i++) {
                    String ch = String.valueOf(value.charAt(i));
                    last = block.call(List.of(new MTString(ch)));
                }

                yield last;
            }

            case "collectChars:" -> {
                MTBlockObject block = requireBlock(args, 0, selector);
                List<MTObject> result = new ArrayList<>();

                for (int i = 0; i < value.length(); i++) {
                    String ch = String.valueOf(value.charAt(i));
                    result.add(block.call(List.of(new MTString(ch))));
                }

                yield new MTArray(result);
            }

            case "selectChars:" -> {
                MTBlockObject block = requireBlock(args, 0, selector);
                StringBuilder result = new StringBuilder();

                for (int i = 0; i < value.length(); i++) {
                    String ch = String.valueOf(value.charAt(i));
                    MTObject keep = block.call(List.of(new MTString(ch)));

                    if (!(keep instanceof MTBoolean b)) {
                        throw new RuntimeException("selectChars: le bloc doit retourner un Boolean");
                    }

                    if (b.value()) {
                        result.append(ch);
                    }
                }

                yield new MTString(result.toString());
            }

            case "rejectChars:" -> {
                MTBlockObject block = requireBlock(args, 0, selector);
                StringBuilder result = new StringBuilder();

                for (int i = 0; i < value.length(); i++) {
                    String ch = String.valueOf(value.charAt(i));
                    MTObject reject = block.call(List.of(new MTString(ch)));

                    if (!(reject instanceof MTBoolean b)) {
                        throw new RuntimeException("rejectChars: le bloc doit retourner un Boolean");
                    }

                    if (!b.value()) {
                        result.append(ch);
                    }
                }

                yield new MTString(result.toString());
            }

            case "takeWhile:" -> {
                MTBlockObject block = requireBlock(args, 0, selector);
                StringBuilder result = new StringBuilder();

                for (int i = 0; i < value.length(); i++) {
                    String ch = String.valueOf(value.charAt(i));
                    MTObject condition = block.call(List.of(new MTString(ch)));

                    if (!(condition instanceof MTBoolean b)) {
                        throw new RuntimeException("takeWhile: le bloc doit retourner un Boolean");
                    }

                    if (!b.value()) {
                        break;
                    }

                    result.append(ch);
                }

                yield new MTString(result.toString());
            }

            case "dropWhile:" -> {
                MTBlockObject block = requireBlock(args, 0, selector);
                int i = 0;

                while (i < value.length()) {
                    String ch = String.valueOf(value.charAt(i));
                    MTObject condition = block.call(List.of(new MTString(ch)));

                    if (!(condition instanceof MTBoolean b)) {
                        throw new RuntimeException("dropWhile: le bloc doit retourner un Boolean");
                    }

                    if (!b.value()) {
                        break;
                    }

                    i++;
                }

                yield new MTString(value.substring(i));
            }

            case "readUntil:" -> {
                MTBlockObject block = requireBlock(args, 0, selector);
                StringBuilder result = new StringBuilder();

                for (int i = 0; i < value.length(); i++) {
                    String ch = String.valueOf(value.charAt(i));
                    MTObject stop = block.call(List.of(new MTString(ch)));

                    if (!(stop instanceof MTBoolean b)) {
                        throw new RuntimeException("readUntil: le bloc doit retourner un Boolean");
                    }

                    if (b.value()) {
                        break;
                    }

                    result.append(ch);
                }

                yield new MTString(result.toString());
            }

            case "asArray" -> {
                List<MTObject> result = new ArrayList<>();

                for (int i = 0; i < value.length(); i++) {
                    String ch = String.valueOf(value.charAt(i));
                    result.add(new MTString(ch));
                }

                yield new MTArray(result);
            }

            // ------------------------------------------------------------
            // Classification caractère
            // ------------------------------------------------------------
            case "isWhitespace" -> new MTBoolean(
                    value.length() == 1 && Character.isWhitespace(value.charAt(0))
            );

            case "isDigit" -> new MTBoolean(
                    value.length() == 1 && Character.isDigit(value.charAt(0))
            );

            case "isLetter" -> new MTBoolean(
                    value.length() == 1 && Character.isLetter(value.charAt(0))
            );

            case "isLetterOrDigit" -> new MTBoolean(
                    value.length() == 1 && Character.isLetterOrDigit(value.charAt(0))
            );

            // ------------------------------------------------------------
            // Conversion
            // ------------------------------------------------------------
            case "asInteger" -> {
                try {
                    yield new MTInteger(Integer.parseInt(value.trim()));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("asInteger: entier invalide: " + value);
                }
            }

            case "asFloat" -> {
                try {
                    yield new MTFloat(Double.parseDouble(value.trim()));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("asFloat: flottant invalide: " + value);
                }
            }

	    case "replaceAll:with:" -> {
    		MTString patternArg = requireString(args, 0, selector);
   		MTString replacementArg = requireString(args, 1, selector);

    		String result = value.replaceAll(
            		patternArg.value(),
            		replacementArg.value()
    		);

    		yield new MTString(result);
	    }

            // ------------------------------------------------------------
            // Affichage
            // ------------------------------------------------------------
            case "printString" -> {
    		String escaped = value.replace("'", "\\'");  
    		yield new MTString("'" + escaped + "'");  
	    }


            default -> throw new RuntimeException("Message inconnu pour String: " + selector);
        };
    }

    private MTString requireString(List<MTObject> args, int index, String selector) {
        if (index >= args.size()) {
            throw new RuntimeException(selector + ": argument manquant à l’indice " + index);
        }
        if (!(args.get(index) instanceof MTString s)) {
            throw new RuntimeException(selector + ": String attendu, reçu: " + args.get(index));
        }
        return s;
    }

    private MTInteger requireInteger(List<MTObject> args, int index, String selector) {
        if (index >= args.size()) {
            throw new RuntimeException(selector + ": argument manquant à l’indice " + index);
        }
        if (!(args.get(index) instanceof MTInteger i)) {
            throw new RuntimeException(selector + ": Integer attendu, reçu: " + args.get(index));
        }
        return i;
    }

    private MTBlockObject requireBlock(List<MTObject> args, int index, String selector) {
        if (index >= args.size()) {
            throw new RuntimeException(selector + ": block manquant à l’indice " + index);
        }
        if (!(args.get(index) instanceof MTBlockObject block)) {
            throw new RuntimeException(selector + ": Block attendu, reçu: " + args.get(index));
        }
        return block;
    }

    private void checkIndex1Based(int index) {
        if (index < 1 || index > value.length()) {
            throw new RuntimeException(
                    "Index String hors limites: " + index + " pour taille " + value.length()
            );
        }
    }

    private static String stripLeft(String s) {
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return s.substring(i);
    }

    private static String stripRight(String s) {
        int i = s.length() - 1;
        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    @Override
    public String toString() {
        return value;
    }
}
