
public class Solver {
	public static void main(String [] args) {
		Parser parser = new Parser(args[0]);
		Clauses clauses = parser.parse();
		System.out.println(clauses.toString());
	}
}
