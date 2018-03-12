package DataStructures;

public class Edge<T> {
    protected Node<T> source;
    protected Node<T> destination;
    protected double weight;
    protected Clause antecedent;

    public Edge(Node<T> source, Node<T> destination, Clause antecedent) {
        this.source = source;
        this.destination = destination;
        this.antecedent = antecedent;
        this.weight = 0;
    }

    public Edge(Node<T> source, Node<T> destination, Clause antecedent, double weight) {
        this(source, destination, antecedent);
        this.weight = weight;
    }

    public Node<T> getSource() {
        return source;
    }

    public Node<T> getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public Clause getAntecedent() {
        return antecedent;
    }

    public String toString() {
        return source.toString() + " --" + antecedent.toString() + "-> " + destination.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof Edge)) {
            return false;
        }
        Edge otherEdge = (Edge) other;
        return weight == otherEdge.weight
                && source.equals(otherEdge.source)
                && destination.equals(otherEdge.destination)
                && antecedent.equals(otherEdge.antecedent);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
