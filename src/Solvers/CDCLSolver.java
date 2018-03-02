package Solvers;

import java.util.ArrayList;
import java.util.HashMap;

import DataStructures.*;

public class CDCLSolver extends Solver {
    protected IGraph implications = new IGraph();

    public CDCLSolver(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resetSolver();
        while (!isSatisfied()) {
            Literal selectedLiteral = pickBranchingAssignment();
            if (selectedLiteral == null && level == 0) {
                return null;
            }
            if (formula.hasConflicts(getLevelState()) || selectedLiteral == null) {
                Clauses conflicts = analyzeConflict(getLevelState());
                level = backtrack(conflicts);
                continue;
            }
            Assignment propagatedState = propagateUnit(selectedLiteral);
            level += 1;
            state.put(level, propagatedState);
        }
        System.out.println("Assigned: " + getLevelState().getAllValues().size() + " / " + variables.size());
        System.out.println("Unassigned: " + getUnassignedVariables(getLevelState()));
        return getLevelState().getAllValues();
    }

    @Override
    protected Literal pickBranchingAssignment() {
        // TODO: Select assignment based on conflict clauses and attempted assignments
        ArrayList<Literal> attemptedList = getLevelState().getAttempts();
        if (attemptedList == null || attemptedList.isEmpty()) {
            return formula.pickUnassignedLiteral(getLevelState());
        } else if (attemptedList.size() >= 2) {
            return null;
        }
        return attemptedList.get(0).getInverse();
    }

    @Override
    protected Assignment propagateUnit(Literal literal) {
        Assignment assignment = new Assignment(getLevelState());

        implications.addDecision(literal);
        getLevelState().addAttempt(literal);
        assignment.assignValue(literal.getName(), literal.getSign());

        ArrayList<Clause> clauses = formula.toArray();
        for (Clause clause : clauses) {
            // TODO: Propagate lone clause (should lone clauses be added as decisions with implications too?)
            // TODO: Propagate dependent unit clause + capture implications
            if (!clause.evaluate(assignment) && !clause.hasConflicts(assignment) && clause.isUnitClause(assignment)) {
                Literal unitLiteral = clause.getUnitLiteral(assignment);
                assignment.assignValue(unitLiteral.getName(), unitLiteral.getSign());
            }
        }

        return assignment;
    }

    @Override
    protected Clauses analyzeConflict(Assignment assignment) {
        // TODO: Get conflict clause(s) from graph using UIP(s) schemes
        return null;
    }

    @Override
    protected Integer backtrack(Clauses conflicts) {
        // TODO: Infer level to backtrack to from conflicts and graph
        return level == 0 ? level : level - 1;
    }

}
