package DataStructures;

public class Literal implements Comparable<Literal> {
    private boolean sign; // negative = false
    private final String label;
    private Boolean truthValue;

    public Literal(String variable) {
        sign = Utilities.isPositive(variable);
        label = Utilities.removeSignsAndSpaces(variable);
        truthValue = null;
    }

    public Literal(String variable, boolean value) {
        this(variable);
        truthValue = value;
    }

    public void toggleValue() {
        truthValue = !truthValue;
    }

    public void toggleSign() {
        sign = !sign;
    }

    public boolean isPositive() {
        return sign;
    }

    public boolean isConflicting(Assignment assignment) {
        Boolean assignTruth = assignment.getAssignValue(getLiteralInteger() - 1);
        if (assignTruth == null) {
            return false;
        }

        return (assignTruth && !isPositive()) || (!assignTruth && isPositive());
    }

    public Boolean getLiteralValue() {
        return truthValue;
    }

    public String getLiteralName() {
        return label;
    }

    public int getLiteralInteger() { return Integer.parseInt(label); }

    public Boolean evaluate() {
        return truthValue == null ? null : !(sign ^ truthValue);
    }

    public String toString() {
        return sign ? label : String.format("-%s", label);
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
        if ((truthValue != null && truthValue.equals(otherLiteral.truthValue))
                || (truthValue == null && otherLiteral.truthValue == null)) {
            return sign == otherLiteral.sign && label.equals(otherLiteral.label);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

}