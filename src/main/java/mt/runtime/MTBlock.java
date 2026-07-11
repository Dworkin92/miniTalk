package mt.runtime;

import mt.ast.MTBlockNode;
import mt.interpreter.MTInterpreter;

public class MTBlock
        extends MTObject {

    private final MTScope capturedScope;

    private final MTArray parameters;

    private final MTBlockNode ast;

    public MTBlock(
            MTScope capturedScope) {

        this(
                capturedScope,
                new MTArray(),
                null);
    }

    public MTBlock(
            MTScope capturedScope,
            MTArray parameters,
            MTBlockNode ast) {

        this.capturedScope =
                capturedScope;

        this.parameters =
                parameters;

        this.ast =
                ast;
    }

    public MTScope getCapturedScope() {

        return capturedScope;
    }

    public MTArray getParameters() {

        return parameters;
    }

    public int parameterCount() {

        return parameters.size();
    }

    public MTBlockNode getAst() {

        return ast;
    }

    public MTScope createActivationScope(
            MTArray arguments) {

        if (arguments.size()
                != parameters.size()) {

            throw new RuntimeException(
                    "Expected "
                    + parameters.size()
                    + " arguments but got "
                    + arguments.size());
        }

        MTScope activationScope =
                new MTScope(
                        capturedScope);

        for (int i = 0;
             i < parameters.size();
             i++) {

            MTSymbol parameter =
                    (MTSymbol)
                            parameters.at(i);

            MTObject argument =
                    arguments.at(i);

            activationScope.define(
                    parameter,
                    argument);
        }

        return activationScope;
    }

    public MTObject value() {
        return value(
            new MTArray());
    }

    public MTObject value(
        MTArray arguments) {

        MTScope activationScope =
            createActivationScope(
                    arguments);

        MTInterpreter interpreter = new MTInterpreter();

        /*
         * L'AST n'est pas encore execute.
         *
         * On garde simplement le scope d'activation vivant pour les
         * futures etapes.
         */

        if (activationScope == null) {

            throw new RuntimeException(
                "Activation scope creation failed");
        }

        // return MTNil.instance();
        /*
        return interpreter.evaluate(
            ast.getBody(),
            activationScope);
        */
        try {

            return interpreter.evaluate(
                ast.getBody(),
                activationScope);
        }
        catch (MTNonLocalReturnException ex) {
            return ex.getValue();
        }
    }

    @Override
    public String toString() {

        return "[MTBlock]";
    }
}
