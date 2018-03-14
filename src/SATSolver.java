import java.util.*;

import DataStructures.*;
import Solvers.*;

public class SATSolver {

    public static void main(String[] args) {
        Solver solver = null;
        String path = "./test/testcases/sat/1.cnf";
        if (args.length != 0) {
            path = args[0];
        }
        Parser parser = new Parser(path);
        Clauses clauses = parser.getParsedClauses();
        int literalsCount = parser.getNumOfLiterals();

        System.out.println(clauses);

        Strategy strategy = Strategy.CDCL;
//        Strategy strategy = Strategy.DPLL;
//        Strategy strategy = Strategy.RDPLL;

        long startTime = System.currentTimeMillis();

        switch(strategy) {
            case RDPLL:
                solver = new RDPLLSolver(clauses, literalsCount);
                break;
            case DPLL:
                solver = new DPLLSolver(clauses, literalsCount);
                break;
            case CDCL:
                solver = new CDCLSolver(clauses, literalsCount);
                break;
            default:
                break;

        }

        if (solver != null) {
            HashMap<String, Boolean> results = solver.solve();
            String output = results == null ? "UNSAT" : Utilities.getOutputFromMap(results);
            System.out.println(output);
        } else {
            System.out.println("Unsupported strategy.");
        }

        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 1000.0;
        System.out.println("Execution Time: " + elapsedTime + " seconds");
    }

}