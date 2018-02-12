package Solvers;

import java.util.HashMap;
import java.util.HashSet;

import DataStructures.*;

// Solver outlines the main CDCL algorithm
abstract public class Solver {
    protected Clauses clauses;
    protected HashMap<Integer, HashSet<Literal>> levelStates;

    public Solver(Clauses clauses) {
        this.clauses = clauses;
        // TODO: Initialize first level...
    }

    // The only exposed method for public use
    abstract public HashSet<Literal> solve();

    // Fundamental methods generic to any CDCL algorithm
    abstract protected Literal pickBranchingVariable();
    abstract protected Boolean propagateUnit();
    abstract protected Clauses analyzeConflict();
    abstract protected Integer backtrack();
    abstract protected Boolean isAllAssigned();
}