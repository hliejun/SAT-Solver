package Solvers;

import DataStructures.Clauses;
import DataStructures.Literal;

import java.util.HashMap;
import java.util.HashSet;

// Solver outlines the main CDCL algorithm
abstract public class Solver {
    protected final Clauses formula;
    protected final HashSet<String> variables;
    protected final int numOfLiterals;

    // State: Every increment in level inherits assignments from previous levels; backtracking deletes entire level
    protected HashMap<Integer, HashMap<String, Boolean>> state;
    // Attempts: Each assignment made is registered as an attempt for each level
    protected HashMap<Integer, HashSet<Literal>> attempts;
    // Level: The current level that the solver is operating in
    protected Integer level;
    // Learnt Clauses: The "mistakes" that CDCL learnt not to repeat
    protected Clauses learntClauses;

    public Solver(Clauses clauses, int literalsCount) {
        formula = clauses;
        numOfLiterals = literalsCount;
        variables = new HashSet<String>();
        formula.getLiteralSet().forEach(literal -> variables.add(literal.getLiteralName()));
    }

    protected boolean isAllAssigned() {
        return state.get(level).keySet().size() == variables.size();
    }

    protected void resetSolver() {
        level = 0;
        state = new HashMap<Integer, HashMap<String, Boolean>>();
        state.put(level, new HashMap<String, Boolean>());
        attempts = new HashMap<Integer, HashSet<Literal>>();
        attempts.put(level, new HashSet<Literal>());
    }

    protected HashSet<String> getUnassignedVariables() {
        HashSet<String> variableSet = new HashSet<String>(variables);
        variableSet.removeAll(state.get(level).keySet());
        return variableSet;
    }

    // The only exposed method for public use
    abstract public HashMap<String, Boolean> solve();

    // Fundamental methods generic to any CDCL algorithm
    abstract protected Literal pickBranchingAssignment();
    abstract protected boolean propagateUnit(String variable, Boolean value);
    abstract protected Clauses analyzeConflict();
    abstract protected Integer backtrack(Clauses conflicts);

}