package DataStructures;

import java.util.*;

// TODO: Redesign compareTo
// TODO: Make sure clauses are sorted before processing

public class Clauses implements Comparable<Clauses> {
    private HashSet<Clause> clauses;

    public Clauses() {
        clauses = new HashSet<>();
    }

    public Clauses(ArrayList<Clause> listOfClauses) {
        clauses = new HashSet<>();
        listOfClauses.forEach(clause -> addClause(clause));
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
    }

    public void addClause(String[] literalsString) {
        clauses.add(new Clause(literalsString));
    }

    public void removeClause(Clause clause) {
        clauses.remove(clause);
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
        for (Clause clause : clauses) {
            isSatisfied &= clause.evaluate(assignment);
            if (!isSatisfied) {
                return false;
            }
        }
        return true;
    }

    public boolean hasConflicts(Assignment assignment) {
        for (Clause clause : clauses) {
            if (clause.hasConflicts(assignment)) {
                return true;
            }
        }
        return false;
    }

    public Literal pickUnassignedLiteral(Assignment assignment) {
        for (Clause clause : clauses) {
            for (Literal literal : clause.toArray()) {
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
        if (clauses.equals(otherClauses.clauses)) {
            return 0;
        } else if (clauses.size() != otherClauses.clauses.size()) {
            return clauses.size() <= otherClauses.clauses.size() ? -1 : 1;
        } else {
            ArrayList<Clause> clausesList = toArray();
            ArrayList<Clause> otherClausesList = otherClauses.toArray();
            for (int index = 0; index < clausesList.size(); index++) {
                int order = clausesList.get(index).compareTo(otherClausesList.get(index));
                if (order != 0) {
                    return order;
                }
            }
            return 0;
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