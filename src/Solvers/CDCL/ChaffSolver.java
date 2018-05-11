package Solvers.CDCL;

import DataStructures.*;

import java.util.*;

public class ChaffSolver extends CDCLSolver {

    public ChaffSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();

        performGeneralUnitPropagation();

        if (stateGraph.isConflicted()) {


            /** CONFLICT **/


            //System.out.println("Fail to propagate before entering loop."); ////
            return null;
        }

        level = 0;

        while (!stateGraph.isAllAssigned()) {
            Variable decision = pickBranchingVariable();
            if (decision == null) {
                //System.out.println("Fail to make a decision."); ////
                return null;
            }

            level += 1;
            //System.out.println("Level: " + (level - 1) + " -> " + level); ////

            stateGraph.addDecision(decision, level);
            //System.out.println("Decision: " + decision); ////

            performDecisionUnitPropagation(decision);

            if (!stateGraph.isConflicted()) {
                continue;
            }

            Clause conflict = stateGraph.getConflictClause();
            Integer proposedLevel = analyzeConflict(conflict);
            if (proposedLevel == null) {


                /** CONFLICT **/


                //System.out.println("Fail to propose a backtrack level."); ////
                return null;
            }

            backtrack(proposedLevel);

            level += 1;

            performGeneralUnitPropagation();

            if (stateGraph.isConflicted()) {



                /** CONFLICT **/
                //Clause conflictClause = stateGraph.getConflictClause();
                ////System.out.println("CONFLICT at: " + conflictClause);
                //analyzeConflict(conflictClause);

                // TODO: Prove UNSAT by performing resolution with clauses
                    // 1. Take the most recent learnt clause
                    // 2. Get all base clauses (array) from learnt clause map
                    // 3. Take conflict clause
                    // 4. Apply resolution strategy
                    // 5. Check for resolved empty clause



                //System.out.println("Fail to propagate after backtracking from conflict."); ////
                return null;
            }
        }

        //System.out.println("Satisfiable: " + stateGraph.evaluate(formula)); ////
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

            Boolean clauseTruth = stateGraph.evaluate(clause);

            if (clauseTruth == null) {
                Variable impliedVariable = stateGraph.getImpliedVariable(clause, level);

                if (impliedVariable == null) {
                    continue;
                }

                stateGraph.addImplication(clause, impliedVariable);
                //System.out.println("+ Implication: " + impliedVariable); ////

                performDecisionUnitPropagation(impliedVariable);
            } else if (!clauseTruth) {
                stateGraph.addConflict(decision, clause);
                //System.out.println("Conflict propagating " + decision + " at " + clause); ////
                return;
            }
        }
    }

    @Override
    protected Variable pickBranchingVariable() {
        Variable branchVariable = stateGraph.getNextRandomUnassignedVariable(level);
        //System.out.println("+ Decision (unassigned): " + branchVariable); ////

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



        // TODO: Capture resolution clauses in order mapped to learnt clause
            // 1. Collect an array (ordered) of conflict + antecedent clauses
            // 2. Map array to learnt clause



        while(!stateGraph.isAtUniqueImplicationPoint(learntClause, conflictLevel)) {
            Clause antecedentClause = stateGraph.getNextAntecedentClause(learntClause, conflictLevel);

            //System.out.println("Resolving: " + learntClause + " with " + antecedentClause); ////
            learntClause = applyResolution(learntClause, antecedentClause);
            //System.out.println(" ... into: " + learntClause); ////
        }



        // TODO: Set most recent learnt clause variable for later reference
        


        learntClauses.add(learntClause);

        Integer highestLevel = stateGraph.getHighestLevel(learntClause, conflictLevel);

        if (highestLevel == null) {
            //System.out.println("Fail to obtain highest level for backtrack."); ////
            return null;
        }

        Integer backtrackLevel = highestLevel == 0 ? 0 : highestLevel - 1;
        //System.out.println("Proposed backtrack level: " + backtrackLevel); ////

        return backtrackLevel < 0 ? null : backtrackLevel;
    }

    @Override
    protected void backtrack(int proposedLevel) {
        //System.out.println("Backtracking: " + level + " -> " + proposedLevel); ////
        stateGraph.revertState(proposedLevel);
        level = proposedLevel;
    }

    private HashSet<Clause> getAllClauses() {
        HashSet<Clause> clauses = new HashSet<>();
        clauses.addAll(formula.getClausesSet());
        clauses.addAll(learntClauses);

        return clauses;
    }

}
