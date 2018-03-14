package Solvers;

import java.util.*;

import DataStructures.*;

public class CDCLSolver extends Solver {
    private IGraph stateGraph = new IGraph(variables);

    public CDCLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

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
                stateGraph.addConflict(clause, level);
                return;
            }
            Variable impliedVariable = stateGraph.getImpliedVariable(clause, level);
            if (impliedVariable != null) {
                stateGraph.addImplication(clause, impliedVariable);
                System.out.println("+ Implication: " + impliedVariable);
                if (stateGraph.isConflicted(clause)) {
                    stateGraph.addConflict(clause, level);
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
            // TODO: Check with decision otherwise return null
            Variable branchingVariable = stateGraph.getImpliedVariable(learntClause, level + 1);
            learntClause = null;
            System.out.println("+ Decision (conflict): " + branchingVariable);
            return branchingVariable;
        }
        Variable branchingVariable = stateGraph.pickUnassignedVariable(level + 1);
        System.out.println("+ Decision (unassigned): " + branchingVariable);
        return branchingVariable;
    }

    private Integer analyzeConflict(Clause conflictClause) {
        // TODO: Check this... seems that the learnt clause does not fit the notes description
        Node<Variable> conflictNode = stateGraph.getConflictNode();
        if (conflictClause == null || conflictNode == null) {
            return null; /** This shouldn't happen... **/
        }
        Integer conflictLevel = conflictNode.getValue().getLevel();
        HashSet<Clause> resolvedClauses = new HashSet<>();
        Clause learntClause = new Clause(conflictClause.toArray());
        LinkedList<Edge<Variable>> queue = new LinkedList<>();
        queue.addAll(stateGraph.getAntecedentEdges(conflictNode));
        while(!queue.isEmpty()) {
            Edge<Variable> antecedentEdge = queue.pollFirst();
            Node<Variable> antecedentNode = antecedentEdge.getSource();
            boolean isSameLevel = antecedentNode.getValue().getLevel() == conflictLevel;
            Clause antecedentClause = antecedentEdge.getAntecedent();
            if (isSameLevel && antecedentClause != null && !resolvedClauses.contains(antecedentClause)) {
                System.out.println("Resolving: " + learntClause + " with " + antecedentClause);
                learntClause = resolve(learntClause, antecedentClause);
                System.out.println(" ... into: " + learntClause);
                resolvedClauses.add(antecedentClause);
                if (!stateGraph.isAtUniqueImplicationPoint(learntClause, conflictLevel)) {
                    queue.addAll(stateGraph.getAntecedentEdges(antecedentNode));
                }
            }
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

    private Clause resolve(Clause firstClause, Clause secondClause) {
        // TODO: Check this... seems that the learnt clause does not fit the notes description
        ArrayList<Literal> resolvedLiterals = new ArrayList<>();
        HashMap<String, ArrayList<Literal>> clauseMap = new HashMap<>();
        HashSet<Literal> combinedLiterals = new HashSet<>();
        combinedLiterals.addAll(firstClause.getLiterals());
        combinedLiterals.addAll(secondClause.getLiterals());
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