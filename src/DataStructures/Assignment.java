package DataStructures;

import java.util.*;

public class Assignment {
    private HashMap<String, Boolean> values;
    private ArrayList<Literal> attempts;
    private final int numOfVariables;

    public Assignment(int numOfVariables) {
        values = new HashMap<>();
        attempts = new ArrayList<>();
        this.numOfVariables = numOfVariables;
    }

    public Assignment(Assignment oldAssignment) {
        values = new HashMap<>(oldAssignment.getAllValues());
        attempts = new ArrayList<>();
        numOfVariables = oldAssignment.getNumOfVariables();
    }

    public void assignValue(String variable, boolean value) {
        values.put(variable, value);
    }

    public void addAttempt(Literal literal) {
        attempts.add(literal);
    }

    public Boolean getValue(String variable) {
        return values.get(variable);
    }

    public HashMap<String, Boolean> getAllValues() {
        return values;
    }

    public ArrayList<Literal> getAttempts() {
        return attempts;
    }

    public int getNumOfVariables() {
        return numOfVariables;
    }

    public Assignment copy() {
        Assignment newAssignment = new Assignment(this);
        return newAssignment;
    }

}