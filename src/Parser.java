import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Parser {
	private String fileName;
	private BufferedReader reader;
	private int numOfClauses;
	private int numOfLiterals;
	
	public Parser(String fileName) {
		this.fileName = fileName;
	}
	
	public Clauses parse(){
		Clauses clauses = new Clauses();
		readToProblemStmt();
		for (int i = 0; i < numOfClauses; i ++) {
			try {
				String line = reader.readLine();
				while(line != null) {
					clauses.addClause(Utilities.splitBySpace(line));
					line = reader.readLine();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
		}
		
		return clauses;
	}
	
	private void readToProblemStmt() {
		try {
			File file = new File(fileName);
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null && Utilities.isCommentLine(line)) {
				line = reader.readLine();
			}
			init(line);
		} catch (FileNotFoundException fnfe) {
			System.out.println("File does not exist");
			System.exit(0);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			System.exit(0);
		}
	}
	
	private void init(String line) {
		String[] input = Utilities.splitBySpace(line);
		numOfLiterals = Utilities.convertToInt(input[input.length - 2]);
		numOfClauses = Utilities.convertToInt(input[input.length - 1]);
	}
	
	public int getNumOfClauses() {
		return numOfClauses;
	}
	
	public int getNumOfLiterals() {
		return numOfLiterals;
	}
}
