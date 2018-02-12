import java.util.HashMap;

import Solvers.*;
import DataStructures.*;

public class SATSolver {

    public static void main(String[] args) {
        Solver solver = null;
        String path = "./test/testcases/input1.cnf";
        if (args.length != 0) {
            path = args[0];
        }
        Parser parser = new Parser(path);
        Clauses clauses = parser.getParsedClauses();

        // TODO: Pick strategy(by args)...
        Strategy strategy = Strategy.DPLL;

        // TODO: Initialize solver...
        switch(strategy) {
            case DPLL:
                solver = new DPLLSolver(clauses);
                break;
            case CDCL:
                solver = new CDCLSolver(clauses);
                break;
            default:
                break;
        }

        // TODO: Solve and print result...
        if (solver != null) {
            HashMap<String, Boolean> results = solver.solve();
            String output = results == null ? "UNSAT" : results.toString();
            System.out.println(output);
        } else {
            System.out.println("Unsupported strategy.");
        }
    }

}