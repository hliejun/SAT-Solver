import DataStructures.Clauses;
import Solvers.CDCLSolver;
import Solvers.DPLLSolver;
import Solvers.RecursiveDPLLSolver;
import Solvers.Solver;

import java.util.HashMap;

public class SATSolver {

    public static void main(String[] args) {
        Solver solver = null;
        String path = "./test/testcases/unsat/3.cnf";
        if (args.length != 0) {
            path = args[0];
        }
        Parser parser = new Parser(path);
        Clauses clauses = parser.getParsedClauses();
        int literalsCount = parser.getNumOfLiterals();

        System.out.println(clauses);

        // TODO: Pick strategy(by args)...
        Strategy strategy = Strategy.RDPLL;

        // TODO: Initialize solver...
        switch(strategy) {
            case RDPLL:
                solver = new RecursiveDPLLSolver(clauses, literalsCount);
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