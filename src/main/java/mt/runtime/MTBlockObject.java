package mt.runtime;

import mt.ast.MTExpr;
import mt.interpreter.MTEnvironment;
import mt.interpreter.MTInterpreter;
import mt.util.MTDebug;
import mt.util.MTConfig;

import java.util.List;

public class MTBlockObject implements MTObject {

    private final List<String> params;
    private final MTExpr body;
    private final MTEnvironment capturedEnv;
    private final MTInterpreter interpreter;

    public MTBlockObject(
            List<String> params,
            MTExpr body,
            MTEnvironment capturedEnv,
            MTInterpreter interpreter
    ) {
        this.params = params;
        this.body = body;
        this.capturedEnv = capturedEnv;
        this.interpreter = interpreter;
    }

    @Override
    public MTObject send(String selector, List<MTObject> args) {
        return switch (selector) {
            case "value" -> call(List.of());
            case "value:" -> call(args);
            case "value:value:" -> call(args);
            case "value:value:value:" -> call(args);

            case "whileTrue:" -> {
                MTBlockObject bodyBlock = requireBlock(args, 0);
                MTObject last = MTNil.INSTANCE;

                while (true) {
                    MTObject cond = callWithReceiver(null, List.of(), null);

                    if (!(cond instanceof MTBoolean b)) {
                        throw new RuntimeException("whileTrue: exige que le block receveur retourne un Boolean");
                    }

                    if (!b.value()) {
                        break;
                    }

                    //last = bodyBlock.call(List.of());
		    last = bodyBlock.callWithReceiver(null, List.of(), null);
                }

                yield last;
            }


	    case "repeatWhile:" -> {
    		MTBlockObject condition = requireBlock(args, 0);
    		MTObject last = MTNil.INSTANCE;

    		while (true) {
        	    // 1. exécuter le corps (this)
       		    last = callWithReceiver(null, List.of(), null);

        	    // 2. évaluer la condition
        	    MTObject cond = condition.call(List.of());

        	    if (!(cond instanceof MTBoolean b)) {
            		throw new RuntimeException("repeatWhile: exige un Boolean");
        	    }

        	    // 3. sortir si faux
        	    if (!b.value()) {
            		break;
        	    }
    		}

    		yield last;
	    }

        case "whileFalse:" -> {
            MTBlockObject bodyBlock = requireBlock(args, 0);
            MTObject last = MTNil.INSTANCE;

            while (true) {
                MTObject cond = callWithReceiver(null, List.of(), null);

                if (!(cond instanceof MTBoolean b)) {
                    throw new RuntimeException("whileFalse: exige que le block receveur retourne un Boolean");
                }

                if (b.value()) {
                    break;
                }

                last = bodyBlock.callWithReceiver(null, List.of(), null);
            }

            yield last;
        }


	    case "repeatUntil:" -> {
    		MTBlockObject condition = requireBlock(args, 0);
    		MTObject last = MTNil.INSTANCE;

    		while (true) {
        		last = callWithReceiver(null, List.of(), null);

        		MTObject cond = condition.call(List.of());

        		if (!(cond instanceof MTBoolean b)) {
            			throw new RuntimeException("repeatUntil: exige un Boolean");
        		}

        		if (b.value()) {
            			break;
        		}
    		}

    		yield last;
	    }


	    case "repeat" -> {
    		while (true) {
        		callWithReceiver(null, List.of(), null);
    		}
    		//throw new RuntimeException("Boucle infinie");
	    }

            case "printString" -> {
    		if (params.isEmpty()) {
        		yield new MTString("[...]");
    		}

    		StringBuilder sb = new StringBuilder("[");
    		for (String p : params) {
        		sb.append(":").append(p).append(" ");
    		}
    		sb.append("...]");

    		yield new MTString(sb.toString());
	    }

            default -> throw new RuntimeException("Message inconnu pour block: " + selector);
        };
    }

    public MTObject call(List<MTObject> args) {
        return callWithReceiver(null, args, null);
    }

    public MTObject callWithReceiver(MTObject self, List<MTObject> args, MTClass ownerClass) {
        if (MTConfig.DEBUG) {
            System.out.println("[BLOCK] call with " + args);
        }

        MTEnvironment local = new MTEnvironment(capturedEnv);

        MTReturnTarget returnTargetObj;
        boolean createdHere = false;

        try {
            returnTargetObj = (MTReturnTarget) capturedEnv.lookup(MTReturn.TARGET_KEY);
        } catch (RuntimeException e) {
            returnTargetObj = new MTReturnTarget(new Object());
            createdHere = true;
        }

        local.define(MTReturn.TARGET_KEY, returnTargetObj);

        if (self != null) {
            if (self instanceof MTInstance inst) {
                local.define("self", new MTSelf(inst));

                if (ownerClass != null && ownerClass.superclass() != null) {
                    local.define("super", new MTSuper(inst, ownerClass.superclass()));
                }
            } else {
                local.define("self", self);
            }
        }

        for (int i = 0; i < params.size(); i++) {
            local.define(params.get(i), args.get(i));
        }

        try {
            return interpreter.eval(body, local);
        } catch (MTReturn r) {
            if (createdHere && r.target() == returnTargetObj.token()) {
                return r.value();
            }
            throw r;
        }
    }

    private MTBlockObject requireBlock(List<MTObject> args, int index) {
        if (index >= args.size()) {
            throw new RuntimeException("Block manquant à l’indice " + index);
        }
        if (!(args.get(index) instanceof MTBlockObject block)) {
            throw new RuntimeException("Block attendu à l’indice " + index + ", reçu: " + args.get(index));
        }
        return block;
    }
}
