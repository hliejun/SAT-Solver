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
        Literal literal = new Literal("1");
        assertTrue("Literal should yield true sign.", literal.getSign());
        assertTrue("Literal should evaluate to true.", literal.evaluate(true));
        assertEquals("Literal should be named: '1'.", "1", literal.getName());
        assertEquals("Literal should be converted to string: '1'.", "1", literal.toString());
    }

    @Test
    public void testNegativeValueNumericLiteral() {
        Literal literal = new Literal("1");
        assertTrue("Literal should yield true sign.", literal.getSign());
        assertFalse("Literal should evaluate to false.", literal.evaluate(false));
        assertEquals("Literal should be named: '1'", "1", literal.getName());
        assertEquals("Literal should be converted to string: '1'.", "1", literal.toString());
    }

    @Test
    public void testNegativeSignNumericLiteral() {
        Literal literal = new Literal("-1");
        assertFalse("Literal should yield false sign.", literal.getSign());
        assertFalse("Literal should evaluate to false.", literal.evaluate(true));
        assertEquals("Literal should be named: '1'.", "1", literal.getName());
        assertEquals("Literal should be converted to string: '-1'.", "-1", literal.toString());
    }

    @Test
    public void testNegativeSignValueLiteral() {
        Literal literal = new Literal("-1");
        assertFalse("Literal should yield false sign.", literal.getSign());
        assertTrue("Literal should evaluate to true.", literal.evaluate(false));
        assertEquals("Literal should be named: '1'.", "1", literal.getName());
        assertEquals("Literal should be converted to string: '-1'.", "-1", literal.toString());
    }

    @Test
    public void testZeroLabelledLiteral() {
        Literal literal = new Literal("0");
        assertTrue("Literal should yield true sign.", literal.getSign());
        assertTrue("Literal should evaluate to true.", literal.evaluate(true));
        assertEquals("Literal should be named: '0'.", "0", literal.getName());
        assertEquals("Literal should be converted to string: '0'.", "0", literal.toString());
    }

    @Test
    public void testAlphabetLiteral() {
        Literal literal = new Literal("-A");
        assertFalse("Literal should yield false sign.", literal.getSign());
        assertTrue("Literal should evaluate to true.", literal.evaluate(false));
        assertEquals("Literal should be named: 'A'.", "A", literal.getName());
        assertEquals("Literal should be converted to string: '-A'.", "-A", literal.toString());
    }

    @Test
    public void testComparableLiterals() {
        Literal literalA = new Literal("A");
        Literal literalNegativeA = new Literal("-A");
        Literal literalB = new Literal("B");
        Literal literalNegativeB = new Literal("-B");
        Literal literalC = new Literal("C");
        Literal literalNegativeC = new Literal("-C");
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
