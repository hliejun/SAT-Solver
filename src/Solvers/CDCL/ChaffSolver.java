package Solvers.CDCL;

import DataStructures.*;

import java.util.*;

//// !!! UNSAT 2 FAILING !!! ////

public class ChaffSolver extends CDCLSolver {

    public ChaffSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();

        performGeneralUnitPropagation();

        if (stateGraph.isConflicted()) {

            System.out.println("Fail to propagate before entering loop."); ////

            return null;
        }

        level = 0;

        while (!stateGraph.isAllAssigned()) {
            Variable decision = pickBranchingVariable();
            if (decision == null) {

                System.out.println("Fail to make a decision."); ////

                return null;
            }

            level += 1;

            System.out.println("Level: " + (level - 1) + " -> " + level); ////

            stateGraph.addDecision(decision, level);
            performDecisionUnitPropagation(decision);

            if (stateGraph.isConflicted()) {
                Clause conflict = stateGraph.getConflictClause();
                Integer proposedLevel = analyzeConflict(conflict);

                if (proposedLevel == null) {

                    System.out.println("Fail to propose a backtrack level."); ////

                    return null;
                }

                backtrack(proposedLevel);

                performGeneralUnitPropagation();

                //// !!! Do we check for conflict here? !!! ////

                level += 1;
            }
        }

        System.out.println("Satisfiable: " + stateGraph.evaluate(formula)); ////

        return stateGraph.getAssignment();
    }

    @Override
    protected void performGeneralUnitPropagation() {
        HashSet<Clause> clauses = new HashSet<>();
        clauses.addAll(formula.getClausesSet());
        clauses.addAll(learntClauses);

        for (Clause clause : clauses) {
            Variable impliedVariable = stateGraph.getImpliedVariable(clause, level);
            if (impliedVariable == null) {
                continue;
            }

            stateGraph.addDecision(impliedVariable, level);
            performDecisionUnitPropagation(impliedVariable);

            if (stateGraph.isConflicted()) {
                return;
            }
        }
    }

    @Override
    protected void performDecisionUnitPropagation(Variable decision) {
        HashSet<Clause> clauses = new HashSet<>();
        clauses.addAll(formula.getClausesSet());
        clauses.addAll(learntClauses);

        for (Clause clause : clauses) {
            if (!clause.containsSymbol(decision.getSymbol())) {
                continue;
            }

            if (stateGraph.isConflicted(clause)) {
                if (stateGraph.getConflictClause() == null) {
                    stateGraph.addConflict(decision, clause);
                }

                System.out.println("Conflict propagating " + decision + " at " + clause); ////

                return;
            }

            Variable impliedVariable = stateGraph.getImpliedVariable(clause, level);
            if (impliedVariable != null) {
                stateGraph.addImplication(clause, impliedVariable);

                System.out.println("+ Implication: " + impliedVariable); ////

                if (stateGraph.isConflicted(clause)) {
                    if (stateGraph.getConflictClause() == null) {
                        stateGraph.addConflict(impliedVariable, clause);
                    }

                    System.out.println("Conflict implying " + impliedVariable + " using " + clause); ////

                    return;
                }

                performDecisionUnitPropagation(impliedVariable);
            }
        }
    }

    @Override
    protected Variable pickBranchingVariable() {

        // TODO: Pick an unassigned variable with most frequent occurrences in 2-clauses (see Clauses)

        Variable branchingVariable = stateGraph.pickNextUnassignedVariable(level);

        System.out.println("+ Decision (unassigned): " + branchingVariable); ////

        return branchingVariable;
    }

    @Override
    protected Integer analyzeConflict(Clause conflictClause) {
        Node<Variable> conflictNode = stateGraph.getConflictNode();
        if (conflictClause == null || conflictNode == null) {
            return null;
        }
        Integer conflictLevel = conflictNode.getValue().getLevel();

        System.out.println("CONFLICT: " + conflictNode); ////

        Clause learntClause = new Clause(conflictClause.toArray());
        while(!stateGraph.isAtUniqueImplicationPoint(learntClause, conflictLevel)) {

            // TODO: Fix getting of next antecedent clause...

            Clause antecedentClause = stateGraph.getNextAntecedentClause(learntClause, conflictLevel);

            System.out.println("Resolving: " + learntClause + " with " + antecedentClause); ////

            learntClause = applyResolution(learntClause, antecedentClause);

            System.out.println(" ... into: " + learntClause); ////

        }

        learntClauses.add(learntClause);

        // A: Return (largest level - 1) < decision level from learnt clause variables
        Integer highestLevel = stateGraph.getHighestLevel(learntClause, conflictLevel);

        // B: Return (second largest level + 1) < decision level from learnt clause variables
        // TODO: Implement to compare...

        return highestLevel == null ? null : highestLevel - 1;
    }

    @Override
    protected void backtrack(int proposedLevel) {

        System.out.println("Backtracking: " + level + " -> " + proposedLevel); ////

        stateGraph.revertState(proposedLevel);
        level = proposedLevel;
    }

}