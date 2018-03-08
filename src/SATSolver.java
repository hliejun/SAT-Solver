import DataStructures.Clauses;
import DataStructures.Utilities;
import Solvers.CDCLSolver;
import Solvers.DPLLSolver;
import Solvers.RDPLLSolver;
import Solvers.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class SATSolver {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        String path = "./test/testcases/benchmark";
        if (args.length != 0) {
            path = args[0];
        }

        File folder = new File(path);
        File[] listOfFolders = folder.listFiles();
        for (int i = 0; i < listOfFolders.length; i++) {
            System.out.println("Testing folder : " + listOfFolders[i].getName());
            System.out.println("************************************************************");
            System.out.println();
            File[] listOfFiles = listOfFolders[i].listFiles();
            PrintWriter writer = new PrintWriter(listOfFolders[i].getName() + "-output.txt", "UTF-8");
            for (int j = 0; j < listOfFiles.length; j++) {
                String currentFile = (j + 1) + ".cnf";
                System.out.println("Testing file : " + currentFile);
                String filename = path + "/" + listOfFolders[i].getName() + "/" + currentFile;
                writer.println("Testing file : " + filename);
                writer.println("=================================================");
                solve(filename, writer);
                writer.println();
            }
            writer.close();
            System.out.println("Testing Completed!");
            break;
        }

    }

    public static void solve(String file, PrintWriter writer) {
        Solver solver = null;
        long startTime = System.currentTimeMillis();
        Parser parser = new Parser(file);
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
            endTimer(startTime, writer);
            writer.println(output);
        } else {
            System.out.println("Unsupported strategy.");
        }
    }

    public static void endTimer(long startTime, PrintWriter writer) {
        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 1000.0;
        writer.println("Execution Time: " + elapsedTime + " seconds");
    }

}