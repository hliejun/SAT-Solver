package DataStructures;

import java.util.*;

public class IGraph {
    private Graph<Literal> graph;
    private HashMap<Clause, HashSet<Edge<Literal>>> labelSet;

    public IGraph() {
        graph = new Graph<>();
        labelSet = new HashMap<>();
    }

    public void addDecision(Literal decision) {
        graph.addNode(new Node<>(decision));
    }

    public void addImplications(Clauses clauses) {
        clauses.getClausesSet().forEach(this::addImplication);
    }

    private void addImplication(Clause clause) {
        HashSet<Node<Literal>> falseLiterals = new HashSet<>();
        ArrayList<Node<Literal>> leftLiterals = new ArrayList<>();
        clause.getLiterals().forEach(literal -> {
            Node<Literal> inversedNode = new Node<>(literal.getInverse());
            if (graph.getNodes().contains(inversedNode)) {
                falseLiterals.add(inversedNode);
            } else {
                leftLiterals.add(new Node<>(literal));
            }
        });
        if (falseLiterals.isEmpty() || leftLiterals.size() != 1) {
            return;
        }
        Node<Literal> exception = leftLiterals.get(0);
        graph.addNode(exception);
        falseLiterals.forEach(node -> {
            Edge<Literal> implication = new Edge<>(node, exception);
            graph.addEdge(implication);
            labelSet.computeIfAbsent(clause, key -> new HashSet<>()).add(implication);
        });
    }

    public HashMap<Literal, HashSet<String>> getConflicts() {
        HashMap<Literal, HashSet<String>> conflicts = new HashMap<>();
        graph.adjacencyList.keySet().forEach(node -> {
            HashMap<String, HashSet<Literal>> incidentVariables = new HashMap<>();
            HashSet<Edge<Literal>> edges = graph.getEdgesToNode(node);
            edges.forEach(edge -> {
                Literal source = edge.source.value;
                incidentVariables.computeIfAbsent(source.getName(), key -> new HashSet<>()).add(source);
            });
            incidentVariables.forEach((key, value) -> {
                if (value.size() >= 2) {
                    conflicts.computeIfAbsent(node.getValue(), variable -> new HashSet<>()).add(key);
                }
            });
        });
        return conflicts;
    }

    // TODO: Get conflict cut by scheme
    // TODO: Bounded learning (discard obsolete learnt clauses)
    // TODO: Getters
    // TODO: Copy

}
