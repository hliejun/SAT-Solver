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
    public static Strategy strategy = Strategy.DPLL; // Default
    public static String path = "../test/testcases/benchmark"; // Default

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {

        String chosenStrategy = "DPLL";
        if (args.length == 0) {
            System.out.println("Running testcases from : " + path);
            System.out.println("Running Strategy : " + chosenStrategy);
            Thread.sleep(3000);
            runBenchmarks(path, chosenStrategy);
        } else {
            path = args[0];
            if (args[1].equals("RDPLL")) {
                strategy = Strategy.RDPLL;
            } else if (args[1].equals("DPLL")) {
                strategy = Strategy.DPLL;
            } else if (args[1].equals("CDCL")) {
                strategy = Strategy.CDCL;
            }
            chosenStrategy = args[1];
            System.out.println("Running testcases from : " + path);
            System.out.println("Running Strategy : " + chosenStrategy);
            File folder = new File(path);
            Thread.sleep(3000);
            runTestcases(path, folder, folder.listFiles(), chosenStrategy);
        }
    }

    private static void runBenchmarks(String path, String chosenStrategy) throws FileNotFoundException, UnsupportedEncodingException {
        File folder = new File(path);
        File[] listOfFolders = folder.listFiles();
        if (listOfFolders == null) {
            System.out.println(path + " is an invalid path.");
            System.exit(0);
        }

        for (int i = 0; i < listOfFolders.length; i++) {
            System.out.println("Testing folder : " + listOfFolders[i].getName());
            System.out.println("************************************************************");
            System.out.println();
            File[] listOfFiles = listOfFolders[i].listFiles();
            path = path + "/" + listOfFolders[i].getName();
            runTestcases(path, listOfFolders[i], listOfFiles, chosenStrategy);
            System.out.println("Testing Completed!");
        }
    }

    private static void runTestcases(String path, File folder, File[] listOfFiles, String chosenStrategy) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(folder.getName() + "-output" + "-" + chosenStrategy + ".txt", "UTF-8");
        for (int j = 0; j < listOfFiles.length; j++) {
            String currentFile = (j + 1) + ".cnf";
            System.out.println("Testing file : " + currentFile);
            String filename = path + currentFile;
            writer.println("Testing file : " + filename);
            writer.println("=================================================");
            solve(filename, writer);
            writer.println();
        }
        writer.close();
    }

    public static void solve(String file, PrintWriter writer) {
        Solver solver = null;
        long startTime = System.currentTimeMillis();
        Parser parser = new Parser(file);
        Clauses clauses = parser.getParsedClauses();
        int literalsCount = parser.getNumOfLiterals();

        // writer.println(clauses);

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
        System.out.println("Execution Time: " + elapsedTime + " seconds");
    }
}