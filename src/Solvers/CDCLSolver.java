package Solvers;

import DataStructures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CDCLSolver extends Solver {
    private IGraph stateGraph = new IGraph(variables);

    public CDCLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    // TODO: Add learnt clauses and use them for evaluation
    
    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();
        performUnitPropagation();
        if (stateGraph.getConflictClause() != null) {
            System.out.println("Fail to propagate before entering loop.");
            return null;
        }
        while (!stateGraph.isAllAssigned()) {
            Variable decision = pickBranchingVariable();
            if (decision == null) {
                System.out.println("Fail to make a decision.");
                return null;
            }
            level += 1;
            System.out.println("Level: " + (level - 1) + " -> " + level);
            System.out.println("Adding decision to level " + level + " : " + decision);
            stateGraph.addDecision(decision, level);
            performUnitPropagation(decision);
            Clause conflict = stateGraph.getConflictClause();
            if (conflict != null) {
                Integer proposedLevel = analyzeConflict(conflict);
                if (proposedLevel == null) {
                    System.out.println("Fail to propose a backtrack level.");
                    return null;
                }
                backtrack(proposedLevel);
            }
        }
        System.out.println("Satisfiable: " + stateGraph.evaluate(formula));
        return stateGraph.getAssignment();
    }

    private void performUnitPropagation() {
        HashSet<Clause> clauses = formula.getClausesSet();
        for (Clause clause : clauses) {
            Variable impliedVariable = stateGraph.getImpliedVariable(clause, level);
            if (impliedVariable == null) {
                continue;
            }
            stateGraph.addDecision(impliedVariable, level);
            performUnitPropagation(impliedVariable);
            if (stateGraph.getConflictClause() != null) {
                return;
            }
        }
    }

    private void performUnitPropagation(Variable decision) {
        HashSet<Clause> clauses = formula.getClausesSet();
        for (Clause clause : clauses) {
            if (!clause.containsSymbol(decision.getSymbol())) {
                continue;
            }
            if (stateGraph.isConflicted(clause)) {
                stateGraph.addConflict(decision, clause);
                System.out.println("Conflict propagating " + decision + " at " + clause);
                return;
            }
            Variable impliedVariable = stateGraph.getImpliedVariable(clause, level);
            if (impliedVariable != null) {
                stateGraph.addImplication(clause, impliedVariable);
                System.out.println("+ Implication: " + impliedVariable);
                if (stateGraph.isConflicted(clause)) {
                    stateGraph.addConflict(impliedVariable, clause);
                    System.out.println("Conflict implying " + impliedVariable + " using " + clause);
                    return;
                }
                performUnitPropagation(impliedVariable);
            }
        }
    }

    /**
     If no conflict, pick
     #1. Variable State Independent Decaying Sum (state-of-the-art?)
     #2. Exponential Recency Weighted Average
     (https://www.aaai.org/ocs/index.php/AAAI/AAAI16/paper/download/12451/12112)
     */
    private Variable pickBranchingVariable() {
        if (learntClause != null) {
            HashSet<Node<Variable>> previousDecisions = stateGraph.getPreviousDecisions(level);
            if (previousDecisions == null || previousDecisions.size() > 1) {
                System.out.println("Unable to use learnt clause!");
                System.out.println("Previous decisions at " + level + " : " + previousDecisions);
                return null;
            }
            Variable branchingVariable = null;
            for (Node<Variable> node : previousDecisions) {
                branchingVariable = node.getValue().getInverse();
            }
            learntClause = null;
            System.out.println("+ Decision (conflict): " + branchingVariable);
            return branchingVariable;
        }
        Variable branchingVariable = stateGraph.pickUnassignedVariable(level);
        System.out.println("+ Decision (unassigned): " + branchingVariable);
        return branchingVariable;
    }

    private Integer analyzeConflict(Clause conflictClause) {
        Node<Variable> conflictNode = stateGraph.getConflictNode();
        if (conflictClause == null || conflictNode == null) {
            return null; /** This shouldn't happen... **/
        }
        Integer conflictLevel = conflictNode.getValue().getLevel();
        Clause learntClause = new Clause(conflictClause.toArray());
        while(!stateGraph.isAtUniqueImplicationPoint(learntClause, conflictLevel)) {
            Clause antecedentClause = stateGraph.getAntecedentClause(learntClause);
            System.out.println("Resolving: " + learntClause + " with " + antecedentClause);
            learntClause = resolve(learntClause, antecedentClause);
            System.out.println(" ... into: " + learntClause);
        }
        this.learntClause = learntClause;
        Integer highestLevel = stateGraph.getHighestLevel(learntClause, conflictLevel);
        return highestLevel == null ? null : highestLevel - 1;
    }

    private void backtrack(int proposedLevel) {
        System.out.println("BACKTRACK: " + level + " -> " + proposedLevel);
        stateGraph.revertState(proposedLevel);
        level = proposedLevel;
    }

    private Clause resolve(Clause mainClause, Clause targetClause) {
        if (targetClause == null) {
            return mainClause;
        }
        ArrayList<Literal> resolvedLiterals = new ArrayList<>();
        HashMap<String, ArrayList<Literal>> clauseMap = new HashMap<>();
        HashSet<Literal> combinedLiterals = new HashSet<>();
        combinedLiterals.addAll(mainClause.getLiterals());
        combinedLiterals.addAll(targetClause.getLiterals());
        for (Literal literal : combinedLiterals) {
            clauseMap.computeIfAbsent(literal.getName(), key -> new ArrayList<>()).add(literal);
        }
        clauseMap.forEach((symbol, literalSet) -> {
            if (literalSet.size() == 1) {
                resolvedLiterals.add(literalSet.get(0));
            }
        });
        return new Clause(resolvedLiterals);
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