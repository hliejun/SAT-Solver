import DataStructures.Clauses;
import DataStructures.Utilities;

import Solvers.CDCL.BuggySolver;
import Solvers.Solver;
import Solvers.CDCL.ChaffSolver;
import Solvers.CDCL.VSIDSSolver;
import Solvers.DPLL.DPLLSolver;
import Solvers.DPLL.RDPLLSolver;

import java.util.*;

public class SATSolver {

    public static void main(String[] args) {
        Solver solver = null;
        String path = "./test/testcases/unsat/2.cnf";
        if (args.length != 0) {
            path = args[0];
        }
        Parser parser = new Parser(path);
        Clauses clauses = parser.getParsedClauses();
        int literalsCount = parser.getNumOfLiterals();
        System.out.println(clauses.literalCount);
        System.out.println(clauses);

//        Strategy strategy = Strategy.VSIDS_CDCL;
        Strategy strategy = Strategy.Chaff_CDCL;
//        Strategy strategy = Strategy.Buggy_CDCL;
//        Strategy strategy = Strategy.Iterative_DPLL;
//        Strategy strategy = Strategy.Recursive_DPLL;

        long startTime = System.currentTimeMillis();

        switch(strategy) {
            case Recursive_DPLL:
                solver = new RDPLLSolver(clauses, literalsCount);
                break;
            case Iterative_DPLL:
                solver = new DPLLSolver(clauses, literalsCount);
                break;
            case Buggy_CDCL:
                solver = new BuggySolver(clauses, literalsCount);
                break;
            case Chaff_CDCL:
                solver = new ChaffSolver(clauses, literalsCount);
                break;
            case VSIDS_CDCL:
                solver = new VSIDSSolver(clauses, literalsCount);
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