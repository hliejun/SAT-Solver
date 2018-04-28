package Solvers;

import DataStructures.*;

import java.util.*;

public class ChaffCDCLSolver extends CDCLSolver {

    public ChaffCDCLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {

        // Perform initial unit propagation

        // If conflicted, return null (UNSAT)

        // Initialise decision level = 0

        // While not all variables assigned

            // If backtracked:

                // Unflag backtracked

                // Perform unit propagation on learnt clause

                // If conflicted, return null (UNSAT)

                // Increment decision level

            // Pick branching variable

            // Increment decision level

            // Add decision vertex to state graph

            // Perform unit propagation on decision

            // If conflicted, analyse conflict and get backtrack level

            // If backtrack level < 0, return null (UNSAT)

            // Perform backtrack, set decision level to backtrack level

        // Return solution map (SAT)

        return null;
    }

    @Override
    protected void performUnitPropagation() {

        // For all assertive clauses

            // Make decision on asserted variable

            // Perform unit propagation on decision

            // If conflicted, break and return

    }

    @Override
    protected void performUnitPropagation(Variable decision) {

        // For all clauses (incl. learnt clauses) that contains decision variable

            // If conflicted, add conflict

            // If clause is assertive, add implication

            // If conflicted, add conflict

            // Perform unit propagation on implied variable

    }

    @Override
    protected Variable pickBranchingVariable() {

        // A: Randomly pick an unassigned variable

        // B: Pick an unassigned variable with most frequent occurrences in 2-clauses

        return null;
    }

    @Override
    protected Integer analyzeConflict(Clause conflictClause) {

        // From conflict clause, for all implied variables at decision level until reaching UIP (1 literal @ d.level)

            // Collect from antecedent literals assigned at level < decision level using resolution:

                // Apply resolution

        // Add learnt clause to collection of learnt clauses

        // Return largest level - 1 != decision level from learnt clause variables

        return null;
    }

    @Override
    protected void backtrack(int proposedLevel) {

        // Revert state to proposed level

        // Set current level to proposed level

        // Flag backtracked

    }

    protected Clause applyResolution(Clause intermediateClause, Clause targetClause) {

        // Check rule Chase mentioned...

        return null;
    }

}