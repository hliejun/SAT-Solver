package DataStructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Clauses implements Comparable<Clauses> {
    private HashSet<Clause> clauses;

    public Clauses() {
        clauses = new HashSet<Clause>();
    }

    public Clauses(ArrayList<Clause> listOfClauses) {
        clauses = new HashSet<Clause>();
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
        HashSet<Literal> literals = new HashSet<Literal>();
        clauses.forEach(clause -> literals.addAll(clause.getLiteralsSet()));
        return literals;
    }

    public boolean isSat(Assignment assignment) {
        boolean sat = true;
        for (Clause clause : clauses) {
            sat = sat && clause.isSat(assignment);
            // Lazy Evaluation
            if (!sat) {
                return false;
            }
        }
        return true;
    }

    public boolean isConflicting(Assignment assignment) {
        for (Clause clause : clauses) {
            if (clause.isConflicting(assignment)) {
                return true;
            }
        }
        return false;
    }

    public Literal chooseUnassignLiteral(Assignment assignment) {
        for (Clause clause : clauses) {
            for (Literal literal : clause.getLiteralsSet()) {
                if (assignment.isUnassigned(literal.getLiteralName())) {
                    return literal;
                }
            }
        }
        return null;
    }

    public ArrayList<Clause> toArray() {
        ArrayList<Clause> listOfClauses = new ArrayList<Clause>(clauses);
        Collections.sort(listOfClauses);
        return listOfClauses;
    }

    public String toString() {
        return Utilities.implode(toArray(), " ^ ");
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