public class Solver {
	public static void main(String [] args) {
	    String path = "./test/testcases/input1.cnf";
	    if (args.length != 0) {
	        path = args[0];
        }
		Parser parser = new Parser(path);
		Clauses clauses = parser.parse();
		System.out.println(clauses.toString());
	}
}