package DataStructures;

import java.util.HashMap;

public class Assignment {
    private static final int T = 1;
    private static final int F = -1;
    private static final int U = 0;

    private final int literalsCount;
    private final int[] assignments;

    public Assignment(int literalsCount) {
        this.literalsCount = literalsCount;
        this.assignments = new int[literalsCount];
    }

    public boolean isTrue(String literal) {
        return assignments[Integer.parseInt(literal) - 1] == T;
    }

    public boolean isFalse(String literal) {
        return assignments[Integer.parseInt(literal) - 1] == F;
    }

    public boolean isUnassigned(String literal) {
        return assignments[Integer.parseInt(literal) - 1] == U;
    }

    public void assign(Literal literal, boolean literalTruth) {
        int lit = literal.getLiteralInteger();
        if (literalTruth) {
            assignments[lit - 1] = T;
        } else {
            assignments[lit - 1] = F;
        }
    }

    public Boolean getAssignValue(int idx) {
        if (assignments[idx] == T) {
            return true;
        } else if (assignments[idx] == F) {
            return false;
        }
        return null;
    }

    public HashMap<String, Boolean> getAssignValues() {
        HashMap<String, Boolean> results = new HashMap<String, Boolean>();
        for (int idx = 0; idx < assignments.length; idx ++) {
            results.put(Integer.toString(idx + 1), getAssignValue(idx));
        }
        return results;
    }

}
