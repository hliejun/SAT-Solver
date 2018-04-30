package Solvers.CDCL;

import DataStructures.*;

public class ERWASolver extends ChaffSolver {

    public ERWASolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    protected Variable pickBranchingVariable() {
        Variable branchVariable = stateGraph.getNextRecencyUnassignedVariable(level);
        //// System.out.println("+ Decision (unassigned): " + branchVariable); ////

        return branchVariable;
    }

}
