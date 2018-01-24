import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
	String testPath = "./test/testcases/";
	String validFileName = "input1.cnf";
	String invalidFileName = "";
	/*
	 * p cnf 3 2
		1 -3 0
		2 3 -1 0
	 * 
	 * */
	@Test
	public void testParse() {
		Parser parser = new Parser(testPath + validFileName);
		Clauses c = parser.parse();
		
		assertTrue(parser.getNumOfClauses() == 2);
		assertTrue(parser.getNumOfLiterals() == 3);
		assertTrue(c.getClauses().size() == 2);
		
		assertTrue(c.getClauses().get(0).getLiterals().size() == 2);
		assertTrue(c.getClauses().get(0).getLiterals().get(0).getVariable() == 1);
		assertTrue(c.getClauses().get(0).getLiterals().get(0).getValue() == true);
		assertTrue(c.getClauses().get(0).getLiterals().get(1).getVariable() == -3);
		assertTrue(c.getClauses().get(0).getLiterals().get(1).getValue() == false);
		
		assertTrue(c.getClauses().get(1).getLiterals().size() == 3);
		assertTrue(c.getClauses().get(1).getLiterals().get(0).getVariable() == 2);
		assertTrue(c.getClauses().get(1).getLiterals().get(0).getValue() == true);
		assertTrue(c.getClauses().get(1).getLiterals().get(1).getVariable() == 3);
		assertTrue(c.getClauses().get(1).getLiterals().get(1).getValue() == true);
		assertTrue(c.getClauses().get(1).getLiterals().get(2).getVariable() == -1);
		assertTrue(c.getClauses().get(1).getLiterals().get(2).getValue() == false);
	}

}
