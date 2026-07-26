package mt.tests;

import mt.runtime.*;
import mt.runtime.primitives.*;
import mt.runtime.bootstrap.*;
import mt.ast.*;
import mt.interpreter.*;
import mt.lexer.*;
import mt.parser.*;
import mt.debug.*;
import mt.tests.*;

import static mt.tests.TestUtils.assertResult;

public final class ModuleRegressionTests {

    private static final MTRuntime runtime = MTRuntimeBootstrap.bootstrap();

    private ModuleRegressionTests() {
    }

    public static void runAll() {
        testMetaDirectiveNode();
       /* testModuleDirective();
        testImportDirectives();
        testModuleExecution();*/
    }

    /*
    private static void testModuleDirective() {

    System.out.println(
            "=== Module Directive test ===");

    String source =
            """
            @[module Core]

            42
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    parser.parse();

    assertResult(
            "module name",
            parser.getModuleName());
}

private static void testImportDirectives() {

    System.out.println(
            "=== Import Directives test ===");

    String source =
            """
            @[module Application]
            @[import Collections]
            @[import IO]

            42
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    parser.parse();

    assertResult(
            "module name",
            parser.getModuleName());

    assertResult(
            "imports",
            parser.getImports());
}

private static void testModuleExecution() {

    System.out.println(
            "=== Module Execution test ===");

    String source =
            """
            @[module Core]

            42
            """;

    MTLexer lexer =
            new MTLexer(source);

    MTParser parser =
            new MTParser(
                    lexer.tokenize());

    MTInterpreter interpreter =
            new MTInterpreter(runtime);

    MTObject result =
            interpreter.evaluate(
                    parser.parse(),
                    new MTScope(runtime, null));

    assertResult(
            "42",
            result);
}
*/
private static void testMetaDirectiveNode() {

    System.out.println(
        "=== Meta Directive AST test ===");

    String source =
        """
        @[module Core]

        42
        """;

    MTLexer lexer =
        new MTLexer(source);

    MTParser parser =
        new MTParser(
            lexer.tokenize());

    MTNode ast =
        parser.parse();

    if (!(ast instanceof MTSequenceNode seq)) {
        throw new AssertionError(
            "Expected MTSequenceNode");
    }

    if (!(seq.getStatements().get(0)
            instanceof MTMetaDirectiveNode meta)) {

        throw new AssertionError(
            "Expected MTMetaDirectiveNode");
    }

    assertResult(
        "module Core",
        meta.getText());
}

}
