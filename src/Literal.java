
public class Literal {
	private int variable;
	private boolean truthValue;
	
	public Literal(String var, boolean value) {
		variable = Utilities.convertToInt(var);
		truthValue = value;
	}
	
	public void toggle() {
		variable *= -1;
		truthValue = !truthValue;
	}
	
	public boolean getValue() {
		return truthValue;
	}
	
	public int getVariable() {
		return variable;
	}

	public String toString() {
		return (truthValue) ? Utilities.convertToString(variable) 
				: "-" + Utilities.convertToString(variable);
	}
}
