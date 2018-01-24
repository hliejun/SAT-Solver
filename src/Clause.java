import java.util.ArrayList;

public class Clause {
	private ArrayList<Literal> literals;
	
	public Clause() {
		literals = new ArrayList<Literal>();
	}
	
	public void add(Literal l) {
		literals.add(l);
	}
	
	public void remove(Literal l) {
		literals.remove(l);
	}
	
	public ArrayList<Literal> getLiterals() {
		return literals;
	}
	
	public String toString() {
		String output = "(";
		for (int i = 0; i < literals.size() - 1; i ++) {
			output += literals.get(i) + " V ";
		}
		output += literals.get(literals.size() - 1);
		output += ")";
		return output;
	}
}
