package DataStructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Utilities {

    public static String[] tokenizeAndRemoveZero(String input) {
        return tokenizeAndRemoveZero(input, "\\s+");
    }

    public static String[] tokenizeAndRemoveZero(String input, String token) {
        String[] output = input.trim().split(token);
        if (output.length != 0 && output[output.length - 1].equals("0")) {
            output = Arrays.copyOfRange(output, 0, output.length - 1);
        }
        return output;
    }

    public static String removeSignsAndSpaces(String input) {
        return input.replaceAll("[\\s+-]+", "").toUpperCase();
    }

    public static boolean isComment(String line) {
        return line.charAt(0) == 'c';
    }

    public static boolean isPositive(String literal) {
        return literal.indexOf("-") == -1;
    }

    public static String implode(Collection collection, String separator) {
        return collection.stream().map(Object::toString).collect(Collectors.joining(separator)).toString();
    }

}