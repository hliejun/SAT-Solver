package DataStructures;

import java.util.*;

public class Clauses implements Comparable<Clauses> {
    private HashSet<Clause> clauses;

    public Clauses() {
        clauses = new HashSet<>();
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
    }

    public void addClause(String[] literalsString) {
        clauses.add(new Clause(literalsString));
    }

    public HashSet<Clause> getClausesSet() {
        return clauses;
    }

    public HashSet<Literal> getLiteralSet() {
        HashSet<Literal> literals = new HashSet<>();
        clauses.forEach(clause -> literals.addAll(clause.toArray()));
        return literals;
    }

    public boolean evaluate(Assignment assignment) {
        boolean isSatisfied = true;
        ArrayList<Clause> clauses = toArray();
        for (Clause clause : clauses) {
            isSatisfied &= clause.evaluate(assignment);
            if (!isSatisfied) {
                return false;
            }
        }
        return true;
    }

    public boolean hasConflicts(Assignment assignment) {
        ArrayList<Clause> clauses = toArray();
        for (Clause clause : clauses) {
            if (clause.hasConflicts(assignment)) {
                return true;
            }
        }
        return false;
    }

    public Literal pickUnassignedLiteral(Assignment assignment) {
        ArrayList<Clause> clauses = toArray();
        for (Clause clause : clauses) {
            ArrayList<Literal> literals = clause.toArray();
            for (Literal literal : literals) {
                if (assignment.getValue(literal.getName()) == null) {
                    return literal;
                }
            }
        }
        return null;
    }

    public ArrayList<Clause> toArray() {
        ArrayList<Clause> listOfClauses = new ArrayList<>(clauses);
        Collections.sort(listOfClauses);
        return listOfClauses;
    }

    public String toString() {
        return Utilities.implode(toArray(), " ∧ ");
    }

    @Override
    public int compareTo(Clauses otherClauses) {
        if (clauses.size() == otherClauses.clauses.size()) {
            return 0;
        } else {
            return clauses.size() < otherClauses.clauses.size() ? -1 : 1;
        }
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) {
            return true;
        } else if (!(otherObject instanceof Clauses)) {
            return false;
        } else {
            return this.clauses.equals(((Clauses) otherObject).clauses);
        }
    }

}
