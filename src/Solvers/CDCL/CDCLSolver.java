package Solvers.CDCL;

import DataStructures.*;
import Solvers.Solver;

import java.util.*;

abstract public class CDCLSolver extends Solver {

    protected IGraph stateGraph;

    public CDCLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
        stateGraph = new IGraph(variables);
    }

    abstract protected void performGeneralUnitPropagation();

    abstract protected void performDecisionUnitPropagation(Variable decision);

    abstract protected Variable pickBranchingVariable();

    abstract protected Integer analyzeConflict(Clause conflictClause);

    abstract protected void backtrack(int proposedLevel);

    protected Clause applyResolution(Clause intermediateClause, Clause targetClause) {
        if (targetClause == null) {
            return intermediateClause;
        }

        ArrayList<Literal> resolvedLiterals = new ArrayList<>();
        HashMap<String, ArrayList<Literal>> clauseMap = new HashMap<>();
        HashSet<Literal> combinedLiterals = new HashSet<>();

        combinedLiterals.addAll(intermediateClause.getLiterals());
        combinedLiterals.addAll(targetClause.getLiterals());

        for (Literal literal : combinedLiterals) {
            clauseMap.computeIfAbsent(literal.getName(), key -> new ArrayList<>()).add(literal);
        }

        int complementCount = 0;
        for (Map.Entry<String, ArrayList<Literal>> entry : clauseMap.entrySet()) {
            ArrayList<Literal> literalSet = entry.getValue();
            if (literalSet.size() == 1) {
                resolvedLiterals.add(literalSet.get(0));
            } else if (literalSet.size() > 1) {
                complementCount += 1;
            }
        }

        //return complementCount > 1 ? intermediateClause : new Clause(resolvedLiterals);
        return new Clause(resolvedLiterals);
    }

    @Override
    abstract public HashMap<String, Boolean> solve();

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