
public class Utilities {
	public static String convertToString(int i) {
		return Integer.toString(i);
	}
	
	public static int convertToInt(String s) {
		int result = 0;
		try {
			result = Integer.parseInt(s);
			if (s.equals("0")) {
				throw new NumberFormatException("Invalid literal detected : " + s);
			}
		} catch (NumberFormatException nfe) {}
		return result;
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
