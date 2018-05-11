import DataStructures.*;

import Solvers.CDCL.*;
import Solvers.DPLL.*;
import Solvers.Solver;

import org.supercsv.io.*;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SATSolver {

    private final String INVALID_ARGUMENTS_MESSAGE = "Invalid arguments. Expected: java SATSolver <strategy> <path>";
    private PrintWriter writer = null;

    private final boolean IDE_ENVIRONMENT = true;
    private final boolean PRINT_FILE = false;

    private boolean isBenchmarkEnabled = false;

    private Strategy strategy = Strategy.Resolution_CDCL;
    private String strategyName = null;
    private String path = null;
    private Solver solver = null;

    public static void main(String[] args) {
        SATSolver satSolver = new SATSolver();
        satSolver.init(args);
    }

    private void init(String[] args) {
        try {
            parseArgs(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); ////
            System.exit(0);
        }

        parsePath();
    }

    private double solve() {
        long startTime = System.currentTimeMillis();

        if (solver != null) {
            HashMap<String, Boolean> results = solver.solve();
            String output = results == null ? "UNSAT" : Utilities.getOutputFromMap(results);
            System.out.println(output); ////

            if (PRINT_FILE) {
                this.writer.println(output); ////
            }
        } else {
            System.out.println("Unsupported strategy."); ////
        }

        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 1000.0;
        System.out.println("Execution Time: " + elapsedTime + " seconds"); ////

        if (PRINT_FILE) {
            this.writer.println("Execution Time: " + elapsedTime + " seconds"); ////
        }
        return elapsedTime;
    }

    private void parseArgs(String[] args) throws IllegalArgumentException {
        if (args.length == 0) {
            strategy = Strategy.Resolution_CDCL;
            strategyName = strategy.name();
            //path = IDE_ENVIRONMENT ? "./puzzle/einstein.cnf" : "../puzzle/einstein.cnf";
            path = IDE_ENVIRONMENT ? "./test/testcases/unsat/1.cnf" : "../test/testcases/unsat/1.cnf";

            //path = "./test/testcases/benchmark/125V_538C_sat/4.cnf"; // CDCL outperformed DPLL iterative here...
            //path = "./test/testcases/benchmark/250V_1065C_sat/82.cnf"; // DPLL iterative outperformed CDCL here...

            return;
        } else if (args.length > 2) {
            throw new IllegalArgumentException(INVALID_ARGUMENTS_MESSAGE);
        }

        switch(args[0]) {
            case "BENCHMARK":
                isBenchmarkEnabled = true;
                return;
            case "RDPLL":
                strategy = Strategy.Recursive_DPLL;
                break;
            case "DPLL":
                strategy = Strategy.Iterative_DPLL;
                break;
            case "CHAFF_CDCL":
                strategy = Strategy.Chaff_CDCL;
                break;
            case "TWOCLAUSES_CDCL":
                strategy = Strategy.TwoClause_CDCL;
                break;
            case "ALLCLAUSES_CDCL":
                strategy = Strategy.AllClause_CDCL;
                break;
            case "RESOLUTION_CDCL":
                strategy = Strategy.Resolution_CDCL;
        }

        strategyName = strategy.name();
        path = args[1];
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
            case Resolution_CDCL:
                solver = new ResolutionSolver(clauses, literalsCount);
                break;
            default:
                break;
        }
    }

    private void parsePath() {
        if (isBenchmarkEnabled) {
            String folderPath = IDE_ENVIRONMENT ? "./test/testcases/benchmark" : "../test/testcases/benchmark";
            File file = new File(folderPath);
            File[] benchmarkFolders = file.listFiles();
            Arrays.sort(benchmarkFolders);
            assert benchmarkFolders != null;

            for (File benchmarkFolder : benchmarkFolders) {
                try {
                    System.out.println("Testing " + benchmarkFolder.getName()); ////
                    parseFile(benchmarkFolder);
                } catch (IOException e) {
                    System.out.println(e.getMessage()); ////
                    System.exit(0);
                }
            }
        } else {
            File file = new File(path);
            if (file.exists()) {
                try {
                    parseFile(file);
                } catch (IOException e) {
                    System.out.println(e.getMessage()); ////
                    System.exit(0);
                }
            } else {
                throw new IllegalArgumentException("Invalid path");
            }
        }
    }

    private void parseFile(File file) throws IOException {
        if (isBenchmarkEnabled) {
            String csvPath = setupBenchmark(file);
            for (Strategy strategy : Strategy.values()) {
                this.strategy = strategy;
                this.strategyName = strategy.name();

                writeCSV(csvPath, file);

                Files.copy(
                        new File(file.getName() + ".csv").toPath(),
                        new File(file.getName() + "-Template.csv").toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } else {
            this.writer = new PrintWriter(file.getName() + "-" + strategyName + ".txt");
            if (file.isDirectory()) {
                int folderSize = file.listFiles().length;
                String folderPath = path;
                for (int i = 1; i <= folderSize; i ++) {
                    path = folderPath + "/" + i + ".cnf";
                    run();
                }
            } else {
                run();
            }
            this.writer.close();
        }
    }

    private String setupBenchmark(File file) throws IOException {
        File csvFile = new File(file.getName() + "-Template.csv");
        if (csvFile.createNewFile() && isBenchmarkEnabled) {
            PrintWriter csvWriter = new PrintWriter(csvFile);
            csvWriter.println("S/N");
            if (file.isDirectory()) {
                int filesCount = Objects.requireNonNull(file.listFiles()).length;
                for (int i = 1; i < filesCount + 1; i++) {
                    csvWriter.println(i);
                }
            } else {
                csvWriter.println(1);
            }
            csvWriter.close();
        }

        return csvFile.getPath();
    }

    private void writeCSV(String csvPath, File file) throws IOException {
        ICsvMapReader mapReader = null;
        ICsvMapWriter mapWriter = null;

        try {
            String outputFileName = file.getName() + ".csv";
            CsvPreference prefs = CsvPreference.STANDARD_PREFERENCE;
            mapReader = new CsvMapReader(new FileReader(csvPath), prefs);
            mapWriter = new CsvMapWriter(new FileWriter(outputFileName), prefs);

            String[] readHeader = mapReader.getHeader(true);
            if (readHeader == null) {
                readHeader = new String[0];
            }

            final String[] writeHeader = new String[readHeader.length + 1];
            System.arraycopy(readHeader, 0, writeHeader, 0, readHeader.length);
            final String strategyHeader = strategyName;
            writeHeader[writeHeader.length - 1] = strategyHeader;

            mapWriter.writeHeader(writeHeader);

            Map<String, String> row;
            int fileCounter = 1;
            this.writer = new PrintWriter(file.getName() + "-" + strategyName + ".txt");

            // FIXME: Fix toggle - solver not triggered in non-benchmark IDE mode and no output to file either

            while ((row = mapReader.read(readHeader)) != null) {
                if (file.isDirectory()) {
                    String filename = fileCounter + ".cnf";
                    this.writer.println(filename);
                    path = file.getAbsolutePath() + "/" + filename;
                    row.put(strategyHeader, String.valueOf(run()));
                    fileCounter++;
                } else {
                    row.put(strategyHeader, String.valueOf(run()));
                }
                mapWriter.write(row, writeHeader);
            }
            this.writer.close();
        } finally {
            if(mapReader != null) {
                mapReader.close();
            }
            if(mapWriter != null) {
                mapWriter.close();
            }
        }
    }

    private double run() {
        Parser parser = new Parser(path);
        System.out.println(path); ////

        Clauses clauses = parser.getParsedClauses();
        int literalsCount = parser.getNumOfLiterals();
        parseStrategy(strategy, clauses, literalsCount);

        return solve();
    }

}
