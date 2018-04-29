package Solvers.CDCL;

import DataStructures.*;

public class AllClauseSolver extends ChaffSolver {

    public AllClauseSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    protected Variable pickBranchingVariable() {
        Variable branchingVariable = stateGraph.getNextMostUnassignedVariable(level, formula.getFrequencyTable());
        //// System.out.println("+ Decision (unassigned): " + branchingVariable); ////

        return branchingVariable;
    }

}
