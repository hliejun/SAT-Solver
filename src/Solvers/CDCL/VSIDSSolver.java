package Solvers.CDCL;

import DataStructures.*;

public class VSIDSSolver extends ChaffSolver {

    public VSIDSSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    protected Variable pickBranchingVariable() {

        // VSIDS heuristics for picking branch variables...

        return null;
    }

}
