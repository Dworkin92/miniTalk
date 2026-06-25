package mt.interpreter;


import java.util.List;
import java.util.ArrayList;
import mt.runtime.MTFileClass;

import mt.ast.*;
import mt.runtime.*;


public class MTInterpreter {

    // ✅ accessible depuis le runtime (MTArray, etc.)
    public static MTEnvironment GLOBAL;

    private final MTEnvironment globalEnv = new MTEnvironment();

    public MTInterpreter() {
        GLOBAL = globalEnv;

        // classes de base
        MTClass objectClass = new MTClass("Object", null);
        MTClass classClass  = new MTClass("Class", objectClass, true, objectClass);
        MTClass arrayClass  = new MTClass("Array", objectClass);
	MTClass stringClass = new MTClass("String", objectClass);
	MTClass integerClass = new MTClass("Integer", objectClass);
	MTClass floatClass   = new MTClass("Float", objectClass);
	MTClass booleanClass = new MTClass("Boolean", objectClass);


        globalEnv.define("Object", objectClass);
        globalEnv.define("Class", classClass);
        globalEnv.define("Array", arrayClass);
	globalEnv.define("File", new MTFileClass());
        globalEnv.define("Process", new MTProcessClass());
        globalEnv.define("String", stringClass);
	globalEnv.define("Integer", integerClass);
	globalEnv.define("Float", floatClass);
	globalEnv.define("Boolean", booleanClass);

    }

    // ✅ nécessaire pour Main
    public MTEnvironment getGlobalEnv() {
        return globalEnv;
    }

    public MTObject eval(MTExpr expr) {
        return eval(expr, globalEnv);
    }

    public MTObject eval(MTExpr expr, MTEnvironment env) {

        // -----------------------------
        // literals
        // -----------------------------
        if (expr instanceof MTLiteral lit) {
            Object v = lit.value();

            if (v == null) return MTNil.INSTANCE;
            if (v instanceof Integer i) return new MTInteger(i);
            if (v instanceof Double d) return new MTFloat(d);
            if (v instanceof String s) return new MTString(s);
            if (v instanceof Boolean b) return new MTBoolean(b);

            return (MTObject) v;
        }

        // -----------------------------
        // variable
        // -----------------------------
        if (expr instanceof MTVariableRef var) {
            return env.lookup(var.name());
        }

        // -----------------------------
        // assignation
        // -----------------------------
        if (expr instanceof MTAssignment asg) {
            MTObject val = eval(asg.value(), env);
            env.set(asg.name(), val);
            return val;
        }

        // -----------------------------
        // sequence
        // -----------------------------
        if (expr instanceof MTSequence seq) {
            MTObject result = MTNil.INSTANCE;
            for (MTExpr e : seq.statements()) {
                result = eval(e, env);
            }
            return result;
        }

        // -----------------------------
        // message send
        // -----------------------------
        if (expr instanceof MTMessageSend msg) {

            MTObject receiver = eval(msg.receiver(), env);

            List<MTObject> args = new ArrayList<>();
            for (MTExpr a : msg.args()) {
                args.add(eval(a, env));
            }

            return receiver.send(msg.selector(), args);
        }

        // -----------------------------
        // block
        // -----------------------------
        if (expr instanceof MTBlock block) {
            return new MTBlockObject(block.params(), block.body(), env, this);
        }

        // -----------------------------
        // array literal
        // -----------------------------
        if (expr instanceof MTArrayLiteral array) {
            List<MTObject> values = new ArrayList<>();
            for (MTExpr e : array.elements()) {
                values.add(eval(e, env));
            }
            return new MTArray(values);
        }

        // -----------------------------
        // return
        // -----------------------------
        if (expr instanceof MTReturnExpr ret) {
            MTObject value = eval(ret.value(), env);
            MTReturnTarget target = (MTReturnTarget) env.lookup(MTReturn.TARGET_KEY);
            throw new MTReturn(target.token(), value);
        }

        throw new RuntimeException("Expression inconnue: " + expr);
    }
}
