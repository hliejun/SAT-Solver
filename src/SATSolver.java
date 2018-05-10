import DataStructures.Clauses;
import DataStructures.Utilities;
import Solvers.CDCL.*;
import Solvers.DPLL.DPLLSolver;
import Solvers.DPLL.RDPLLSolver;
import Solvers.Solver;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SATSolver {

    private final boolean IS_BENCHMARKING = true;
    private final String INVALID_ARGUMENTS_MESSAGE = "Invalid arguments expected. java SatSolver <Strategy> <Path>";

    private Strategy strategy = Strategy.AllClause_CDCL; // Default fastest strategy
    private String strategyName = null;
    private String path = null;
    private Solver solver = null;

    public void init(String[] args) {

//        String path = "./test/testcases/unsat/5.cnf";
////        String path = "./test/testcases/benchmark/125V_538C_sat/4.cnf"; // CDCL outperformed DPLL iterative here...
////        String path = "./test/testcases/benchmark/250V_1065C_sat/82.cnf"; // DPLL iterative outperformed CDCL here...
//
////        String path = "./puzzle/einstein.cnf";
//
//        if (args.length != 0) {
//            path = args[0];
//        }

        try {
            parseArgs(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        parsePath();

        //// System.out.println(clauses.literalCount); ////
        //// System.out.println(clauses); ////

//         Strategy strategy = Strategy.Recursive_DPLL;
//         Strategy strategy = Strategy.Iterative_DPLL;

//         Strategy strategy = Strategy.Chaff_CDCL;
//         Strategy strategy = Strategy.TwoClause_CDCL;
        //Strategy strategy = Strategy.AllClause_CDCL;
        // Strategy strategy = Strategy.ERWA_CDCL;
        // Strategy strategy = Strategy.VSIDS_CDCL;
        // Strategy strategy = Strategy.Advanced_CDCL;
    }

    private double solve() {
        long startTime = System.currentTimeMillis();

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
        return elapsedTime;
    }

    public static void main(String[] args) {
        SATSolver satSolver = new SATSolver();
        satSolver.init(args);
    }

    private void parseStrategy(Strategy strategy, Clauses clauses, int literalsCount) {
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
    }

    private void parseArgs(String[] args) throws IllegalArgumentException {
        if (args.length != 2) {
            throw new IllegalArgumentException(INVALID_ARGUMENTS_MESSAGE);
        }

        if (args[0].equals("RDPLL")) {
            strategy = Strategy.Recursive_DPLL;
        } else if (args[0].equals("DPLL")) {
            strategy = Strategy.Iterative_DPLL;
        } else if (args[0].equals("CHAFF_CDCL")) {
            strategy = Strategy.Chaff_CDCL;
        } else if (args[0].equals("TWOCLAUSES_CDCL")) {
            strategy = Strategy.TwoClause_CDCL;
        } else if (args[0].equals("ALLCLAUSES_CDCL")) {
            strategy = Strategy.Chaff_CDCL;
        } else if (args[0].equals("ERWA_CDCL")) {
            strategy = Strategy.Chaff_CDCL;
        } else if (args[0].equals("VSIDS_CDCL")) {
            strategy = Strategy.Chaff_CDCL;
        } else if (args[0].equals("ADVANCED_CDCL")) {
            strategy = Strategy.Advanced_CDCL;
        }

        strategyName = args[0];
        path = args[1];
    }

    private void parsePath() {
        File file = new File(path);
        if (file.exists()) {
            try {
                parseFile(file);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
        } else {
            throw new IllegalArgumentException("Invalid path");
        }
    }

    private void parseFile(File file) throws IOException {
        File csvFile = new File(file.getName() + "-Template.csv");
        if (csvFile.createNewFile() && IS_BENCHMARKING) {
            PrintWriter writer = new PrintWriter(csvFile);
            writer.println("S/N");
            if (file.isDirectory()) {
                for (int i = 1; i < file.listFiles().length + 1; i++) {
                    writer.println(i);
                }
            } else {
                writer.println(1);
            }
            writer.close();
        }

        String csvPath = csvFile.getPath();

        if (IS_BENCHMARKING) {
            for (Strategy strategy : Strategy.values()) {
                this.strategy = strategy;
                this.strategyName = strategy.name();
                writeCSV(csvPath, file);
                if (this.strategy == Strategy.Recursive_DPLL) {
                    break;
                }
            }
        } else {
            writeCSV(csvPath, file);
        }
    }

    private void writeCSV(String csvPath, File file) throws IOException {
        ICsvMapReader mapReader = null;
        ICsvMapWriter mapWriter = null;
        try {
            String outputFileName = file.getName() + ".csv";
            File output = new File(outputFileName);
            CsvPreference prefs = CsvPreference.STANDARD_PREFERENCE;
            if (output.exists()) {
                mapReader = new CsvMapReader(new FileReader(output.getPath()), prefs);
            } else {
                mapReader = new CsvMapReader(new FileReader(csvPath), prefs);
            }
            mapWriter = new CsvMapWriter(new FileWriter(outputFileName), prefs);

            // header used to read the original file
            String[] readHeader = mapReader.getHeader(true);
            if (readHeader == null) {
                readHeader = new String[0];
            }

            // header used to write the new file
            // (same as 'readHeader', but with additional column)
            final String[] writeHeader = new String[readHeader.length + 1];
            System.arraycopy(readHeader, 0, writeHeader, 0, readHeader.length);
            final String strategyHeader = strategyName;
            writeHeader[writeHeader.length - 1] = strategyHeader;

            mapWriter.writeHeader(writeHeader);

            Map<String, String> row;
            int filecounter = 1;
            while ((row = mapReader.read(readHeader)) != null) {
                if (file.isDirectory()) {
                    String filename = filecounter + ".cnf";
                    path = file.getAbsolutePath() + "/" + filename;
                    row.put(strategyHeader, String.valueOf(run()));
                    filecounter ++;
                } else {
                    row.put(strategyHeader, String.valueOf(run()));

                }
                // add your column with desired value

                mapWriter.write(row, writeHeader);
            }

        }
        finally {
            if( mapReader != null ) {
                mapReader.close();
            }
            if( mapWriter != null ) {
                mapWriter.close();
            }
        }
    }

    private double run() throws  IOException {
        Parser parser = new Parser(path);
        System.out.println(path);
        Clauses clauses = parser.getParsedClauses();
        int literalsCount = parser.getNumOfLiterals();
        parseStrategy(strategy, clauses, literalsCount);
        return solve();
    }
}
