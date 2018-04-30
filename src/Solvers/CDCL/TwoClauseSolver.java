package Solvers.CDCL;

import DataStructures.*;

import java.util.*;

public class TwoClauseSolver extends ChaffSolver {

    public TwoClauseSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    protected Variable pickBranchingVariable() {
        HashMap<String, Integer> frequencyTable = formula.getFrequencyTable(true);
        Variable branchVariable = stateGraph.getNextMostUnassignedVariable(level, frequencyTable, false);
        //// System.out.println("+ Decision (unassigned): " + branchVariable); ////

        return branchVariable;
    }
}
