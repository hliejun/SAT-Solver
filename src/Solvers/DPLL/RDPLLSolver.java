package Solvers.DPLL;

import java.util.HashMap;

import DataStructures.*;
import Solvers.Solver;

public class RDPLLSolver extends Solver {
    private Assignment satisfiedAssignments;
    private Assignment workingAssignments;

    public RDPLLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
        workingAssignments = new Assignment(literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();
        if(resolve(workingAssignments)) {
            System.out.println("Assigned: " + satisfiedAssignments.getAllValues().size() + " / " + variables.size());
            System.out.println("Unassigned: " + getUnassignedVariables(satisfiedAssignments));
            return satisfiedAssignments.getAllValues();
        }
        return null;
    }

    public boolean resolve(Assignment assignments) {
        Assignment currentAssignments = assignments.copy();
        assignLoneClauses(currentAssignments);
        if (formula.evaluate(currentAssignments) && currentAssignments.getAllValues().size() == variables.size()) {
            satisfiedAssignments = currentAssignments;
            return true;
        }
        if (formula.hasConflicts(currentAssignments)) {
            return false;
        }
        Literal pickedLiteral = formula.pickUnassignedLiteral(currentAssignments);
        if (pickedLiteral == null) {
            return false;
        }
        currentAssignments.assignValue(pickedLiteral.getName(), true);
        if (resolve(currentAssignments)) {
            return true;
        } else {
            currentAssignments.assignValue(pickedLiteral.getName(), false);
            return resolve(currentAssignments);
        }
    }

    protected void assignLoneClauses(Assignment assignment) {
        for (Clause clause : formula.getClausesSet()) {
            if (!clause.evaluate(assignment) && !clause.hasConflicts(assignment) && clause.isUnitClause(assignment)) {
                Literal literal = clause.getUnitLiteral(assignment);
                assignment.assignValue(literal.getName(), literal.getSign());
            }
        }
    }

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
        return null;
    }

}
