package Solvers;

import DataStructures.Assignment;
import DataStructures.Clause;
import DataStructures.Clauses;
import DataStructures.Literal;

import java.util.HashMap;

public class RecursiveDPLLSolver extends Solver {

    private Assignment assignments;
    private boolean isComplete = false;

    public RecursiveDPLLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
        assignments = new Assignment(literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();
        if(check()) {
            return assignments.getAssignValues();
        }
        return null;
    }

    public boolean check() {
        pickBranchingAssignment();

        if (formula.isSat(assignments)) {
            if(!isComplete) {
                isComplete = true;
            }
            return true;
        }

        if (formula.isConflicting(assignments)) {
            return false;
        }

        Literal nextLiteral = formula.chooseUnassignLiteral(assignments);
        if (nextLiteral == null) {
            return false;
        }

        assignments.assign(nextLiteral, true);
        if (check()) {
            return true;
        } else {
            assignments.assign(nextLiteral, false);
            return check();
        }

    }

    protected void pickBranchingAssign() {
        for (Clause c : formula.getClausesSet()) {
            if (!c.isSat(assignments) && !c.isConflicting(assignments) && c.isUnitClause(assignments)) {
                Literal l = c.getUnassignLiteral(assignments);
                if (l.isPositive()) {
                    assignments.assign(l, true);
                } else {
                    assignments.assign(l, false);
                }
            }
        }
    }

    @Override
    protected Literal pickBranchingAssignment() {
        // DPLL does this chronologically, independent of conflicts

        return null;
    }

    @Override
    protected boolean propagateUnit(String variable, Boolean value) {
        // Need to modify the states / attempts here...
        return false;
    }

    @Override
    protected Clauses analyzeConflict() {
        return null;
    }

    @Override
    protected Integer backtrack(Clauses conflicts) {
        // Need to modify the states / attempts here...
        return null;
    }

}
