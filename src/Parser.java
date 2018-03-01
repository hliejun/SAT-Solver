import java.io.*;

import DataStructures.*;

public class Parser {
    private BufferedReader reader;
    private String fileName;
    private int numOfClauses, numOfLiterals;

    public Parser(String fileName) {
        this.fileName = fileName;
        parseFile();
    }

    public Clauses getParsedClauses() {
        Clauses clauses = new Clauses();
        for (int i = 0; i < numOfClauses; i++) {
            try {
                String line = reader.readLine();
                if (line != null) {
                    clauses.addClause(Utilities.tokenizeAndRemoveZero(line));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }

        return clauses;
    }

    private void parseFile() {
        try {
            File file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null && Utilities.isComment(line)) {
                line = reader.readLine();
            }
            setNumOfElements(line);
        } catch (FileNotFoundException fnfe) {
            System.out.println("File does not exist");
            System.exit(0);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.exit(0);
        }
    }

    private void setNumOfElements(String line) {
        String[] input = Utilities.tokenizeAndRemoveZero(line);
        numOfLiterals = Integer.parseInt(input[input.length - 2]);
        numOfClauses = Integer.parseInt(input[input.length - 1]);
    }

    public int getNumOfClauses() {
        return numOfClauses;
    }

    public int getNumOfLiterals() {
        return numOfLiterals;
    }

}
