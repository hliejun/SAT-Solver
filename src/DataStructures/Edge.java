package DataStructures;

public class Edge<T> {
    protected Node<T> source;
    protected Node<T> destination;
    protected double weight;

    public Edge(T source, T destination) {
        this.source = new Node<T>(source);
        this.destination = new Node<T>(destination);
        this.weight = 0;
    }

    public Edge(T source, T destination, double weight) {
        this(source, destination);
        this.weight = weight;
    }

    public T getSource() {
        return source.getValue();
    }

    public T getDestination() {
        return destination.getValue();
    }

    public double getWeight() {
        return weight;
    }

    public String toString() {
        return source.toString() + " -> " + destination.toString();
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
                && destination.equals(otherEdge.destination);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
