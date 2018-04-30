package Solvers.CDCL;

import DataStructures.*;

import java.util.*;

public class AllClauseSolver extends ChaffSolver {

    public AllClauseSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    protected Variable pickBranchingVariable() {
        HashMap<String, Integer> frequencyTable = formula.getFrequencyTable(false);
        Variable branchVariable = stateGraph.getNextMostUnassignedVariable(level, frequencyTable, false);
        //// System.out.println("+ Decision (unassigned): " + branchVariable); ////

        return branchVariable;
    }

}
