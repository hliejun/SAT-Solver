package Solvers.CDCL;

import DataStructures.*;

import java.util.*;

public class ChaffSolver extends CDCLSolver {

    protected HashMap<Clause, ArrayList<Clause>> resolutionMap;
    protected Clause resolutionEntry;

    public ChaffSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
        resolutionMap = new HashMap<>();
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();

        performGeneralUnitPropagation();

        if (stateGraph.isConflicted()) {
            /** CONFLICT **/
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
            System.out.println("Decision: " + decision); ////

            performDecisionUnitPropagation(decision);

            if (!stateGraph.isConflicted()) {
                continue;
            }

            Clause conflict = stateGraph.getConflictClause();
            Integer proposedLevel = analyzeConflict(conflict);
            if (proposedLevel == null) {
                /** CONFLICT **/
                System.out.println("Fail to propose a backtrack level."); ////
                return null;
            }

            backtrack(proposedLevel);

            level += 1;

            performGeneralUnitPropagation();

            if (stateGraph.isConflicted()) {
                /** CONFLICT **/
                Clause learntClause = new Clause(resolutionEntry.toArray());
                ArrayList<Clause> chain = getRootClauses(learntClause);

                // TODO: Prove UNSAT by performing resolution with clauses
                // 4. Apply resolution strategy (try linear, if not try combi? Re-look at AIMA...)
                // 5. Check for resolved empty clause

                HashSet<Clause> uniqueChain = new HashSet<>();
                uniqueChain.addAll(chain);
                chain = new ArrayList<>();
                chain.addAll(uniqueChain);

                Clause resolutionClause = new Clause(stateGraph.getConflictClause().toArray());

                // FIXME: Explore all orders?
//                for (Clause clause : chain) {
//                    System.out.println("[UNSAT] Resolving: " + resolutionClause + " with " + clause); ////
//                    resolutionClause = applyResolution(resolutionClause, clause);
//                    System.out.println("[UNSAT]  ... into: " + resolutionClause); ////
//                }

                System.out.println("Resolved clause: " + resolutionClause);

                System.out.println("Root clauses: " + chain);

                System.out.println("Conflict clause: " + stateGraph.getConflictClause());

                System.out.println("Fail to propagate after backtracking from conflict."); ////
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

            Boolean clauseTruth = stateGraph.evaluate(clause);

            if (clauseTruth == null) {
                Variable impliedVariable = stateGraph.getImpliedVariable(clause, level);

                if (impliedVariable == null) {
                    continue;
                }

                stateGraph.addImplication(clause, impliedVariable);
                System.out.println("+ Implication: " + impliedVariable); ////

                performDecisionUnitPropagation(impliedVariable);
            } else if (!clauseTruth) {
                stateGraph.addConflict(decision, clause);
                System.out.println("Conflict propagating " + decision + " at " + clause); ////
                return;
            }
        }
    }

    @Override
    protected Variable pickBranchingVariable() {
        Variable branchVariable = stateGraph.getNextRandomUnassignedVariable(level);
        System.out.println("+ Decision (unassigned): " + branchVariable); ////

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

        ArrayList<Clause> resolutionChain = new ArrayList<>();
        resolutionChain.add(conflictClause);

        while(!stateGraph.isAtUniqueImplicationPoint(learntClause, conflictLevel)) {
            Clause antecedentClause = stateGraph.getNextAntecedentClause(learntClause, conflictLevel);

            System.out.println("Resolving: " + learntClause + " with " + antecedentClause); ////
            learntClause = applyResolution(learntClause, antecedentClause);
            resolutionChain.add(antecedentClause);
            System.out.println(" ... into: " + learntClause); ////
        }

        learntClauses.add(learntClause);
        resolutionMap.put(learntClause, resolutionChain);
        resolutionEntry = learntClause;

        Integer highestLevel = stateGraph.getHighestLevel(learntClause, conflictLevel);

        if (highestLevel == null) {
            System.out.println("Fail to obtain highest level for backtrack."); ////
            return null;
        }

        Integer backtrackLevel = highestLevel == 0 ? 0 : highestLevel - 1;
        System.out.println("Proposed backtrack level: " + backtrackLevel); ////

        return backtrackLevel < 0 ? null : backtrackLevel;
    }

    @Override
    protected void backtrack(int proposedLevel) {
        System.out.println("Backtracking: " + level + " -> " + proposedLevel); ////
        stateGraph.revertState(proposedLevel);
        level = proposedLevel;
    }

    @Override
    protected void resetSolver() {
        super.resetSolver();
        resolutionMap = new HashMap<>();
    }

    private HashSet<Clause> getAllClauses() {
        HashSet<Clause> clauses = new HashSet<>();
        clauses.addAll(formula.getClausesSet());
        clauses.addAll(learntClauses);

        return clauses;
    }

    private ArrayList<Clause> getRootClauses(Clause learntClause) {
        ArrayList<Clause> rootClauses = new ArrayList<>();

        ArrayList<Clause> chain = resolutionMap.get(learntClause);
        if (chain == null) {
            System.out.println("Fail to propagate after backtracking from conflict."); ////
            return null;
        }

        rootClauses.addAll(chain);

        while(!isRoot(rootClauses)) {
            ArrayList<Clause> clauses = new ArrayList<>(rootClauses);
            ArrayList<Clause> intermediateClauses = new ArrayList<>();

            for (Clause clause : clauses) {
                ArrayList<Clause> chainClauses = resolutionMap.get(clause);
                if (chainClauses != null) {
                    rootClauses.remove(clause);
                    intermediateClauses.addAll(chainClauses);
                }
            }
            rootClauses.addAll(0, intermediateClauses);
        }

        return rootClauses;
    }

    private boolean isRoot(ArrayList<Clause> clauses) {
        for(Clause clause : clauses) {
            if (resolutionMap.get(clause) != null) {
                return false;
            }
        }

        return true;
    }

}
