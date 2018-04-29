/*

// TODO: Research on other CDCL techniques

*** BASIC ***
- Try: Search Restart
- Try: Clause Deletion Policies
- Minimization
- Lightweight Branching

*** ADVANCED ***
- Phase Saving
- Luby Restarts
- Literal Blocks Distance
- Pre-processing / In-processing

 */

public enum Strategy {
    Iterative_DPLL,
    Recursive_DPLL,

    Chaff_CDCL,
    TwoClause_CDCL,
    AllClause_CDCL,
    ERWA_CDCL,
    VSIDS_CDCL,

    Advanced_CDCL
}
