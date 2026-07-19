package mt.tests;

import mt.runtime.MTObject;
/*
 * TestUtils : cette classe est un porte-manteau pour des fonctions
 *             utilisees dans chaque test de non regression
 */
public final class TestUtils {

    private TestUtils() {
    }

    /*
     * assertResult : cette fonction permet d'afficher un resultat
     *                d'operation en concatenant les deux operandes
     *                avec un indicateur resultat
     */
    public static void assertResult(
            String expression,
            Object result) {

        System.out.println(
                expression
                + " ==> "
                + result.toString());
    }

    public static void assertEquals(
        String label,
        Object expected,
        Object actual) {

        if (expected.equals(actual)) {
            System.out.println(
                label
                + " expected: "
                + expected
                + " => OK");
        } else {
            System.out.println(
                label
                + " expected: "
                + expected
                + " actual: "
                + actual
                + " => FAILED");
        }
    }
}
