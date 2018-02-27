package Solvers;

import DataStructures.*;

import java.util.*;

public class DPLLSolver extends Solver {

    public DPLLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();
        while (!isSatisfied()) {
            assignLoneClause();
            if (isSatisfied()) {
                break;
            }
            Assignment levelState = state.get(level);
            Literal selectedLiteral = pickBranchingAssignment();
            if (selectedLiteral == null && level == 0) {
                return null;
            }
            if (formula.hasConflicts(levelState) || selectedLiteral == null) {
                Clauses conflicts = analyzeConflict(levelState);
                level = backtrack(conflicts);
                continue;
            }
            Assignment propagatedState = propagateUnit(selectedLiteral);
            level += 1;
            state.put(level, propagatedState);
        }
        System.out.println("Assigned: " + state.get(level).getAllValues().size() + " / " + variables.size());
        System.out.println("Unassigned: " + getUnassignedVariables(state.get(level)));
        return state.get(level).getAllValues();
    }

    protected void assignLoneClause() {
        Assignment levelState = state.get(level);
        for (Clause clause : formula.getClausesSet()) {
            if (!clause.evaluate(levelState) && !clause.hasConflicts(levelState) && clause.isUnitClause(levelState)) {
                Literal literal = clause.getUnitLiteral(levelState);
                levelState.assignValue(literal.getName(), literal.getSign());
            }
        }
    }

    @Override
    protected Literal pickBranchingAssignment() {
        ArrayList<Literal> attemptedList = state.get(level).getAttempts();
        if (attemptedList == null || attemptedList.isEmpty()) {
            return formula.pickUnassignedLiteral(state.get(level));
        } else if (attemptedList.size() >= 2) {
            return null;
        }
        return attemptedList.get(0).getInverse();
    }

    @Override
    protected Assignment propagateUnit(Literal literal) {
        state.get(level).addAttempt(literal);
        Assignment assignment = new Assignment(state.get(level));
        assignment.assignValue(literal.getName(), literal.getSign());
        return assignment;
    }

    @Override
    protected Clauses analyzeConflict(Assignment propagatedState) {
        return null;
    }

    @Override
    protected Integer backtrack(Clauses conflicts) {
        return level == 0 ? level : level - 1;
    }

}
