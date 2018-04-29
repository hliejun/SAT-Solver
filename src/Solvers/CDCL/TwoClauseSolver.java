package Solvers.CDCL;

import DataStructures.*;

public class TwoClauseSolver extends ChaffSolver {

    public TwoClauseSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    protected Variable pickBranchingVariable() {
        Variable branchingVariable = stateGraph.getNextTwoClauseUnassignedVariable(level);
        //// System.out.println("+ Decision (unassigned): " + branchingVariable); ////

        return branchingVariable;
    }
}
