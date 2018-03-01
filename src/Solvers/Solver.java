package Solvers;

import java.util.*;

import DataStructures.*;

abstract public class Solver {
    protected final Clauses formula;
    protected final HashSet<String> variables;
    protected final int numOfLiterals;
    protected HashMap<Integer, Assignment> state;
    protected Integer level;
    protected Clauses learntClauses;

    public Solver(Clauses clauses, int literalsCount) {
        formula = clauses;
        numOfLiterals = literalsCount;
        variables = new HashSet<>();
        formula.getLiteralSet().forEach(literal -> variables.add(literal.getName()));
        learntClauses = null;
        resetSolver();
    }

    protected boolean isSatisfied() {
        Assignment currentState = state.get(level);
        return formula.evaluate(currentState) && currentState.getAllValues().size() == variables.size();
    }

    protected void resetSolver() {
        state = new HashMap<>();
        level = 0;
        state.put(level, new Assignment(variables.size()));
    }

    protected HashSet<String> getUnassignedVariables(Assignment state) {
        HashSet<String> variableSet = new HashSet<>(variables);
        variableSet.removeAll(state.getAllValues().keySet());
        return variableSet;
    }

    abstract public HashMap<String, Boolean> solve();

    abstract protected Literal pickBranchingAssignment();
    abstract protected Assignment propagateUnit(Literal literal);
    abstract protected Clauses analyzeConflict(Assignment assignment);
    abstract protected Integer backtrack(Clauses conflicts);

}

// TODO: Abstract data structure comparisons to Solver

//    public int compareTo(Clause otherClause) {
//        if (literals.equals(otherClause.literals)) {
//            return 0;
//        } else if (literals.size() != otherClause.literals.size()) {
//            return literals.size() <= otherClause.literals.size() ? -1 : 1;
//        } else {
//            ArrayList<Literal> clauseLiterals = toArray();
//            ArrayList<Literal> otherLiterals = otherClause.toArray();
//            int order = 0;
//            for (int index = 0; index < clauseLiterals.size(); index++) {
//                for (int otherIndex = 0; otherIndex < otherLiterals.size(); otherIndex++) {
//                    order += clauseLiterals.get(index).compareTo(otherLiterals.get(otherIndex));
//                }
//            }
//            return -1 * order;
//        }
//    }

//    public int compareTo(Clauses otherClauses) {
//        if (clauses.equals(otherClauses.clauses)) {
//            return 0;
//        } else if (clauses.size() != otherClauses.clauses.size()) {
//            return clauses.size() <= otherClauses.clauses.size() ? -1 : 1;
//        } else {
//            ArrayList<Clause> clausesList = toArray();
//            ArrayList<Clause> otherClausesList = otherClauses.toArray();
//            for (int index = 0; index < clausesList.size(); index++) {
//                int order = clausesList.get(index).compareTo(otherClausesList.get(index));
//                if (order != 0) {
//                    return order;
//                }
//            }
//            return 0;
//        }
//    }
