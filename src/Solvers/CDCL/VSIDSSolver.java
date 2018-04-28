package Solvers.CDCL;

import DataStructures.*;

/**
 If no conflict, pick
 #1. Variable State Independent Decaying Sum (state-of-the-art?)
 #2. Exponential Recency Weighted Average
 (https://www.aaai.org/ocs/index.php/AAAI/AAAI16/paper/download/12451/12112)
 */
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
