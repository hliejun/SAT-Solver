import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;

import DataStructures.*;

public class LiteralTest {
    private String variable;
    private boolean value;
    private final ExpectedException exception = ExpectedException.none();

    @Test
    public void testPostiveValueNumericLiteral() {
        Literal literal = new Literal("1", true);
        assertTrue("Literal should yield true sign.", literal.getLiteralSign());
        assertTrue(" Literal should yield true value.", literal.getLiteralValue());
        assertTrue("Literal should evaluate to true.", literal.evaluate());
        assertEquals("Literal should be named: '1'.", "1", literal.getLiteralName());
        assertEquals("Literal should be converted to string: '1'.", "1", literal.toString());
    }

    @Test
    public void testNegativeValueNumericLiteral() {
        Literal literal = new Literal("1", false);
        assertTrue("Literal should yield true sign.", literal.getLiteralSign());
        assertFalse("Literal should yield false value.", literal.getLiteralValue());
        assertFalse("Literal should evaluate to false.", literal.evaluate());
        assertEquals("Literal should be named: '1'", "1", literal.getLiteralName());
        assertEquals("Literal should be converted to string: '1'.", "1", literal.toString());
    }

    @Test
    public void testNegativeSignNumericLiteral() {
        Literal literal = new Literal("-1", true);
        assertFalse("Literal should yield false sign.", literal.getLiteralSign());
        assertTrue("Literal should yield true value.", literal.getLiteralValue());
        assertFalse("Literal should evaluate to false.", literal.evaluate());
        assertEquals("Literal should be named: '1'.", "1", literal.getLiteralName());
        assertEquals("Literal should be converted to string: '-1'.", "-1", literal.toString());
    }

    @Test
    public void testNegativeSignValueLiteral() {
        Literal literal = new Literal("-1", false);
        assertFalse("Literal should yield false sign.", literal.getLiteralSign());
        assertFalse("Literal should yield false value.", literal.getLiteralValue());
        assertTrue("Literal should evaluate to true.", literal.evaluate());
        assertEquals("Literal should be named: '1'.", "1", literal.getLiteralName());
        assertEquals("Literal should be converted to string: '-1'.", "-1", literal.toString());
    }

    @Test
    public void testZeroLabelledLiteral() {
        Literal literal = new Literal("0", true);
        assertTrue("Literal should yield true sign.", literal.getLiteralSign());
        assertTrue("Literal should yield true value.", literal.getLiteralValue());
        assertTrue("Literal should evaluate to true.", literal.evaluate());
        assertEquals("Literal should be named: '0'.", "0", literal.getLiteralName());
        assertEquals("Literal should be converted to string: '0'.", "0", literal.toString());
    }

    @Test
    public void testNullValueLiteral() {
        Literal literal = new Literal("1");
        assertTrue("Literal should yield true sign.", literal.getLiteralSign());
        assertNull("Literal should yield null value.", literal.getLiteralValue());
        assertNull("Literal should evaluate to null.", literal.evaluate());
        assertEquals("Literal should be named: '1'.", "1", literal.getLiteralName());
        assertEquals("Literal should be converted to string: '1'.", "1", literal.toString());
    }

    @Test
    public void testAlphabetLiteral() {
        Literal literal = new Literal("-A", false);
        assertFalse("Literal should yield false sign.", literal.getLiteralSign());
        assertFalse("Literal should yield false value.", literal.getLiteralValue());
        assertTrue("Literal should evaluate to true.", literal.evaluate());
        assertEquals("Literal should be named: 'A'.", "A", literal.getLiteralName());
        assertEquals("Literal should be converted to string: '-A'.", "-A", literal.toString());
    }

    @Test
    public void testPostiveValueToggle() {
        Literal literal = new Literal("-A", false);
        assertTrue("Literal should evaluate to true.", literal.evaluate());
        literal.toggleValue();
        assertFalse("Literal should retain false sign after value toggle.", literal.getLiteralSign());
        assertTrue("Literal should yield true value after value toggle.", literal.getLiteralValue());
        assertFalse("Literal should evaluate to false after value toggle.", literal.evaluate());
        assertEquals("Literal should be converted to string: '-A'.", "-A", literal.toString());
    }

    @Test
    public void testNegativeValueToggle() {
        Literal literal = new Literal("A", true);
        assertTrue("Literal should evaluate to true.", literal.evaluate());
        literal.toggleValue();
        assertTrue("Literal should retain true sign after value toggle.", literal.getLiteralSign());
        assertFalse("Literal should yield false value after value toggle.", literal.getLiteralValue());
        assertFalse("Literal should evaluate to false after value toggle.", literal.evaluate());
        assertEquals("Literal should be converted to string: 'A'.", "A", literal.toString());
    }

    @Test
    public void testComparableLiterals() {
        Literal literalA = new Literal("A", false);
        Literal literalNegativeA = new Literal("-A", true);
        Literal literalB = new Literal("B");
        Literal literalNegativeB = new Literal("-B", false);
        Literal literalC = new Literal("C", true);
        Literal literalNegativeC = new Literal("-C", true);
        ArrayList<Literal> literalsList = new ArrayList<Literal>();
        literalsList.add(literalC);
        literalsList.add(literalNegativeB);
        literalsList.add(literalNegativeA);
        literalsList.add(literalA);
        literalsList.add(literalNegativeC);
        literalsList.add(literalB);
        Collections.sort(literalsList);
        assertEquals("First sorted literal should be: '-A'.", literalNegativeA, literalsList.remove(0));
        assertEquals("Second sorted literal should be: 'A'.", literalA, literalsList.remove(0));
        assertEquals("Third sorted literal should be: '-B'.", literalNegativeB, literalsList.remove(0));
        assertEquals("Fourth sorted literal should be: 'B'.", literalB, literalsList.remove(0));
        assertEquals("Fifth sorted literal should be: '-C'.", literalNegativeC, literalsList.remove(0));
        assertEquals("Sixth sorted literal should be: 'C'.", literalC, literalsList.remove(0));
    }
}
