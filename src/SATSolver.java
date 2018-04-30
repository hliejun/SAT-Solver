import DataStructures.Clauses;
import DataStructures.Utilities;

import Solvers.CDCL.*;
import Solvers.Solver;

import Solvers.DPLL.DPLLSolver;
import Solvers.DPLL.RDPLLSolver;

import java.util.*;

public class SATSolver {

    public static void main(String[] args) {
        Solver solver = null;

        // TODO: Model Einstein's Puzzle as a SAT problem

        // FIXME: Could be a sign that we need something like a search restart
        String path = "./test/testcases/benchmark/125V_538C_sat/4.cnf"; // CDCL outperformed DPLL iterative here...
//        String path = "./test/testcases/benchmark/250V_1065C_sat/82.cnf"; // DPLL iterative outperformed CDCL here...
        if (args.length != 0) {
            path = args[0];
        }
        Parser parser = new Parser(path);
        Clauses clauses = parser.getParsedClauses();
        int literalsCount = parser.getNumOfLiterals();

        //// System.out.println(clauses.literalCount); ////
        //// System.out.println(clauses); ////

//         Strategy strategy = Strategy.Recursive_DPLL;
//         Strategy strategy = Strategy.Iterative_DPLL;

//         Strategy strategy = Strategy.Chaff_CDCL;
//         Strategy strategy = Strategy.TwoClause_CDCL;
        Strategy strategy = Strategy.AllClause_CDCL;
        // Strategy strategy = Strategy.ERWA_CDCL;
        // Strategy strategy = Strategy.VSIDS_CDCL;
        // Strategy strategy = Strategy.Advanced_CDCL;

        long startTime = System.currentTimeMillis();

        switch(strategy) {
            case Recursive_DPLL:
                solver = new RDPLLSolver(clauses, literalsCount);
                break;
            case Iterative_DPLL:
                solver = new DPLLSolver(clauses, literalsCount);
                break;
            case Chaff_CDCL:
                solver = new ChaffSolver(clauses, literalsCount);
                break;
            case TwoClause_CDCL:
                solver = new TwoClauseSolver(clauses, literalsCount);
                break;
            case AllClause_CDCL:
                solver = new AllClauseSolver(clauses, literalsCount);
                break;
            case ERWA_CDCL:
                solver = new ERWASolver(clauses, literalsCount);
                break;
            case VSIDS_CDCL:
                solver = new VSIDSSolver(clauses, literalsCount);
                break;
            case Advanced_CDCL:
                solver = new AdvancedSolver(clauses, literalsCount);
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
