package Solvers.CDCL.Unused;

import DataStructures.*;

/**

*** BASIC ***
- Try: Search Restart
- Try: Clause Deletion Policies
- Try: Watched Literals
- Minimization
- Lightweight Branching

*** ADVANCED ***
- Phase Saving
- Luby Restarts
- Literal Blocks Distance
- Pre-processing / In-processing

*/

public class AdvancedSolver extends VSIDSSolver {

    public AdvancedSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

}
