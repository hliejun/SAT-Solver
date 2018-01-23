import java.util.ArrayList;

public class Clauses {
	private ArrayList<Clause> clauses;;
	
	public Clauses() {
		clauses = new ArrayList<Clause>();
	}
	
	public void addClause(String[] lits) {
		Clause clause = new Clause();
		for (int i = 0; i < lits.length - 1; i ++) {
			String variable = lits[i];
			Literal literal;
			if(Utilities.isLitPos(variable)) {
				literal = new Literal(variable, true);
			} else {
				literal = new Literal(variable, false);
			}
			clause.add(literal);
		}
		clauses.add(clause);
	}
	
	public String toString() {
		String output = "";
		for (int i = 0; i < clauses.size() - 1; i ++) {
			output += clauses.get(i) + "^";
		}
		output += clauses.get(clauses.size() - 1);
		return output;
	}
	
}
