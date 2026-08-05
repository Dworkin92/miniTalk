package mt.interpreter;

import mt.ast.*;
import mt.runtime.*;
import mt.runtime.bootstrap.*;
import mt.debug.MTDebug;
import mt.exceptions.MTRuntimeException;

public class MTInterpreter {

    private final MTRuntime runtime;

    public MTInterpreter(MTRuntime runtime) {
        this.runtime = runtime;
    }

    public MTObject evaluate(
            MTNode node,
            MTScope scope) {

        if (node instanceof MTIntegerLiteralNode n) {
            MTClass integerClass =
                runtime.classNamed("Integer");
                //MTKernelBootstrap.createIntegerClass();

            MTInteger value = new MTInteger(n.getValue());

            value.setClazz(integerClass);

            return value;
            //return new MTInteger(
            //        n.getValue());
        }

        if (node instanceof MTSymbolLiteralNode n) {
            return n.getSymbol();
        }

        if (node instanceof MTNilLiteralNode) {
            return MTNil.instance();
        }

        if (node instanceof MTTemporaryDeclarationNode temp) {

            MTArray temporaries = temp.getTemporaries();

            for(MTObject each : temporaries) {
                MTSymbol symbol = (MTSymbol)each;

                scope.define(symbol, MTNil.instance());
            }
/*
            for (int i = 0; i < temporaries.size(); i++) {

                MTSymbol symbol = (MTSymbol) temporaries.at(i);

                scope.define(symbol, MTNil.instance());
            }
            */

            return MTNil.instance();
        }

        if (node instanceof MTBooleanLiteralNode n) {
            return MTBoolean.valueOf(n.getValue());
        }

        if (node instanceof MTVariableNode n) {
            return scope.lookup(n.getName());
        }

        if (node instanceof MTStringLiteralNode n) {

            MTClass stringClass = MTKernelBootstrap.createStringClass();

            MTString result = new MTString(n.getValue());

            result.setClazz(stringClass);

            return result;
        }

        if (node instanceof MTAssignmentNode n) {

            MTObject value = evaluate(
                    n.getValue(),
                    scope);

            scope.assign(n.getVariable(), value);

            return value;
        }

        if (node instanceof MTMessageSendNode n) {

            MTObject receiver = evaluate(n.getReceiver(), scope);

            MTArray arguments = new MTArray();

            for (MTNode argumentNode : n.getArguments().getElements()) {

                arguments.add(evaluate(argumentNode, scope));
            }


            MTDebug.log("receiver = " + receiver);

            MTDebug.log("receiver class = " + receiver.getClazz());

            MTDebug.log("selector = " + n.getSelector());

            return receiver.send(n.getSelector(), arguments, scope);
        }

        if (node instanceof MTObjectLiteralNode n) {

            return n.getObject();
        }

        if (node instanceof MTSequenceNode sequence) {

            for (MTSymbol temporary : sequence.getTemporaries()) {
                //System.out.println("define temporary " + temporary);
                scope.define(temporary, MTNil.instance());
            }

            MTObject result = MTNil.instance();

            for (MTNode statement : sequence.getStatements()) {
                result = evaluate(statement, scope);
            }

            return result;
        }

        if (node instanceof MTBlockNode n) {
            MTClass blockClass = MTKernelBootstrap.createBlockClass();

            MTBlock block = new MTBlock(
                scope, n.getParameters(), n);

            block.setClazz(blockClass);

            MTDebug.log("created block class = " + block.getClazz());

            return block;
        }

        if (node instanceof MTNonLocalReturnNode n) {

            MTObject value = evaluate(n.getExpression(), scope);

            throw new MTNonLocalReturnException(value, scope.getHomeBlock());
        }

        throw new MTRuntimeException(
                "Unsupported node: " + node.getClass().getName());
    }
}
