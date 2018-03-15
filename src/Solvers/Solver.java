package Solvers;

import java.util.*;

import DataStructures.*;

abstract public class Solver {
    protected final Clauses formula;
    protected final HashSet<String> variables;
    protected final int numOfLiterals;
    protected HashMap<Integer, Assignment> state;
    protected Integer level;
    protected HashSet<Clause> learntClauses;
    protected Variable conflictVariable;

    public Solver(Clauses clauses, int literalsCount) {
        formula = clauses;
        numOfLiterals = literalsCount;
        variables = new HashSet<>();
        formula.getLiteralSet().forEach(literal -> variables.add(literal.getName()));
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
        learntClauses = new HashSet<>();
        conflictVariable = null;
    }

    protected HashSet<String> getUnassignedVariables(Assignment state) {
        HashSet<String> variableSet = new HashSet<>(variables);
        variableSet.removeAll(state.getAllValues().keySet());
        return variableSet;
    }

    protected Assignment getLevelState() {
        return state.get(level);
    }

    abstract public HashMap<String, Boolean> solve();

    abstract protected Literal pickBranchingAssignment();
    abstract protected Assignment propagateUnit(Literal literal);
    abstract protected Clauses analyzeConflict(Assignment assignment);
    abstract protected Integer backtrack(Clauses conflicts);

}
