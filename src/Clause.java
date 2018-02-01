import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Clause implements Comparable<Clause> {
    private HashSet<Literal> literals;

    public Clause() {
        literals = new HashSet<Literal>();
    }

    public Clause(ArrayList<Literal> listOfLiterals) {
        literals = new HashSet<Literal>();
        listOfLiterals.forEach(literal -> addLiteral(literal));
    }

    public Clause(String[] literalsString) {
        literals = new HashSet<Literal>();
        for (String literalCharacter : literalsString) {
            literals.add(new Literal(literalCharacter));
        }
    }

    public void addLiteral(Literal literal) {
        literals.add(literal);
    }

    public void removeLiteral(Literal literal) {
        literals.remove(literal);
    }

    public HashSet<Literal> getLiteralsSet() {
        return literals;
    }

    public ArrayList<Literal> toArray() {
        ArrayList<Literal> listOfLiterals = new ArrayList<Literal>(literals);
        Collections.sort(listOfLiterals);
        return listOfLiterals;
    }

    public String toString() {
        return String.format("(%s)", Utilities.implode(toArray(), " V "));
    }

    @Override
    public int compareTo(Clause otherClause) {
        if (literals.equals(otherClause.literals)) {
            return 0;
        } else if (literals.size() != otherClause.literals.size()) {
            return literals.size() <= otherClause.literals.size() ? -1 : 1;
        } else {
            ArrayList<Literal> literalsList = toArray();
            ArrayList<Literal> otherLiteralsList = otherClause.toArray();
            for (int index = 0; index < literalsList.size(); index++) {
                int order = literalsList.get(index).compareTo(otherLiteralsList.get(index));
                if (order != 0) {
                    return order;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) {
            return true;
        } else if (!(otherObject instanceof Clause)) {
            return false;
        } else {
            return this.literals.equals(((Clause) otherObject).literals);
        }
    }
}