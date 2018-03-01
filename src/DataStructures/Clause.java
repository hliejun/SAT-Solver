package DataStructures;

import java.util.*;

public class Clause implements Comparable<Clause> {
    private HashSet<Literal> literals;

    public Clause(ArrayList<Literal> listOfLiterals) {
        literals = new HashSet<>();
        listOfLiterals.forEach(literal -> addLiteral(literal));
    }

    public Clause(String[] literalsString) {
        literals = new HashSet<>();
        for (String literalCharacter : literalsString) {
            literals.add(new Literal(literalCharacter));
        }
    }

    public void addLiteral(Literal literal) {
        literals.add(literal);
    }

    public void removeLiteral(Literal literal) {
        literals.remove(literal);
    }

    public boolean evaluate(Assignment assignment) {
        ArrayList<Literal> literals = toArray();
        for (Literal literal : literals) {
            Boolean variableValue = assignment.getValue(literal.getName());
            if (variableValue != null && literal.evaluate(variableValue)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasConflicts(Assignment assignment) {
        ArrayList<Literal> literals = toArray();
        for (Literal literal: literals) {
            if (!literal.hasConflicts(assignment)) {
                return false;
            }
        }
        return true;
    }

    public boolean isUnitClause(Assignment assignment) {
        int numOfUnassignedLiterals = 0;
        ArrayList<Literal> literals = toArray();
        for (Literal literal : literals) {
            if (assignment.getValue(literal.getName()) == null) {
                numOfUnassignedLiterals++;
            }
        }
        return numOfUnassignedLiterals == 1;
    }

    public Literal getUnitLiteral(Assignment assignment) {
        if (!isUnitClause(assignment)) {
            return null;
        }
        ArrayList<Literal> literals = toArray();
        for (Literal literal : literals) {
            if (assignment.getValue(literal.getName()) == null) {
                return literal;
            }
        }
        return null;
    }

    public ArrayList<Literal> toArray() {
        ArrayList<Literal> listOfLiterals = new ArrayList<>(literals);
        Collections.sort(listOfLiterals);
        return listOfLiterals;
    }

    public String toString() {
        return String.format("(%s)", Utilities.implode(toArray(), " ∨ "));
    }

    @Override
    public int compareTo(Clause otherClause) {
        if (literals.size() == otherClause.literals.size()) {
            return 0;
        } else {
            return literals.size() < otherClause.literals.size() ? -1 : 1;
        }
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) {
            return true;
        } else if (!(otherObject instanceof Clause)) {
            return false;
        } else {
            return this.literals.equals(((Clause) otherObject).literals);
        }
    }

}
