package Solvers;

import DataStructures.Assignment;
import DataStructures.Clause;
import DataStructures.Clauses;
import DataStructures.Literal;

import java.util.HashMap;

public class RecursiveDPLLSolver extends Solver {

    private Assignment satAssignments;
    private boolean isComplete = false;
    private Assignment workingAssignments;

    public RecursiveDPLLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
        workingAssignments = new Assignment(literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();
        if(check(workingAssignments)) {
            return satAssignments.getAssignValues();
        }
        return null;
    }

    public boolean check(Assignment assignments) {
        Assignment currAssignments = assignments.clone();
        pickBranchingAssign(currAssignments);

        if (formula.isSat(currAssignments)) {
            if(!isComplete) {
                isComplete = true;
            }
            satAssignments = currAssignments;
            return true;
        }

        if (formula.isConflicting(currAssignments)) {
            return false;
        }

        Literal nextLiteral = formula.chooseUnassignLiteral(currAssignments);
        if (nextLiteral == null) {
            return false;
        }

        currAssignments.assign(nextLiteral, true);
        if (check(currAssignments)) {
            return true;
        } else {
            currAssignments.assign(nextLiteral, false);
            return check(currAssignments);
        }

    }

    protected void pickBranchingAssign(Assignment assignment) {
        for (Clause c : formula.getClausesSet()) {
            if (!c.isSat(assignment) && !c.isConflicting(assignment) && c.isUnitClause(assignment)) {
                Literal l = c.getUnitLiteral(assignment);
                if (l.isPositive()) {
                    assignment.assign(l, true);
                } else {
                    assignment.assign(l, false);
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
