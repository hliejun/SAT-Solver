package DataStructures;

public class Variable implements Comparable<Variable> {
    final private String symbol;
    final private boolean value;
    private int level;

    public Variable(String symbol, boolean value, int level) {
        this.symbol = symbol;
        this.value = value;
        this.level = level;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean getValue() {
        return value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int newLevel) {
        level = newLevel;
    }

    public Variable getInverse() {
        return new Variable(symbol, !value, level);
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
        return symbol.equals(otherVariable.symbol)
                && value == otherVariable.value
                && level == otherVariable.level;
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public String toString() {
        return symbol + "@" + level + "=" + value;
    }

}
