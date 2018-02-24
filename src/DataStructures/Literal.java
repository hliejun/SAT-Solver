package DataStructures;

// TODO: Redesign compareTo

public class Literal implements Comparable<Literal> {
    private final boolean sign;
    private final String label;

    public Literal(String variable) {
        sign = Utilities.isPositive(variable);
        label = Utilities.removeSignsAndSpaces(variable);
    }

    public Literal(String label, boolean sign) {
        this.sign = sign;
        this.label = Utilities.removeSignsAndSpaces(label);
    }

    public String getName() {
        return label;
    }

    public boolean getSign() {
        return sign;
    }

    public Literal getInverse() {
        return new Literal(label, !sign);
    }

    public boolean hasConflicts(Assignment assignment) {
        if (assignment.getValue(label) == null) {
            return false;
        }
        return !evaluate(assignment.getValue(label));
    }

    public Boolean evaluate(boolean truthValue) {
        return !(sign ^ truthValue);
    }

    public String toString() {
        return sign ? label : String.format("¬%s", label);
    }

    @Override
    public int compareTo(Literal otherLiteral) {
        if (!label.equals(otherLiteral.label)) {
            return label.compareTo(otherLiteral.label);
        } else if (sign != otherLiteral.sign) {
            return sign ? 1 : -1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) {
            return true;
        } else if (!(otherObject instanceof Literal)) {
            return false;
        }
        Literal otherLiteral = (Literal) otherObject;
        return sign == otherLiteral.sign && label.equals(otherLiteral.label);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}