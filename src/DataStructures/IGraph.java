package DataStructures;

import java.util.*;
import java.util.stream.IntStream;

public class IGraph {
    private Graph<Variable> graph;
    private HashMap<String, Node<Variable>> values;
    private HashMap<Variable, Integer> levels;
    private HashMap<Integer, HashSet<Node<Variable>>> decisions;
    private HashSet<String> variables;
    private Clause conflictClause;
    private Node<Variable> conflictNode;

    private enum Type { CONFLICT }

    public IGraph(HashSet<String> variables) {
        this.variables = variables;
        graph = new Graph<>();
        values = new HashMap<>();
        levels = new HashMap<>();
        decisions = new HashMap<>();
        conflictClause = null;
        conflictNode = null;
    }

    public boolean addAssignment(Node<Variable> node, int level, boolean isForced) {
        Variable assignment = node.value;
        if (!isForced && graph.containsNode(node)) {
            return false;
        }
        graph.removeNode(node);
        graph.addNode(node);
        values.put(assignment.getSymbol(), node);
        levels.put(assignment, level);
        return true;
    }

    public void addDecision(Variable decision, int decisionLevel) {
        Node<Variable> decisionNode = new Node<>(decision);
        addAssignment(decisionNode, decisionLevel, true);
        decisions.computeIfAbsent(decisionLevel, key -> new HashSet<>()).add(decisionNode);
    }

    public void addImplication(Clause antecedent, Variable implication, int propagationLevel) {
        HashSet<Literal> literals = antecedent.getLiterals();
        Literal impliedLiteral = new Literal(implication.getSymbol(), implication.getValue());
        Node<Variable> impliedNode = new Node<>(implication);
        addAssignment(impliedNode, propagationLevel, false);
        literals.forEach(literal -> {
            Variable antecedentAssignment = new Variable(literal.getName(), !literal.getSign());
            Node<Variable> antecedentNode = new Node<>(antecedentAssignment);
            if (!literal.equals(impliedLiteral) && graph.containsNode(antecedentNode)) {
                graph.addEdge(new Edge<>(antecedentNode, impliedNode, antecedent));
            }
        });
    }

    public void addConflict(Clause conflictClause, int conflictLevel) {
        HashSet<Literal> literals = conflictClause.getLiterals();
        Type conflictLabel = Type.CONFLICT;
        Variable conflict = new Variable(conflictLabel.name(), true);
        Node<Variable> conflictNode = new Node<>(conflict.hashCode(), conflict);
        this.conflictClause = conflictClause;
        this.conflictNode = conflictNode;
        graph.addNode(conflictNode);
        values.put(conflictLabel.name(), conflictNode);
        levels.put(conflict, conflictLevel);
        literals.forEach(literal -> {
            Node<Variable> conflictSource = values.get(literal.getName());
            graph.addEdge(new Edge<>(conflictSource, conflictNode, conflictClause));
        });
    }

    public void revertState(int level) {
        graph.getNodes().forEach(node -> {
            Variable assignment = node.value;
            Integer assignedLevel = levels.get(assignment);
            if(assignedLevel != null && assignedLevel > level) {
                graph.removeNode(node);
                values.remove(assignment.getSymbol());
                levels.remove(assignment);
                int lastLevel = decisions.size() - 1;
                if (lastLevel > level) {
                    IntStream.range(level + 1, lastLevel).forEachOrdered(expiredLevel -> {
                        decisions.remove(expiredLevel);
                    });
                }
            }
        });
        conflictClause = null;
    }

    public boolean isConflicted(Clause clause) {
        HashSet<Literal> literals = clause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> node = values.get(literal.getName());
            if (node == null || literal.evaluate(node.value.getValue())) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllAssigned() {
        return graph.getNodes().size() == variables.size();
    }

    public boolean isAtUniqueImplicationPoint(Clause clause, int level) {
        HashSet<Literal> literals = clause.getLiterals();
        int count = 0;
        for (Literal literal : literals) {
            Integer assignmentLevel = levels.get(new Variable(literal.getName(), literal.getSign()));
            if (assignmentLevel != null && assignmentLevel == level) {
                count += 1;
            }
        }
        return count == 1;
    }

    public Variable getLoneVariable(Clause clause) {
        HashSet<Literal> literals = clause.getLiterals();
        Variable loneVariable = null;
        for (Literal literal : literals) {
            Node<Variable> node = values.get(literal.getName());
            if (node == null && loneVariable != null) {
                return null;
            } else if (node == null) {
                loneVariable = new Variable(literal.getName(), literal.getSign());
            } else if (literal.evaluate(node.value.getValue())) {
                return null;
            }
        }
        return loneVariable;
    }

    public Integer getBacktrackLevel(Clause clause, int conflictLevel) {
        int backtrackLevel = -1;
        HashSet<Literal> literals = clause.getLiterals();
        for (Literal literal : literals) {
            Node literalNode = new Node<>(new Variable(literal.getName(), literal.getSign()));
            Integer nodeLevel = getLevelOfNode(literalNode);
            if (nodeLevel != null && nodeLevel > backtrackLevel && nodeLevel < conflictLevel) {
                backtrackLevel = nodeLevel;
            }
        }
        return backtrackLevel < 0 ? null : backtrackLevel;
    }

    public Variable pickUnassignedVariable(int level) {
        HashSet<Node<Variable>> nodes = graph.getNodes();
        HashSet<String> unassignedSymbols = new HashSet<>();
        variables.forEach(symbol -> {
            if (!nodes.contains(new Node<>(new Variable(symbol, true)))) {
                unassignedSymbols.add(symbol);
            }
        });
        HashSet<Node<Variable>> previousDecisions = decisions.get(level);
        for (String symbol : unassignedSymbols) {
            Node<Variable> positiveAssignment = new Node<>(new Variable(symbol, true));
            Node<Variable> negativeAssignment = new Node<>(new Variable(symbol, false));
            if (previousDecisions == null || !previousDecisions.contains(positiveAssignment)) {
                return positiveAssignment.getValue();
            }
            if (!previousDecisions.contains(negativeAssignment)) {
                return negativeAssignment.getValue();
            }
        }
        return null;
    }

    public Clause getConflictClause() {
        return conflictClause;
    }

    public Node<Variable> getConflictNode() {
        return conflictNode;
    }

    public Integer getLevelOfNode(Node<Variable> node) {
        return levels.get(node.value);
    }

    public HashSet<Edge<Variable>> getAntecedentEdges(Node impliedNode) {
        return graph.getEdgesToNode(impliedNode);
    }

    public HashMap<String, Boolean> getAssignment() {
        HashMap<String, Boolean> assignment = new HashMap<>();
        values.forEach((key, node) -> assignment.put(key, node.value.getValue()));
        return assignment;
    }

}
