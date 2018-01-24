import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LiteralTest {
	private String variable;
	private boolean value;
	private final ExpectedException exception = ExpectedException.none();

	@Test
	public void testPostiveLiteral() {
		variable = "1";
		value = true;
		Literal posLiteral = new Literal(variable, value);
		assertTrue(posLiteral.getVariable() == Utilities.convertToInt(variable));
		assertTrue(posLiteral.getValue() == value);
		assertTrue(posLiteral.toString().equals(variable));
	}
	
	@Test
	public void testDoubleDigitPositiveLiteral() {
		variable = "10";
		value = true;
		Literal doublePosLiteral = new Literal(variable, value);
		assertTrue(doublePosLiteral.getVariable() == Utilities.convertToInt(variable));
		assertTrue(doublePosLiteral.getValue() == value);
		assertTrue(doublePosLiteral.toString().equals(variable));
	}
	
	@Test
	public void testNegativeLiteral() {
		variable = "-1";
		value = false;
		Literal negLiteral = new Literal(variable, value);
		assertTrue(negLiteral.getVariable() == Utilities.convertToInt(variable));
		assertTrue(negLiteral.getValue() == value);
		assertTrue(negLiteral.toString().equals(variable));
	}
	
	@Test
	public void testDoubleNegativeLiteral() {
		variable = "-10";
		value = false;
		Literal doubleNegLiteral = new Literal(variable, value);
		assertTrue(doubleNegLiteral.getVariable() == Utilities.convertToInt(variable));
		assertTrue(doubleNegLiteral.getValue() == value);
		assertTrue(doubleNegLiteral.toString().equals(variable));
	}
	
	@Test
	public void test0Literal() {
		variable = "0";
		value = false;
		Literal zeroLiteral = new Literal(variable, value);
		exception.expect(NumberFormatException.class);
	}
	
	@Test
	public void testAlphabetLiteral() {
		variable = "a";
		value = false;
		Literal alphaLiteral = new Literal(variable, value);
		exception.expect(NumberFormatException.class);
	}

	@Test
	public void testPostiveToggle() {
		variable = "10";
		value = true;
		Literal doublePosLiteral = new Literal(variable, value);
		doublePosLiteral.toggle();
		assertTrue(doublePosLiteral.getVariable() == -1 * Utilities.convertToInt(variable));
		assertTrue(doublePosLiteral.getValue() == !value);
		assertTrue(doublePosLiteral.toString().equals("-" + variable));
	}
	
	@Test
	public void testNegativeToggle() {
		variable = "-10";
		value = false;
		String expectedVariable = "10";
		Literal doubleNegLiteral = new Literal(variable, value);
		doubleNegLiteral.toggle();
		assertTrue(doubleNegLiteral.getVariable() == Utilities.convertToInt(expectedVariable));
		assertTrue(doubleNegLiteral.getValue() == !value);
		assertTrue(doubleNegLiteral.toString().equals(expectedVariable));
	}

}
