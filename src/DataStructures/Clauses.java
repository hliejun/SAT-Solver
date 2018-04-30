package DataStructures;

import java.util.*;
import java.util.stream.Collectors;

public class Clauses implements Comparable<Clauses> {
    private HashSet<Clause> clauses;
    public HashMap<Literal, Integer> literalCount;
    public HashMap<Literal, Integer> twoClauseLiteralCount;

    public Clauses() {
        clauses = new HashSet<>();
        literalCount = new HashMap<>();
        twoClauseLiteralCount = new HashMap<>();
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
    }

    public void addClause(String[] literalsString) {
        Clause clause = new Clause(literalsString);
        updateLiteralCount(clause);
        clauses.add(clause);
    }

    private void updateLiteralCount(Clause clause) {
        for (Literal literal : clause.getLiterals()) {
            if (literalCount.containsKey(literal)) {
                literalCount.put(literal, literalCount.get(literal) + 1);
            } else {
                literalCount.put(literal, 1);
            }

            if (clause.getLiterals().size() == 2 && twoClauseLiteralCount.containsKey(literal)) {
                twoClauseLiteralCount.put(literal, twoClauseLiteralCount.get(literal) + 1);
            } else if (clause.getLiterals().size() == 2){
                twoClauseLiteralCount.put(literal, 1);
            } else {
                twoClauseLiteralCount.put(literal, 0);
            }
        }

        //// !!! Do we need to sort here? !!! ////
        literalCount = literalCount.entrySet()
                .stream()
                .sorted(Map.Entry.<Literal, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void updateLiteralCount(Clause clause, int constant) {
        for (Literal literal : clause.getLiterals()) {
            if (literalCount.containsKey(literal)) {
                literalCount.put(literal, (literalCount.get(literal) + 1) / constant);
            } else {
                literalCount.put(literal, 1);
            }
        }

        literalCount =
                literalCount.entrySet().stream()
                        .sorted(Map.Entry.<Literal, Integer>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
    }

    public HashSet<Clause> getClausesSet() {
        return clauses;
    }

    public HashSet<Literal> getLiteralSet() {
        HashSet<Literal> literals = new HashSet<>();
        clauses.forEach(clause -> literals.addAll(clause.toArray()));
        return literals;
    }

    public HashMap<String, Integer> getFrequencyTable(boolean isTwoClause) {
        HashMap<String, Integer> frequencyTable = new HashMap<>();
        HashMap<Literal, Integer> countTable = isTwoClause ? twoClauseLiteralCount : literalCount;
        countTable.forEach((literal, count) -> frequencyTable.put(literal.getName(), count));

        return frequencyTable;
    }

    public boolean evaluate(Assignment assignment) {
        boolean isSatisfied = true;
//        ArrayList<Clause> clauses = toArray();
        for (Clause clause : clauses) {
            isSatisfied &= clause.evaluate(assignment);
            if (!isSatisfied) {
                return false;
            }
        }
        return true;
    }

    public boolean hasConflicts(Assignment assignment) {
//        ArrayList<Clause> clauses = toArray();
        for (Clause clause : clauses) {
            if (clause.hasConflicts(assignment)) {
                return true;
            }
        }
        return false;
    }

    public Literal pickUnassignedLiteral(Assignment assignment) {
//        ArrayList<Clause> clauses = toArray();
        Literal highestUnassignedLiteral = null;
        for (Clause clause : clauses) {
            ArrayList<Literal> literals = clause.toArray();
            for (Literal literal : literals) {
                if (assignment.getValue(literal.getName()) == null) {
                    if (highestUnassignedLiteral == null) {
                        highestUnassignedLiteral = literal;
                    } else if (literalCount.get(highestUnassignedLiteral) < literalCount.get(literal)) {
                        highestUnassignedLiteral = literal;
                    }
                }
            }
        }
        return highestUnassignedLiteral;
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
