import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ClausesTest {
    Clauses clauses;
    Clause clauseA, clauseB, clauseC;
    Literal literalA, literalNotB, literalC;
    ArrayList<Literal> literals;

    @Before
    public void setUp() throws Exception {
        literalA = new Literal("A", true);
        literalNotB = new Literal("-B", false);
        literalC = new Literal("C", true);
        literals = new ArrayList<Literal>();
        literals.add(literalA);
        literals.add(literalNotB);
        literals.add(literalC);
        clauseA = new Clause(new ArrayList<Literal>(literals.subList(0, 2)));
        clauseB = new Clause(new ArrayList<Literal>(literals.subList(1, 3)));
        clauseC = new Clause(literals);
        clauses = new Clauses();
        clauses.addClause(clauseA);
        clauses.addClause(clauseB);
        clauses.addClause(clauseC);
    }

    @Test
    public void testAddClause() {
        Clause clauseD = new Clause(new ArrayList<Literal>(literals.subList(1, 2)));
        assertEquals("This clause set should contain 3 clauses.", 3, clauses.getClausesSet().size());
        clauses.addClause(clauseD);
        assertEquals(
                "This clause set should contain 4 clauses after adding.",
                4,
                clauses.getClausesSet().size()
        );
        assertTrue(
                "This clause set should contain the newly added clause.",
                clauses.getClausesSet().contains(clauseD)
        );
    }

    @Test
    public void testToString() {
        assertEquals(
                "This clause set should be converted to string: '(A V -B) ^ (-B V C) ^ (A V -B V C)'.",
                "(A V -B) ^ (-B V C) ^ (A V -B V C)",
                clauses.toString()
        );
    }
}
