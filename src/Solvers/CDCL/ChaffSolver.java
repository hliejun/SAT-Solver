package Solvers.CDCL;

import DataStructures.*;

import java.util.*;

// TODO: Add lazy evaluation of clause(s)

public class ChaffSolver extends CDCLSolver {

    public ChaffSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();

        performGeneralUnitPropagation();

        if (stateGraph.isConflicted()) {
            //// System.out.println("Fail to propagate before entering loop."); ////
            return null;
        }

        level = 0;

        while (!stateGraph.isAllAssigned()) {
            Variable decision = pickBranchingVariable();
            if (decision == null) {
                //// System.out.println("Fail to make a decision."); ////
                return null;
            }

            level += 1;
            //// System.out.println("Level: " + (level - 1) + " -> " + level); ////

            stateGraph.addDecision(decision, level);
            performDecisionUnitPropagation(decision);

            if (!stateGraph.isConflicted()) {
                continue;
            }

            Clause conflict = stateGraph.getConflictClause();
            Integer proposedLevel = analyzeConflict(conflict);
            if (proposedLevel == null) {
                //// System.out.println("Fail to propose a backtrack level."); ////
                return null;
            }

            backtrack(proposedLevel);

            level += 1;

            performGeneralUnitPropagation();

            if (stateGraph.isConflicted()) {
                //// System.out.println("Fail to propagate after backtracking from conflict."); ////
                return null;
            }
        }

        System.out.println("Satisfiable: " + stateGraph.evaluate(formula)); ////
        return stateGraph.getAssignment();
    }

    @Override
    protected void performGeneralUnitPropagation() {
        HashSet<Clause> clauses = getAllClauses();

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
        HashSet<Clause> clauses = getAllClauses();

        for (Clause clause : clauses) {
            if (stateGraph.isConflicted()) {
                return;
            }

            if (!clause.containsSymbol(decision.getSymbol())) {
                continue;
            }

            if (stateGraph.isConflicted(clause)) {
                stateGraph.addConflict(decision, clause);
                //// System.out.println("Conflict propagating " + decision + " at " + clause); ////
                return;
            }

            Variable impliedVariable = stateGraph.getImpliedVariable(clause, level);
            if (impliedVariable == null) {
                continue;
            }

            stateGraph.addImplication(clause, impliedVariable);
            //// System.out.println("+ Implication: " + impliedVariable); ////

            performDecisionUnitPropagation(impliedVariable);
        }
    }

    @Override
    protected Variable pickBranchingVariable() {
        Variable branchVariable = stateGraph.getNextRandomUnassignedVariable(level);
        //// System.out.println("+ Decision (unassigned): " + branchVariable); ////

        return branchVariable;
    }

    @Override
    protected Integer analyzeConflict(Clause conflictClause) {
        Node<Variable> conflictNode = stateGraph.getConflictNode();
        if (conflictClause == null || conflictNode == null) {
            return null;
        }
        Integer conflictLevel = conflictNode.getValue().getLevel();

        Clause learntClause = new Clause(conflictClause.toArray());
        while(!stateGraph.isAtUniqueImplicationPoint(learntClause, conflictLevel)) {
            Clause antecedentClause = stateGraph.getNextAntecedentClause(learntClause, conflictLevel);

            //// System.out.println("Resolving: " + learntClause + " with " + antecedentClause); ////
            learntClause = applyResolution(learntClause, antecedentClause);
            //// System.out.println(" ... into: " + learntClause); ////
        }

        learntClauses.add(learntClause);

        Integer backtrackLevel = stateGraph.getHighestLevel(learntClause, conflictLevel) - 1;

        return backtrackLevel < 0 ? null : backtrackLevel;
    }

    @Override
    protected void backtrack(int proposedLevel) {
        //// System.out.println("Backtracking: " + level + " -> " + proposedLevel); ////
        stateGraph.revertState(proposedLevel);
        level = proposedLevel;
    }

    protected HashSet<Clause> getAllClauses() {
        HashSet<Clause> clauses = new HashSet<>();
        clauses.addAll(formula.getClausesSet());
        clauses.addAll(learntClauses);

        return clauses;
    }

}
