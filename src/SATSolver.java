import DataStructures.Clauses;
import DataStructures.Utilities;
import Solvers.*;

import java.util.*;

public class SATSolver {

    public static void main(String[] args) {
        Solver solver = null;
        String path = "./test/testcases/benchmark/250V_1065C_sat/82.cnf";
        if (args.length != 0) {
            path = args[0];
        }
        Parser parser = new Parser(path);
        Clauses clauses = parser.getParsedClauses();
        int literalsCount = parser.getNumOfLiterals();
        System.out.println(clauses.literalCount);
        System.out.println(clauses);

        Strategy strategy = Strategy.ChaffCDCL;
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
            case ChaffCDCL:
                solver = new ChaffCDCLSolver(clauses, literalsCount);
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