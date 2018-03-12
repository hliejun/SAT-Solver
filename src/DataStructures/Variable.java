package DataStructures;

public class Variable implements Comparable<Variable> {
    final private String symbol;
    final private boolean value;

    public Variable(String symbol, boolean value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public int compareTo(Variable otherVariable) {
        return symbol.compareTo(otherVariable.symbol);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) {
            return true;
        } else if (!(otherObject instanceof Variable)) {
            return false;
        }
        Variable otherVariable = (Variable) otherObject;
        return symbol == otherVariable.symbol && value == otherVariable.value;
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public String toString() {
        return symbol + "=" + value;
    }

}
