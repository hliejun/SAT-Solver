package DataStructures;

public class Node<T> {
    protected int identifier;
    protected T value;

    public Node(T value) {
        identifier = value.hashCode();
        this.value = value;
    }

    public Node(int identifier, T value) {
        this.identifier = identifier;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public String toString() {
        return "N( " + value.toString() +  " )";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof Node)) {
            return false;
        }
        Node otherNode = (Node) other;
        return identifier == otherNode.identifier && value.equals(otherNode.value);
    }

    @Override
    public int hashCode() {
        return identifier;
    }

}
