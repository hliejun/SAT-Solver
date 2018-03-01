package DataStructures;

import java.util.*;
import java.util.stream.Collectors;

public class Graph<T> {
    protected boolean isDirected;
    protected HashMap<Node<T>, HashSet<Edge<T>>> adjacencyList;

    public Graph(boolean isDirected) {
        this.adjacencyList = new HashMap<>();
        this.isDirected = isDirected;
    }

    public void addNode(Node<T> node) {
        if (adjacencyList.get(node) != null) {
            return;
        }
        adjacencyList.put(node, new HashSet<>());
    }

    public void removeNode(Node<T> node) {
        if (adjacencyList.get(node) == null) {
            return;
        }
        getEdgesFromNode(node).forEach(this::removeEdge);
        getEdgesToNode(node).forEach(this::removeEdge);
        adjacencyList.remove(node);
    }

    public boolean containsNode(Node<T> node) {
        return adjacencyList.get(node) != null;
    }

    public void addEdge(Edge<T> edge) {
        if (!containsNode(edge.source)) {
            addNode(edge.source);
        }
        if (!containsNode(edge.destination)) {
            addNode(edge.destination);
        }
        HashSet<Edge<T>> edges = adjacencyList.get(edge.source);
        edges.add(edge);
    }

    public void removeEdge(Edge<T> edge) {
        Node<T> source = edge.source;
        HashSet<Edge<T>> edges = adjacencyList.get(source);
        if (edges == null) {
            return;
        }
        edges.remove(edge);
    }

    public boolean containsEdge(Edge<T> edge) {
        Node<T> source = edge.getSource();
        HashSet<Edge<T>> edges = adjacencyList.get(source);
        return edges != null && edges.contains(edge);
    }

    public HashSet<Edge<T>> getEdgesFromNode(Node<T> node) {
        HashSet<Edge<T>> edges = adjacencyList.get(node);
        return edges == null ? new HashSet<>() : edges;
    }

    public HashSet<Edge<T>> getEdgesToNode(Node<T> target) {
        HashSet<Edge<T>> incomingEdges = new HashSet<>();
        adjacencyList.values().forEach(edges -> incomingEdges.addAll(edges.stream().filter(
                edge -> edge.destination.equals(target)
        ).collect(Collectors.toList())));
        return incomingEdges;
    }

    public HashSet<Edge<T>> getEdgesBetweenNodes(Node<T> firstNode, Node<T> secondNode) {
        HashSet<Edge<T>> edgesFromFirstNode = adjacencyList.get(firstNode);
        HashSet<Edge<T>> edgesFromSecondNode = adjacencyList.get(secondNode);
        HashSet<Edge<T>> edges = new HashSet<>();

        edges.addAll(edgesFromFirstNode.stream().filter(
                edge -> edge.destination.equals(secondNode)
        ).collect(Collectors.toList()));

        edges.addAll(edgesFromSecondNode.stream().filter(
                edge -> edge.destination.equals(firstNode)
        ).collect(Collectors.toList()));

        return edges;
    }

    public HashSet<Node<T>> getAdjacentNodes(Node<T> node) {
        HashSet<Edge<T>> edges = getEdgesFromNode(node);
        HashSet<Node<T>> adjacentNodes = new HashSet<>();
        edges.forEach(edge -> adjacentNodes.add(edge.destination));
        return adjacentNodes;
    }

    public HashSet<Node<T>> getNodes() {
        HashSet<Node<T>> nodes = new HashSet<>();
        nodes.addAll(adjacencyList.keySet());
        return nodes;
    }

    public HashSet<Edge<T>> getEdges() {
        HashSet<Edge<T>> edges = new HashSet<>();
        adjacencyList.values().forEach(edges::addAll);
        return edges;
    }

}
