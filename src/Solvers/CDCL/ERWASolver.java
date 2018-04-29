package Solvers.CDCL;

import DataStructures.*;

public class ERWASolver extends ChaffSolver {

    public ERWASolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    protected Variable pickBranchingVariable() {
        Variable branchingVariable = stateGraph.getNextRecencyUnassignedVariable(level);
        //// System.out.println("+ Decision (unassigned): " + branchingVariable); ////

        return branchingVariable;
    }

}
