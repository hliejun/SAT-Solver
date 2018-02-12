package Solvers;

import DataStructures.*;

import java.util.HashSet;

public class DPLLSolver extends Solver {

    public DPLLSolver(Clauses clauses) {
        super(clauses);
    }

    @Override
    public HashSet<Literal> solve() {
        return null;
    }

    @Override
    protected Literal pickBranchingVariable() {
        return null;
    }

    @Override
    protected Boolean propagateUnit() {
        return null;
    }

    @Override
    protected Clauses analyzeConflict() {
        return null;
    }

    @Override
    protected Integer backtrack() {
        return null;
    }

    @Override
    protected Boolean isAllAssigned() {
        return null;
    }

}
