import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class ClauseTest {

	Clause clause;
	Literal dummyLiteral = new Literal("1", true);
	Literal dummyLiteral2 = new Literal("2", true);
	
	@Before
	public void setUp() throws Exception {
		clause = new Clause();
		clause.add(dummyLiteral);
		clause.add(dummyLiteral2);
	}

	@Test
	public void testAdd() {
		clause.add(new Literal("3", true));
		ArrayList<Literal> literals = clause.getLiterals();
		assertTrue(literals.size() == 3);
		for(int i = 0; i < literals.size(); i++) {
			assertTrue(literals.get(i).getVariable() == i + 1);
			assertTrue(literals.get(i).getValue() == true);
		}
	}

	@Test
	public void testRemove() {
		clause.remove(dummyLiteral);
		clause.remove(dummyLiteral2);
		assertTrue(clause.getLiterals().size() == 0);
		assertFalse(clause.getLiterals().contains(dummyLiteral));
	}

	@Test
	public void testGetClause() {
		assertTrue(clause.getLiterals().size() == 2);
		assertTrue(clause.getLiterals().contains(dummyLiteral));
		assertTrue(clause.getLiterals().contains(dummyLiteral2));
	}

	@Test
	public void testToString() {
		String expected = "(" + dummyLiteral.getVariable() + " V " +
				dummyLiteral2.getVariable() + ")";
		assertTrue(expected.equals(clause.toString()));
	}

}
