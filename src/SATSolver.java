import java.util.HashSet;

import Solvers.*;
import DataStructures.*;

public class SATSolver {

    public static void main(String[] args) {
        String path = "./test/testcases/input1.cnf";
        if (args.length != 0) {
            path = args[0];
        }
        Parser parser = new Parser(path);
        Clauses clauses = parser.getParsedClauses();
        System.out.println(clauses.toString());

        // TODO: Pick strategy(by args)...
        Strategy strategy = Strategy.DPLL;
        Solver solver = null;

        // TODO: Initialize solver...
        switch(strategy) {
            case DPLL:
                break;
            case CDCL:
                break;
            default:
                break;
        }

        // TODO: Solve and print result...
        if (solver != null) {
            // Solver
        }
    }

}