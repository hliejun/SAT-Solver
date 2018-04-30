package DataStructures;

import java.util.*;
import java.util.stream.Collectors;

public class Clauses implements Comparable<Clauses> {

    private HashSet<Clause> clauses;

    private HashMap<Literal, Integer> literalCount;
    private HashMap<Literal, Integer> twoClauseLiteralCount;

    private TreeMap<String, Integer> symbolCount;
    private TreeMap<String, Integer> twoClauseSymbolCount;

    public Clauses() {
        clauses = new HashSet<>();

        literalCount = new HashMap<>();
        twoClauseLiteralCount = new HashMap<>();

        symbolCount = new TreeMap<>();
        twoClauseSymbolCount = new TreeMap<>();
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
    }

    public void addClause(String[] literalsString) {
        Clause clause = new Clause(literalsString);

        updateLiteralCount(clause);
        updateSymbolCount(clause);

        clauses.add(clause);
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

    //// !!! Does this ignore literal signs? !!! ////
    public Literal pickUnassignedLiteral(Assignment assignment) {
        Literal pickedLiteral = null;

        for (Clause clause : clauses) {
            ArrayList<Literal> literals = clause.toArray();
            for (Literal literal : literals) {
                Boolean literalValue = assignment.getValue(literal.getName());
                if (literalValue == null
                        && (pickedLiteral == null || literalCount.get(pickedLiteral) < literalCount.get(literal))) {
                    pickedLiteral = literal;
                }
            }
        }

        return pickedLiteral;
    }

    public void sortLiteralCount() {
        literalCount = literalCount.entrySet()
                .stream()
                .sorted(Map.Entry.<Literal, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        twoClauseLiteralCount = twoClauseLiteralCount.entrySet()
                .stream()
                .sorted(Map.Entry.<Literal, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
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
    }

    public TreeMap<String, Integer> getFrequencyTable(boolean isTwoClause) {
//        HashMap<String, Integer> frequencyTable = new HashMap<>();
//        HashMap<Literal, Integer> countTable = isTwoClause ? twoClauseLiteralCount : literalCount;
//        countTable.forEach((literal, count) -> frequencyTable.put(literal.getName(), count));
//
//        return frequencyTable;

        return isTwoClause ? twoClauseSymbolCount : symbolCount;
    }

    public void sortSymbolCount() {
        symbolCount = symbolCount.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, TreeMap::new));

        twoClauseSymbolCount = twoClauseSymbolCount.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, TreeMap::new));
    }

    private void updateSymbolCount(Clause clause) {
        int clauseSize = clause.getLiterals().size();

        for (Literal literal : clause.getLiterals()) {
            String symbol = literal.getName();

            if (symbolCount.containsKey(symbol)) {
                symbolCount.put(symbol, symbolCount.get(symbol) + 1);
            } else {
                symbolCount.put(symbol, 1);
            }

            if (clauseSize == 2 && twoClauseSymbolCount.containsKey(symbol)) {
                twoClauseSymbolCount.put(symbol, twoClauseSymbolCount.get(symbol) + 1);
            } else if (clauseSize == 2){
                twoClauseSymbolCount.put(symbol, 1);
            } else {
                twoClauseSymbolCount.put(symbol, 0);
            }
        }
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

    /**
     * Unused methods
     */

    public void updateLiteralCount(Clause clause, int constant) {
        HashSet<Literal> literals = clause.getLiterals();
        for (Literal literal : literals) {
            if (literalCount.containsKey(literal)) {
                literalCount.put(literal, (literalCount.get(literal) + 1) / constant);
            } else {
                literalCount.put(literal, 1);
            }
        }
    }

}
