package mt.runtime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MTClass implements MTObject {

    private String name;
    private final MTClass superclass;
    private final boolean classFactory;
    private final MTClass instanceSuperclass;

    private final Map<String, MTMethod> methods = new HashMap<>();
    private final Map<String, MTMethod> classMethods = new HashMap<>();

    private final Set<String> instanceVariables = new HashSet<>();
    private final Map<String, MTObject> classVariables = new HashMap<>();

    public MTClass(String name, MTClass superclass) {
        this(name, superclass, false, null);
    }

    public MTClass(String name, MTClass superclass, boolean classFactory, MTClass instanceSuperclass) {
        this.name = name;
        this.superclass = superclass;
        this.classFactory = classFactory;
        this.instanceSuperclass = instanceSuperclass;
    }

    public void addMethod(String selector, MTBlockObject method) {
        methods.put(selector, new MTMethod(this, method));
    }

    public void addClassMethod(String selector, MTBlockObject method) {
        classMethods.put(selector, new MTMethod(this, method));
    }

    public MTMethod lookup(String selector) {
        MTMethod method = methods.get(selector);
        if (method != null) return method;
        if (superclass != null) return superclass.lookup(selector);
        return null;
    }

    public boolean hasMethod(String selector) {
        if (methods.containsKey(selector)) return true;
        if (superclass != null) return superclass.hasMethod(selector);
        return false;
    }

    public void addInstanceVariable(String name) {
        instanceVariables.add(name);
    }

    public boolean hasInstVar(String name) {
        if (instanceVariables.contains(name)) return true;
        if (superclass != null) return superclass.hasInstVar(name);
        return false;
        }

    public void addClassVariable(String name) {
        classVariables.put(name, MTNil.INSTANCE);
    }

    public boolean hasClassVar(String name) {
        if (classVariables.containsKey(name)) return true;
        if (superclass != null) return superclass.hasClassVar(name);
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
        if (superclass != null && superclass.hasClassVar(name)) {
            return superclass.setClassVar(name, value);
        }
        throw new RuntimeException("Variable de classe inconnue: " + name);
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {

        // automatic class var getter
        if (args.isEmpty() && hasClassVar(selector)) {
            return getClassVar(selector);
        }

        // automatic class var setter
        if (selector.endsWith(":") && args.size() == 1) {
            String fieldName = selector.substring(0, selector.length() - 1);
            if (hasClassVar(fieldName)) {
                return setClassVar(fieldName, args.get(0));
            }
        }

        // class-side methods
        MTMethod classMethod = classMethods.get(selector);
        if (classMethod != null) {
            return classMethod.body().callWithReceiver(this, args, classMethod.owner());
        }

        switch (selector) {
            case "new" -> {
                if (classFactory) {
                    return new MTClass("Anonymous", instanceSuperclass);
                }
                return new MTInstance(this);
            }

            case "new:" -> {
                String className = ((MTString) args.get(0)).value();
                if (classFactory) {
                    return new MTClass(className, instanceSuperclass);
                }
                throw new RuntimeException("new: non supporté sur une classe normale");
            }

            case "addMethod:with:" -> {
                String methodName = ((MTString) args.get(0)).value();
                MTBlockObject block = (MTBlockObject) args.get(1);
                addMethod(methodName, block);
                return this;
            }

            case "addClassMethod:with:" -> {
                String methodName = ((MTString) args.get(0)).value();
                MTBlockObject block = (MTBlockObject) args.get(1);
                addClassMethod(methodName, block);
                return this;
            }

            case "addInstVar:" -> {
                String varName = ((MTString) args.get(0)).value();
                addInstanceVariable(varName);
                return this;
            }

            case "addClassVar:" -> {
                String varName = ((MTString) args.get(0)).value();
                addClassVariable(varName);
                return this;
            }

            case "subclass" -> {
                return new MTClass("Anonymous", this);
            }

            case "subclassNamed:" -> {
                String childName = ((MTString) args.get(0)).value();
                return new MTClass(childName, this);
            }

            case "name" -> {
                return new MTString(name);
            }

            case "named:" -> {
                String newName = ((MTString) args.get(0)).value();
                this.name = newName;
                return this;
            }

            case "superclass" -> {
                return superclass != null ? superclass : MTNil.INSTANCE;
            }


	    //----------------------------------------------------------
	    // introspection
	    //----------------------------------------------------------
	    case "methods" -> {
    		List<MTObject> names = new ArrayList<>();
    		for (String name : methods.keySet()) {
        		names.add(new MTString(name));
    		}
    		yield new MTArray(names);
	    }

            case "printString" -> {
                //return new MTString("Class(" + name + ")");
		new MTString(name);
            }
        }

        throw new RuntimeException("Message inconnu pour Class: " + selector);
    }

    public MTClass superclass() {
        return superclass;
    }

    @Override
    public String toString() {
        return "Class(" + name + ")";
    }
}
