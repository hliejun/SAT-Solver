import static org.junit.Assert.*;
import org.junit.Test;

import DataStructures.*;

public class ParserTest {
    String testPath = "./test/testcases/";
    String validFileName = "input1.cnf";

    /*
     *  p  cnf  3  2
        1  -3   0
        2   3  -1  0
     *
     * */
    @Test
    public void testParse() {
        Parser parser = new Parser(testPath + validFileName);
        Clauses clauses = parser.getParsedClauses();

        assertEquals("Number of clauses should be 2.", 2, parser.getNumOfClauses());
        assertEquals("Number of literals should be 3.", 3, parser.getNumOfLiterals());

        assertEquals(
                "Number of clauses in clause set should be 2.",
                2,
                clauses.getClausesSet().size()
        );

        assertEquals(
                "First clause in clause set should have 2 literals.",
                2,
                clauses.toArray().get(0).toArray().size()
        );

        assertEquals(
                "First literal in first clause should be named: 1.",
                "1",
                clauses.toArray().get(0).toArray().get(0).getName()
        );
        assertTrue(
                "First literal in first clause should have sign: true.",
                clauses.toArray().get(0).toArray().get(0).getSign()
        );

        assertEquals(
                "Second literal in first clause should be named: 3.",
                "3",
                clauses.toArray().get(0).toArray().get(1).getName()
        );
        assertFalse(
                "Second literal in first clause should have sign: false.",
                clauses.toArray().get(0).toArray().get(1).getSign()
        );

        assertEquals(
                "Second clause in clause set should have 3 literals.",
                3,
                clauses.toArray().get(1).toArray().size()
        );

        assertEquals(
                "First literal in second clause should be named: 1.",
                "1",
                clauses.toArray().get(1).toArray().get(0).getName()
        );
        assertFalse(
                "First literal in second clause should have sign: false.",
                clauses.toArray().get(1).toArray().get(0).getSign()
        );

        assertEquals(
                "Second literal in second clause should be named: 2.",
                "2",
                clauses.toArray().get(1).toArray().get(1).getName()
        );
        assertTrue(
                "Second literal in second clause should have sign: true.",
                clauses.toArray().get(1).toArray().get(1).getSign()
        );

        assertEquals(
                "Third literal in second clause should be named: 3.",
                "3",
                clauses.toArray().get(1).toArray().get(2).getName()
        );
        assertTrue(
                "Third literal in second clause should have sign: true.",
                clauses.toArray().get(1).toArray().get(2).getSign()
        );
    }
}
