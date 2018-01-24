import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClausesTest {
	Clauses dummyClauses = new Clauses();
	Literal dummyLit = new Literal("1", true);
	Literal dummyLit2 = new Literal("-2", false);
	Literal dummyLit3 = new Literal("10", true);
	
	@Before
	public void setUp() throws Exception {
		dummyClauses.addClause(new String[] {"1", "10", "0"});
		dummyClauses.addClause(new String[] {"-2", "1", "0"});
		dummyClauses.addClause(new String[] {"1", "-2", "10", "0"});
	}

	@Test
	public void testAddClause() {
		String[] expected = new String[] {"1", "2", "0"};
		Clauses clauses = new Clauses();
		clauses.addClause(expected);
		assertTrue(clauses.getClauses().size() == 1);
		for(int i = 0; i < clauses.getClauses().size(); i ++) {
			Clause c = clauses.getClauses().get(i);
			assertTrue(c.getLiterals().size() == 2);
			for(int j = 0; j < c.getLiterals().size(); j ++) {
				assertTrue(c.getLiterals().get(j).getVariable() == j + 1);
				assertTrue(c.getLiterals().get(j).getValue());
			}
		}
	}

	@Test
	public void testToString() {
		String expected = "(1 V 10) ^ (-2 V 1) ^ (1 V -2 V 10)";
		assertTrue(dummyClauses.toString().equals(expected));
	}

}
