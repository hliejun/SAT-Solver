package Solvers;

import java.util.*;

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
            if (decision == null) {
                return null;
            }
            level += 1;
            stateGraph.addDecision(decision, level);
            performUnitPropagation(decision);
            Clause conflict = stateGraph.getConflictClause();
            if (conflict != null) {
                Integer proposedLevel = analyzeConflict(conflict);
                if (proposedLevel == null) {
                    return null;
                }
                backtrack(proposedLevel);
            }
        }
        return stateGraph.getAssignment();
    }

    protected void performUnitPropagation() {
        HashSet<Clause> clauses = formula.getClausesSet();
        for (Clause clause : clauses) {
            Variable loneVariable = stateGraph.getLoneVariable(clause);
            if (loneVariable == null) {
                continue;
            }
            stateGraph.addDecision(loneVariable, level);
            performUnitPropagation(loneVariable);
            if (stateGraph.getConflictClause() != null) {
                return;
            }
        }
    }

    protected void performUnitPropagation(Variable decision) {
        HashSet<Clause> clauses = formula.getClausesSet();
        for (Clause clause : clauses) {
            if (!clause.containsVariable(decision.getSymbol())) {
                continue;
            }
            if (stateGraph.isConflicted(clause)) {
                stateGraph.addConflict(clause, level);
                return;
            }
            Variable loneVariable = stateGraph.getLoneVariable(clause);
            if (loneVariable != null) {
                stateGraph.addDecision(loneVariable, level);
                performUnitPropagation(loneVariable);
            }
        }
    }

    protected Variable pickBranchingVariable() {
        // TODO: Select assignment based on stateGraph
        // If has conflict, pick learnt clause implied variable
        // If no conflict, pick?
            // #0. Random / Linear
            // #1. Variable State Independent Decaying Sum (state-of-the-art?)
            // #2. Exponential Recency Weighted Average
            //     (https://www.aaai.org/ocs/index.php/AAAI/AAAI16/paper/download/12451/12112)
        return null;
    }

    protected Integer analyzeConflict(Clause conflictClause) {
        Node<Variable> conflictNode = stateGraph.getConflictNode();
        if (conflictClause == null || conflictNode == null) {
            return null; /** This shouldn't happen... **/
        }
        Integer conflictLevel = stateGraph.getLevelOfNode(conflictNode);
        HashSet<Clause> resolvedClauses = new HashSet<>();
        Clause learntClause = new Clause(conflictClause.toArray());
        LinkedList<Edge<Variable>> queue = new LinkedList<>();
        queue.addAll(stateGraph.getAntecedentEdges(conflictNode));
        while(!queue.isEmpty() && !stateGraph.isAtUniqueImplicationPoint(learntClause, conflictLevel)) {
            Edge<Variable> antecedentEdge = queue.pollFirst();
            Node<Variable> antecedentNode = antecedentEdge.getSource();
            boolean isSameLevel = stateGraph.getLevelOfNode(antecedentNode) == conflictLevel;
            Clause antecedentClause = antecedentEdge.getAntecedent();
            if (isSameLevel && antecedentClause != null && !resolvedClauses.contains(antecedentClause)) {
                learntClause = resolve(learntClause, antecedentClause);
                resolvedClauses.add(antecedentClause);
                queue.addAll(stateGraph.getAntecedentEdges(antecedentNode));
            }
        }
        // TODO: Backtrack to latest level (of conflict clause variables)
        return null;
    }

    protected void backtrack(int proposedLevel) {
        stateGraph.revertState(proposedLevel);
        level = proposedLevel;
    }

    protected Clause resolve(Clause firstClause, Clause secondClause) {
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