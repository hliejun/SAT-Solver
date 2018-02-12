package Solvers;

import DataStructures.*;

import java.util.HashMap;

public class CDCLSolver extends Solver {

    public CDCLSolver(Clauses clauses) {
        super(clauses);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        return null;
    }

    @Override
    protected Literal pickBranchingAssignment() {
        return null;
    }

    @Override
    protected boolean propagateUnit(String variable, Boolean value) {
        return false;
    }

    @Override
    protected Clauses analyzeConflict() {
        return null;
    }

    @Override
    protected Integer backtrack(Clauses conflicts) {
        return null;
    }

}
