/*

// TODO: Research on other CDCL techniques

*** BASIC ***
- Minimization
- Search Restart
- Lightweight Branching
- Clause Deletion Policies

*** ADVANCED ***
- Phase Saving
- Luby Restarts
- Literal Blocks Distance
- Pre-processing / In-processing

 */

public enum Strategy {
    Iterative_DPLL, Recursive_DPLL, TwoClause_CDCL, Chaff_CDCL, VSIDS_CDCL
}