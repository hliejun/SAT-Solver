
public class Utilities {
	public static String convertToString(int i) {
		return Integer.toString(i);
	}
	
	public static int convertToInt(String s) {
		return Integer.parseInt(s);
	}
	
	public static String[] splitBySpace(String s) {
		return s.split("\\s+");
	}
	
	public static boolean isCommentLine(String line) {
		return line.charAt(0) == 'c';
	}
	
	public static boolean isLitPos(String lit) {
		return (lit.indexOf("-") == -1);
	}
}
