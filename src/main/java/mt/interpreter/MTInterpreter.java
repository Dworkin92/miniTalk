package mt.interpreter;

import mt.ast.*;
import mt.runtime.*;
import mt.runtime.bootstrap.*;

public class MTInterpreter {

    public MTObject evaluate(
            MTNode node,
            MTScope scope) {

        if (node instanceof MTIntegerLiteralNode n) {
            MTClass integerClass =
                MTKernelBootstrap.createIntegerClass();

            MTInteger value = new MTInteger(n.getValue());

            value.setClazz(integerClass);

            return value;
            //return new MTInteger(
            //        n.getValue());
        }

        if (node instanceof MTSymbolLiteralNode n) {

            return n.getSymbol();
        }

        if (node instanceof MTVariableNode n) {

            return scope.lookup(n.getName());
        }

        if (node instanceof MTAssignmentNode n) {

            MTObject value = evaluate(
                    n.getValue(),
                    scope);

            scope.assign(n.getVariable(), value);

            return value;
        }

        if (node instanceof MTMessageSendNode n) {

            MTObject receiver =
                evaluate(
                    n.getReceiver(),
                    scope);

            MTArray arguments =
                new MTArray();

            for (MTNode argumentNode :
                n.getArguments()
                    .getElements()) {

                arguments.add(
                    evaluate(
                        argumentNode,
                        scope));
            }

            /*
System.out.println(
        "receiver = " + receiver);

System.out.println(
        "receiver class = "
        + receiver.getClazz());

System.out.println(
        "selector = "
        + n.getSelector());
*/
            return receiver.send(
                n.getSelector(),
                arguments);
        }

        if (node instanceof MTObjectLiteralNode n) {

            return n.getObject();
        }

        if (node instanceof MTSequenceNode n) {

            MTObject result = MTNil.instance();

            for (MTNode statement : n.getStatements()) {

                result = evaluate(statement, scope);
            }

            return result;
        }

        if (node instanceof MTBlockNode n) {

            return new MTBlock(
                scope,          // scope capture
                n.getParameters(),
                n
                //n.getBody()
                );
        }

        if (node instanceof MTNonLocalReturnNode n) {

            MTObject value =
                evaluate(
                    n.getExpression(),
                    scope);

            throw new MTNonLocalReturnException(value);
        }

        throw new RuntimeException(
                "Unsupported node: "
                + node.getClass().getName());
    }
}
