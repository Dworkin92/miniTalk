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

public final class BlockRegressionTests {

    private BlockRegressionTests() {
    }

    public static void runAll() {

        testBlock();

        testBlockParameters();

        testBlockValueArray();

        testBlockTemporaries();

        testUndeclaredTemporary();

        testClosureMutation();

        testClosureWithTemporary();

        testNestedClosure();

        testNestedClosureNonLocalReturn();

    }

    private static void testBlock() {
        System.out.println("=== Block V4 test ===");

        MTScope global = new MTScope(null);
        MTClass integerClass = MTKernelBootstrap.createIntegerClass();

        MTInteger zValue = new MTInteger(100);
        zValue.setClazz(integerClass);
        global.define(MTSymbol.intern("z"),zValue);

        MTArray params = new MTArray();
        params.add(MTSymbol.intern("x"));
        params.add(MTSymbol.intern("y"));

        MTVariableNode x = new MTVariableNode(MTSymbol.intern("x"));
        MTVariableNode y = new MTVariableNode(MTSymbol.intern("y"));
        MTVariableNode z = new MTVariableNode(MTSymbol.intern("z"));

        MTArrayNode argsXY = new MTArrayNode();
        argsXY.add(y);

        MTMessageSendNode plusXY = new MTMessageSendNode(
                x, MTSymbol.intern("+"), argsXY);

        MTArrayNode argsXYZ = new MTArrayNode();
        argsXYZ.add(z);

        MTMessageSendNode plusXYZ = new MTMessageSendNode(
                plusXY, MTSymbol.intern("+"), argsXYZ);
        MTSequenceNode body =new MTSequenceNode();
        body.add(plusXYZ);

        MTBlockNode blockNode = new MTBlockNode(params, body);

        MTBlock block =new MTBlock(global, params, blockNode);

        MTArray arguments = new MTArray();

        MTInteger ten = new MTInteger(10);
        ten.setClazz(integerClass);
        MTInteger twenty = new MTInteger(20);
        twenty.setClazz(integerClass);

        arguments.add(ten);
        arguments.add(twenty);

        MTScope activation = block.createActivationScope(arguments);
        assertResult("x", activation.lookup(MTSymbol.intern("x")));
        assertResult("y", activation.lookup(MTSymbol.intern("y")));
        assertResult("z",activation.lookup(MTSymbol.intern("z")));

        MTInterpreter interpreter = new MTInterpreter();

        /*
        System.out.println(
            interpreter.evaluate(
                blockNode.getBody(),
                activation));
        */

        assertResult("z <- 100. [ :x :y | x + y + z ] value: 10 value: 20",
            block.value(
                arguments));
    }

    private static void testBlockParameters() {

    System.out.println(
            "=== Parser Block Parameters test ===");

    String source =
            "[:x | x]";

    MTLexer lexer =
            new MTLexer(source);

        MTDebug.log(lexer.tokenize().toString());

        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter();

        MTBlock block = (MTBlock)interpreter.evaluate(ast, new MTScope(null));
        System.out.println("nb de parametres ==> " + block.parameterCount());

        MTClass integerClass = MTKernelBootstrap.createIntegerClass();
        MTInteger value = new MTInteger(123);
        value.setClazz(integerClass);
        MTArray arguments = new MTArray();
        arguments.add(value);
        assertResult( source + " value: 123", block.value(arguments));

        source = "[:x :y | x + y]";
        lexer = new MTLexer(source);
        MTDebug.log(lexer.tokenize().toString());
        parser = new MTParser(lexer.tokenize());
        ast = parser.parse();
        block = (MTBlock)interpreter.evaluate(
            ast, new MTScope(null));
        System.out.println("nb de parametres ==> " + block.parameterCount());

        MTInteger ten = new MTInteger(10);
        ten.setClazz(integerClass);
        MTInteger twentythree = new MTInteger(23);
        twentythree.setClazz(integerClass);
        arguments = new MTArray();
        arguments.add(ten);
        arguments.add(twentythree);
        assertResult(source + " value: 10 value 23", block.value(arguments));
    }

    private static void testBlockValueArray() {
        System.out.println("=== Block valueArray: test ===");

        String source = "[:x :y :z | (x + y) * z]";

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter();

        MTBlock block = (MTBlock)interpreter.evaluate(
                    ast, new MTScope(null));
        MTClass integerClass = MTKernelBootstrap.createIntegerClass();

        MTInteger one = new MTInteger(1);
        one.setClazz(integerClass);
        MTInteger two = new MTInteger(2);
        two.setClazz(integerClass);
        MTInteger three = new MTInteger(3);
        three.setClazz(integerClass);

        MTArray actualArguments = new MTArray();
        actualArguments.add(one);
        actualArguments.add(two);
        actualArguments.add(three);

        assertResult( source + " valueArray: #(1 2 3)", block.value(actualArguments));
    }

    private static void testBlockTemporaries() {
        System.out.println("=== Block Temporaries test ===");

        String source =
            """
            [:x :y |

                | z |

                z := x + y.

                z

            ]
            """;

        MTLexer lexer = new MTLexer(source);
        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter();

        MTBlock block =(MTBlock)interpreter.evaluate(
            ast, new MTScope(null));

        MTClass integerClass = MTKernelBootstrap.createIntegerClass();

        MTInteger ten = new MTInteger(10);
        ten.setClazz(integerClass);

        MTInteger twenty = new MTInteger(20);
        twenty.setClazz(integerClass);

        MTArray arguments = new MTArray();

        arguments.add(ten);
        arguments.add(twenty);

        assertResult(source + " value: 10 value: 20", block.value(arguments));
    }

    private static void testUndeclaredTemporary() {
        System.out.println("=== Undeclared Temporary test ===");

        String source =
            """
            [:x :y |

                z := x + y.

                z

            ]
            """;

        try {

            MTLexer lexer = new MTLexer(source);

            MTParser parser = new MTParser(lexer.tokenize());

            MTNode ast = parser.parse();
            MTInterpreter interpreter = new MTInterpreter();

            MTBlock block = (MTBlock)interpreter.evaluate(
                ast, new MTScope(null));

            MTClass integerClass = MTKernelBootstrap.createIntegerClass();

            MTInteger ten = new MTInteger(10);
            ten.setClazz(integerClass);
            MTInteger twenty = new MTInteger(20);
            twenty.setClazz(integerClass);

            MTArray arguments = new MTArray();
            arguments.add(ten);
            arguments.add(twenty);

            MTObject result = block.value(arguments);

            System.out.println("FAILED : " + result);
        } catch (RuntimeException ex) {
            System.out.println(source + " value: 10 value: 20\n" + ex.getMessage());
        }
    }

    private static void testClosureMutation() {
        System.out.println("=== Closure Mutation test ===");

        MTScope global = new MTScope(null);
        MTClass integerClass = MTKernelBootstrap.createIntegerClass();

        MTInteger zero = new MTInteger(0);
        zero.setClazz(integerClass);
        global.define(MTSymbol.intern("counter"),zero);

        String source =
            """
            [

                counter := counter + 1

            ]
            """;

        MTLexer lexer = new MTLexer(source);

        MTParser parser = new MTParser(lexer.tokenize());

        MTInterpreter interpreter = new MTInterpreter();

        MTBlock block = (MTBlock)interpreter.evaluate(parser.parse(), global);
        assertResult( "counter <- 0. [ counter := counter + 1 ]",block.value());
        assertResult( "[ counter := counter + 1 ]", block.value());

        System.out.println("counter value ==> " + global.lookup(MTSymbol.intern("counter")));
    }

    private static void testClosureWithTemporary() {
        System.out.println("=== Closure With Temporary test ===");

        MTScope global = new MTScope(null);
        MTClass integerClass = MTKernelBootstrap.createIntegerClass();

        MTInteger factor = new MTInteger(2);
        factor.setClazz(integerClass);
        global.define(MTSymbol.intern("factor"),factor);

        String source =
            """
            [:x |

                | y |

                y := x + 1.

                y * factor

            ]""";

        MTLexer lexer = new MTLexer(source);

        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter();

        MTBlock block = (MTBlock)interpreter.evaluate(ast,global);

        MTInteger ten = new MTInteger(10);
        ten.setClazz(integerClass);

        MTArray arguments = new MTArray();
        arguments.add(ten);

        assertResult("factor <- 2.\n"+ source + " value: 10", block.value(arguments));
    }

    private static void testNestedClosure() {
        System.out.println("=== Nested Closure test ===");

        MTScope global = new MTScope(null);

        MTClass integerClass = MTKernelBootstrap.createIntegerClass();

        MTInteger factor = new MTInteger(3);
        factor.setClazz(integerClass);
        global.define(MTSymbol.intern("factor"),factor);

        String source =
            """
            [:x |

                [:y |

                    (x + y) * factor

                ]

            ] """;

        MTLexer lexer = new MTLexer(source);

        MTParser parser = new MTParser(lexer.tokenize());

        MTNode ast = parser.parse();

        MTInterpreter interpreter = new MTInterpreter();

        /*
         * Block externe
         */

        MTBlock outer = (MTBlock)interpreter.evaluate(ast, global);

        /*
         * x = 10
         */

        MTInteger ten = new MTInteger(10);
        ten.setClazz(integerClass);
        MTArray outerArguments = new MTArray();
        outerArguments.add(ten);

        /*
         * Execution du block externe
         * => retourne le block interne
         */

        MTBlock inner = (MTBlock)outer.value(outerArguments);

        /*
         * y = 20
         */

        MTInteger twenty = new MTInteger(20);
        twenty.setClazz(integerClass);
        MTArray innerArguments = new MTArray();
        innerArguments.add(twenty);

        /*
         * Execution du block interne
         */

        assertResult("factor := 3.\n(" + source + " value: 10) value: 20", inner.value(innerArguments));
    }

    private static void testNestedClosureNonLocalReturn() {
        System.out.println("=== Nested Closure Non Local Return test ===");

        String source =
            """
            [

                [:x |

                    ^(x + 1)

                ] value: 41.

                999

            ]
            """;

        MTLexer lexer = new MTLexer(source);

        MTParser parser = new MTParser(lexer.tokenize());
        MTNode ast = parser.parse();
        MTInterpreter interpreter = new MTInterpreter();
        MTScope scope = new MTScope(null);

        MTBlock block = (MTBlock)interpreter.evaluate(ast, scope);

        assertResult(source, block.value());
    }
}
