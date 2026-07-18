package mt.runtime;

import mt.ast.MTBlockNode;
import mt.interpreter.MTInterpreter;
import mt.debug.MTDebug;
import mt.exceptions.MTRuntimeException;

public class MTBlock
        extends MTObject {

    private final MTScope capturedScope;

    private final MTArray parameters;

    private final MTBlockNode ast;

    private final MTBlock homeBlock;

    public MTBlock(MTScope capturedScope) {
        this(
                capturedScope,
                new MTArray(),
                null);
    }

    public MTBlock(
            MTScope capturedScope,
            MTArray parameters,
            MTBlockNode ast) {

        this.capturedScope = capturedScope;

        this.parameters = parameters;

        this.ast = ast;

        this.homeBlock =
            capturedScope != null
            && capturedScope.getHomeBlock() != null
            ? capturedScope.getHomeBlock(): this;
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

    public MTBlock getHomeBlock() {

        return homeBlock;
    }

    public MTScope createActivationScope(
        MTArray arguments) {

        if (arguments.size()
                != parameters.size()) {

            throw new MTRuntimeException(
                    "Expected "
                    + parameters.size()
                    + " arguments but got "
                    + arguments.size());
        }

        MTScope activationScope =
                new MTScope(
                        capturedScope.runtime(),
                        capturedScope);

        activationScope.setHomeBlock(homeBlock);

        /* traitement des parametres */
        for (int i = 0; i < parameters.size(); i++) {
            MTSymbol parameter = (MTSymbol)parameters.at(i);

            MTObject argument = arguments.at(i);

            activationScope.define(parameter, argument);
        }

        /* Traitement des variables temporaires */
        MTArray temporaries = ast.getTemporaries();

        for (int i = 0; i < temporaries.size(); i++) {
            MTSymbol temporary = (MTSymbol)temporaries.at(i);

            activationScope.define(temporary, MTNil.instance());
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

        MTInterpreter interpreter = new MTInterpreter(activationScope.getRuntime());

        /*
         * L'AST n'est pas encore execute.
         *
         * On garde simplement le scope d'activation vivant pour les
         * futures etapes.
         */

        if (activationScope == null) {

            throw new MTRuntimeException(
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
            MTDebug.log(
                "this = "
                + System.identityHashCode(this));

            MTDebug.log(
                "homeBlock   = " +
                + System.identityHashCode(homeBlock));

            MTDebug.log(
                "targetBlock = "
                + System.identityHashCode(ex.getTargetBlock()));

            if (ex.getTargetBlock() == this) {

                MTDebug.log("CAPTURED");

                return ex.getValue();
            }

            MTDebug.log("RETHROW");

            throw ex;
        }
    }

    @Override
    public String toString() {

        return "[MTBlock]";
    }
}
