package Solvers;

import java.util.HashMap;

import DataStructures.*;

public class CDCLSolver extends Solver {
    protected IGraph stateGraph = new IGraph(variables);

    public CDCLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();
        performUnitPropagation();
        if (stateGraph.getConflictClause() != null) {
            return null;
        }
        while (!stateGraph.isAllAssigned()) {
            Variable decision = pickBranchingVariable();
            level += 1;
            stateGraph.addDecision(decision, level);
            performUnitPropagation(decision);
            if (stateGraph.getConflictClause() != null) {
                Integer proposedLevel = analyzeConflict();
                if (proposedLevel == null) {
                    return null;
                }
                backtrack(proposedLevel);
            }
        }
        return stateGraph.getAssignment();
    }

    protected void performUnitPropagation() {
        // Scan for lone clauses
        // Add decisions
        // For each decision made, perform unit propagation on assignment
        // If assignment has conflict, break
    }

    protected void performUnitPropagation(Variable decision) {
        // Given a literal assignment, scan for affected clauses
        // For all affected clause:
            // If affected clause is complete and conflicted, add conflict and break
            // If affected clause is incomplete and lone, get implied literal and add implication
            // Recurse propagation on implied literal
    }

    protected Variable pickBranchingVariable() {
        // Select assignment based on stateGraph
        return null;
    }

    protected Integer analyzeConflict() {
        // Get conflict clause
        // Starting from conflict clause and conflict node K, chain resolve:
        // i = 1 : Use conflict clause as intermediary clause
        // i > 1 : Backtrace edges to implied literals
            // If assigned at current decision level, resolve with its antecedent
            // If not assigned at current decision level, retain same intermediary clause
        // i > 1, source == null : Reached decision literal, stop

        // Alternatively, get all literals assigned at decision level < current, and select current level decision

        // Use GRASP or CHAFF approach to determine decision level to backtrack to
        return null;
    }

    protected void backtrack(int proposedLevel) {
        stateGraph.revertState(proposedLevel);
        level = proposedLevel;
    }

    @Override
    protected void resetSolver() {
        super.resetSolver();
        stateGraph = new IGraph(variables);
    }

    /**
        Due to restructuring (to fit the described algorithm in the readings),
        the following abstract methods are unsupported.
     */

    @Override
    protected Literal pickBranchingAssignment() {
        return null;
    }
    @Override
    protected Assignment propagateUnit(Literal literal) {
        return null;
    }

    @Override
    protected Clauses analyzeConflict(Assignment assignment) {
        return null;
    }

    @Override
    protected Integer backtrack(Clauses conflicts) {
        return 0;
    }

}