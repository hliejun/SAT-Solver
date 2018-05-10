package Solvers.RESOLUTION;

import DataStructures.Assignment;
import DataStructures.Clause;
import DataStructures.Clauses;
import DataStructures.Literal;
import Solvers.Solver;

import java.util.*;



public class PLResolution extends Solver {

    public PLResolution(Clauses clauses, int literalsCount) {
        super(clauses, literalsCount);
    }

    private boolean isProved(Clauses resolvents) {
        for (Clause c : resolvents.getClausesSet()) {
            if (c.getLiterals().size() == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean resolve() {

        Set<Clause> clauses = formula.getClausesSet();
        Clauses newClauses = new Clauses();
        do {
            System.out.println("Current Clauses : " + clauses);
            List<Clause> clausesAsList = new ArrayList<>(clauses);
            for (int i = 0; i < clausesAsList.size() - 1; i++) {
                Clause ci = clausesAsList.get(i);
                for (int j = i + 1; j < clausesAsList.size(); j++) {
                    Clause cj = clausesAsList.get(j);
                    Clauses resolvents = plResolve(ci, cj);
                    if (isProved(resolvents)) {
                        return true;
                    }
                    newClauses.addAllClauses(resolvents);
                }
            }
            if (clauses.containsAll(newClauses.getClausesSet())) {
                return false;
            }

            clauses.addAll(newClauses.getClausesSet());

        } while (true);
    }

    public Clauses plResolve(Clause ci, Clause cj) {
        Clauses resolvents = new Clauses();
        resolvePositiveWithNegative(ci, cj, resolvents);
        return resolvents;
    }

    private ArrayList<Literal> getConflictedLiterals(Clause c1, Clause c2) {
        ArrayList<Literal> conflictedLiterals = new ArrayList<>();
        HashMap<String, ArrayList<Literal>> clauseMap = new HashMap<>();
        HashSet<Literal> combinedLiterals = new HashSet<>();

        combinedLiterals.addAll(c1.getLiterals());
        combinedLiterals.addAll(c2.getLiterals());

        for (Literal literal : combinedLiterals) {
            clauseMap.computeIfAbsent(literal.getName(), key -> new ArrayList<>()).add(literal);
        }
        clauseMap.forEach((symbol, literalSet) -> {
            if (literalSet.size() > 1) {
                conflictedLiterals.add(literalSet.get(0));
            }
        });

        return conflictedLiterals;
    }

    protected void resolvePositiveWithNegative(Clause c1, Clause c2,
                                               Clauses resolvents) {
        // Calculate the complementary positive literals from c1 with
        // the negative literals from c2
        ArrayList<Literal> conflictedLiterals = getConflictedLiterals(c1, c2);
        // Construct a resolvent clause for each complement found
        for (Literal literal : conflictedLiterals) {
            Clause resolvent = getResolventClause(c1, c2, literal);

            while (resolvent.isTautology()) {
                resolvent = getResolventClause(resolvent, resolvent, resolvent.getConflictLiteral());
            }

            resolvents.addClause(resolvent);
        }

        if (resolvents.getClausesSet().size() != 0) {
            System.out.println("Resolving: " + c1 + " with " + c2);
            System.out.println(" ... into: " + resolvents);
        }
    }

    private Clause getResolventClause(Clause c1, Clause c2, Literal literal) {
        ArrayList<Literal> resolventLiterals = new ArrayList<>();
        for (Literal c1l : c1.getLiterals()) {
            if (!c1l.getName().equals(literal.getName())) {
                resolventLiterals.add(c1l);
            }
        }
        for (Literal c2l : c2.getLiterals()) {
            if (!c2l.getName().equals(literal.getName())) {
                resolventLiterals.add(c2l);
            }
        }
        // Construct the resolvent clause
        return new Clause(resolventLiterals);
    }

    @Override
    public HashMap<String, Boolean> solve() {
        resolve();
        return null;
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