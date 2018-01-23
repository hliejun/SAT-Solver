import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Parser {
	Path path;
	BufferedReader reader;
	int numOfClauses;
	int numOfLiterals;
	public Parser(String fileName) {
		path = Paths.get(fileName);
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
		try (BufferedReader br = Files.newBufferedReader(path,
	            StandardCharsets.US_ASCII)) {
			String line = br.readLine();
			while (line != null && Utilities.isCommentLine(line)) {
				line = br.readLine();
			}
			init(line);
			reader = br;
		} catch (FileNotFoundException fnfe) {
			System.out.println("File does not exist");
			System.exit(0);
		} catch (IOException ioe) {
			System.out.print(ioe.getMessage());
			System.exit(0);
		}
	}
	
	private void init(String line) {
		String[] input = Utilities.splitBySpace(line);
		numOfLiterals = Utilities.convertToInt(input[input.length - 2]);
		numOfClauses = Utilities.convertToInt(input[input.length - 1]);
	}
}
