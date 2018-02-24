import DataStructures.Clauses;
import DataStructures.Utilities;
import Solvers.CDCLSolver;
import Solvers.DPLLSolver;
import Solvers.RDPLLSolver;
import Solvers.Solver;

import java.util.*;

// TODO: Pick strategy by arguments/parameters

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

//        Strategy strategy = Strategy.DPLL;
        Strategy strategy = Strategy.RDPLL;

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
    }

}