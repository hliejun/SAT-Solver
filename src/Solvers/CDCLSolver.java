package Solvers;

import DataStructures.*;

import java.util.HashMap;

public class CDCLSolver extends Solver {

    public CDCLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
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
    protected Assignment propagateUnit(Literal literal) {
        return null;
    }

    @Override
    protected Clauses analyzeConflict(Assignment assignment) {
        return null;
    }

    @Override
    protected Integer backtrack(Clauses conflicts) {
        // TODO: Every backtrack will need to evaluate from learnt clauses
        return null;
    }

}
