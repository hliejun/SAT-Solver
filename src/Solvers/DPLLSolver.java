package Solvers;

import DataStructures.*;

import java.util.HashMap;

public class DPLLSolver extends Solver {

    public DPLLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();
        while (!isAllAssigned()) {
            Literal assignment = pickBranchingAssignment();
            if (assignment == null) {
                return null; // Cannot find an assignment: UNSAT
            }
            if (propagateUnit(assignment.getLiteralName(), assignment.getLiteralValue())) {
                level += 1;
                continue;
            }
            Clauses conflicts = analyzeConflict();
            level = backtrack(conflicts);
            if (level == null) {
                return null; // Cannot determine a backtrack point: UNSAT
            }
        }
        return state.get(level);
    }

    @Override
    protected Literal pickBranchingAssignment() {
        // DPLL does this chronologically, independent of conflicts
        return null;
    }

    @Override
    protected boolean propagateUnit(String variable, Boolean value) {
        // Need to modify the states / attempts here...
        return false;
    }

    @Override
    protected Clauses analyzeConflict() {
        return null;
    }

    @Override
    protected Integer backtrack(Clauses conflicts) {
        // Need to modify the states / attempts here...
        return null;
    }

}
