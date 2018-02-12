import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import DataStructures.*;

public class ClauseTest {
    Clause clause;
    Literal literalA, literalNotB, literalC;
    ArrayList<Literal> testLiterals;

    @Before
    public void setUp() throws Exception {
        literalA = new Literal("A", true);
        literalNotB = new Literal("-B", true);
        literalC = new Literal("C", false);
        testLiterals = new ArrayList<Literal>();
        testLiterals.add(literalA);
        testLiterals.add(literalNotB);
        testLiterals.add(literalC);
        clause = new Clause(testLiterals);
    }

    @Test
    public void testAdd() {
        Literal newLiteral = new Literal("D", true);
        clause.addLiteral(newLiteral);
        HashSet<Literal> literals = clause.getLiteralsSet();
        assertEquals("Clause should contain 4 literals.", 4, literals.size());
        assertTrue("Clause should contain newly added literal.", literals.contains(newLiteral));
    }

    @Test
    public void testRemove() {
        clause.removeLiteral(literalA);
        HashSet<Literal> literals = clause.getLiteralsSet();
        assertFalse("Clause should not contain removed literal.", literals.contains(literalA));
        assertEquals("Clause should contain 2 literals after removal.", 2, literals.size());
    }

    @Test
    public void testGetClause() {
        assertEquals("Clause should contain 3 literals.", 3, clause.getLiteralsSet().size());
        assertTrue("Clause should contain literal: 'A'.", clause.getLiteralsSet().contains(literalA));
        assertTrue("Clause should contain literal: '-B'.", clause.getLiteralsSet().contains(literalNotB));
        assertTrue("Clause should contain literal: 'C'.", clause.getLiteralsSet().contains(literalC));
    }

    @Test
    public void testToString() {
        assertEquals(
                "Clause should be converted to string: '(A V -B V C)'.",
                "(A V -B V C)",
                clause.toString()
        );
    }

    @Test
    public void testComparableClauses() {
        Clause firstClause = new Clause(new ArrayList<Literal>(testLiterals.subList(0, 1)));
        Clause secondClause = new Clause(new ArrayList<Literal>(testLiterals.subList(2, 3)));
        Clause thirdClause = new Clause(new ArrayList<Literal>(testLiterals.subList(0, 2)));
        Clause fourthClause = new Clause(testLiterals);
        ArrayList<Clause> clauses = new ArrayList<Clause>();
        clauses.add(fourthClause);
        clauses.add(firstClause);
        clauses.add(secondClause);
        clauses.add(thirdClause);
        Collections.sort(clauses);
        assertEquals(
                "First sorted clause should have size: 1.",
                1,
                clauses.remove(0).getLiteralsSet().size()
        );
        assertEquals(
                "Second sorted clause should have size: 1.",
                1,
                clauses.remove(0).getLiteralsSet().size()
        );
        assertEquals(
                "Third sorted clause should have size: 2.",
                2,
                clauses.remove(0).getLiteralsSet().size()
        );
        assertEquals(
                "Fourth sorted clause should have size: 3.",
                3,
                clauses.remove(0).getLiteralsSet().size()
        );
    }
}