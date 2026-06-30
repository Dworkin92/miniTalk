package mt.runtime;

import mt.util.MTDebug;
import mt.util.MTConfig;
import java.util.*;

public class MTClass implements MTObject {

    private String name;
    private final MTClass superclass;
    private final boolean classFactory;
    private final MTClass instanceSuperclass;

    private final Map<String, MTMethod> methods = new HashMap<>();
    private final Map<String, MTMethod> classMethods = new HashMap<>();
    private final Set<String> instanceVariables = new HashSet<>();
    private final Map<String, MTObject> classVariables = new HashMap<>();

    // ✅ introspection
    private final List<MTClass> subclasses = new ArrayList<>();

    public MTClass(String name, MTClass superclass) {
        this(name, superclass, false, null);
    }

    public MTClass(String name, MTClass superclass, boolean classFactory, MTClass instanceSuperclass) {
        this.name = name;
        this.superclass = superclass;
        this.classFactory = classFactory;
        this.instanceSuperclass = instanceSuperclass;

        // ✅ enregistrement automatique dans la hiérarchie
        if (superclass != null) {
            superclass.subclasses.add(this);
        }
    }

    public void addMethod(String selector, MTBlockObject method) {
        methods.put(selector, new MTMethod(this, method));
    }

    public void addClassMethod(String selector, MTBlockObject method) {
        classMethods.put(selector, new MTMethod(this, method));
    }

    public MTMethod lookup(String selector) {

        if (MTConfig.DEBUG) {
            System.out.println("[LOOKUP] " + name + " -> " + selector);
        }

        MTMethod method = methods.get(selector);
        if (method != null) return method;
        if (superclass != null) return superclass.lookup(selector);
        return null;
    }

    public boolean hasInstVar(String name) {
        if (instanceVariables.contains(name)) return true;
        if (superclass != null) return superclass.hasInstVar(name);
        return false;
    }

    public MTObject getClassVar(String name) {
        if (classVariables.containsKey(name)) return classVariables.get(name);
        if (superclass != null) return superclass.getClassVar(name);
        return MTNil.INSTANCE;
    }

    public MTObject setClassVar(String name, MTObject value) {
        if (classVariables.containsKey(name)) {
            classVariables.put(name, value);
            return value;
        }
        if (superclass != null && superclass.classVariables.containsKey(name)) {
            return superclass.setClassVar(name, value);
        }
        throw new RuntimeException("Variable de classe inconnue: " + name);
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        // --- accès variable de classe ---
        if (args.isEmpty() && classVariables.containsKey(selector)) {
            return getClassVar(selector);
        }

        // --- méthodes de classe ---
        MTMethod classMethod = classMethods.get(selector);
        if (classMethod != null) {
            return classMethod.body().callWithReceiver(this, args, classMethod.owner());
        }

        switch (selector) {

            // --------------------------------------------------
            // création
            // --------------------------------------------------
            case "new": {

                if (classFactory) {
                    return new MTClass("Anonymous", instanceSuperclass);
                }

                if (name.equals("Array")) return new MTArray(new ArrayList<>());
                if (name.equals("List")) return new MTListObject();
                if (name.equals("Set")) return new MTSetObject();
                if (name.equals("Dictionary")) return new MTDictionaryObject();
                if (name.equals("Database")) return new MTDatabase();

                return new MTInstance(this);
            }

            case "new:": {
                String className = ((MTString) args.get(0)).value();
                if (classFactory) {
                    return new MTClass(className, instanceSuperclass);
                }
                throw new RuntimeException("new: non supporté");
            }

	    // Class management
	    case "addMethod:with:": {
    		String methodName = ((MTString) args.get(0)).value();
    		MTBlockObject block = (MTBlockObject) args.get(1);
    		addMethod(methodName, block);
    		return this;
	    }

	    case "addClassMethod:with:": {
    		String methodName = ((MTString) args.get(0)).value();
    		MTBlockObject block = (MTBlockObject) args.get(1);
    		addClassMethod(methodName, block);
    		return this;
	    }

	    case "addInstVar:": {
    		String varName = ((MTString) args.get(0)).value();
    		instanceVariables.add(varName);
    		return this;
	    }

	    case "addClassVar:": {
    		String varName = ((MTString) args.get(0)).value();
    		classVariables.put(varName, MTNil.INSTANCE);
    		return this;
	    }

            // --------------------------------------------------
            // hiérarchie
            // --------------------------------------------------

            case "subclass":
            case "subclass:": {
                String childName = args.isEmpty()
                        ? "Anonymous"
                        : ((MTString) args.get(0)).value();
                return new MTClass(childName, this);
            }

            case "superclass": {
                return superclass != null ? superclass : MTNil.INSTANCE;
            }

            case "subclasses": {
                return new MTArray(new ArrayList<>(subclasses));
            }


	    case "allSubclasses": {
    		List<MTObject> result = new ArrayList<>();
    		collectAllSubclasses(this, result);
   		return new MTArray(result);
	    }

            // --------------------------------------------------
            // introspection
            // --------------------------------------------------

            case "methods": {
                List<MTObject> list = new ArrayList<>();
                for (String m : methods.keySet()) {
                    list.add(new MTString(m));
                }
                return new MTArray(list);
            }


	    case "allMethods": {
    		List<MTObject> list = new ArrayList<>();
    		collectMethods(this, list);
    		return new MTArray(list);
	    }

            case "name": {
                return new MTString(name);
            }

            // --------------------------------------------------
            // affichage
            // --------------------------------------------------

            case "printString": {
                return new MTString(name);
            }
        }

        throw new RuntimeException("Message inconnu pour Class: " + selector);
    }

    @Override
    public String toString() {
        return "Class(" + name + ")";
    }


    public MTClass superclass() {
    	return superclass;
    }


    private void collectAllSubclasses(MTClass cls, List<MTObject> result) {
    	for (MTClass sub : cls.subclasses) {
        	result.add(sub);
        	collectAllSubclasses(sub, result);
    	}
    }


    private void collectMethods(MTClass cls, List<MTObject> list) {
    	for (String m : cls.methods.keySet()) {
        	list.add(new MTString(m));
    	}
    	if (cls.superclass() != null) {
        	collectMethods(cls.superclass(), list);
    	}
    }


}
