package DataStructures;

import java.util.*;
import java.util.stream.IntStream;

public class IGraph {
    private Graph<Variable> graph;
    private HashMap<String, Node<Variable>> values;
    private HashMap<Integer, HashSet<Node<Variable>>> decisions;
    private HashSet<String> variables;
    private Clause conflictClause;
    private Node<Variable> conflictNode;

    // TODO: Save conflict as substitution to previous decisions

    public IGraph(HashSet<String> variables) {
        this.variables = variables;
        graph = new Graph<>();
        values = new HashMap<>();
        decisions = new HashMap<>();
        conflictClause = null;
        conflictNode = null;
    }

    public void addDecision(Variable decision, int decisionLevel) {
        int inferenceLevel = decision.getLevel();
        decision.setLevel(decisionLevel);
        Node<Variable> decisionNode = new Node<>(decision);
        assign(decisionNode);
        decisions.computeIfAbsent(inferenceLevel, key -> new HashSet<>()).add(decisionNode);
    }

    public void addImplication(Clause antecedent, Variable impliedVariable) {
        HashSet<Literal> literals = antecedent.getLiterals();
        Node<Variable> impliedNode = new Node<>(impliedVariable);
        assign(impliedNode);
        literals.forEach(literal -> {
            Node<Variable> antecedentNode = values.get(literal.getName());
            if (antecedentNode != null
                    && !literal.getName().equals(impliedVariable.getSymbol())
                    && graph.containsNode(antecedentNode)) {
                graph.addEdge(new Edge<>(antecedentNode, impliedNode, antecedent));
            }
        });
    }

    public void addConflict(Variable conflictVariable, Clause conflictClause) {
        Node<Variable> conflictNode = values.get(conflictVariable.getSymbol());
        this.conflictClause = conflictClause;
        this.conflictNode = conflictNode;
    }

    public void revertState(int level) {
        graph.getNodes().forEach(node -> {
            Variable assignedVariable = node.value;
            if(assignedVariable.getLevel() > level) {
                graph.removeNode(node);
                values.remove(assignedVariable.getSymbol());
                int lastLevel = decisions.size() - 1;
                if (lastLevel > level) {
                    IntStream.range(level + 1, lastLevel).forEachOrdered(oldLevel -> decisions.remove(oldLevel));
                }
            }
        });
        conflictClause = null;
        conflictNode = null;
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
            Node<Variable> node = values.get(literal.getName());
            Integer assignedLevel = node == null ? null : node.value.getLevel();
            if (assignedLevel != null && assignedLevel == level) {
                System.out.println(node);
                count += 1;
            }
        }
        return count == 1;
    }

    public Variable getImpliedVariable(Clause clause, int level) {
        HashSet<Literal> literals = clause.getLiterals();
        Variable impliedVariable = null;
        for (Literal literal : literals) {
            Node<Variable> node = values.get(literal.getName());
            if (node == null && impliedVariable != null) {
                return null;
            } else if (node == null) {
                impliedVariable = literal.toVariable(level);
            } else if (literal.evaluate(node.value.getValue())) {
                return null;
            }
        }
        return impliedVariable;
    }

    public Integer getHighestLevel(Clause clause, int conflictLevel) {
        // TODO: Change to highest level of false assigned variables
        System.out.println("LEARNT CLAUSE: " + clause);
        int highestLevel = -1;
        HashSet<Literal> literals = clause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> literalNode = values.get(literal.getName());
            Integer nodeLevel = literalNode == null ? null : literalNode.value.getLevel();
            if (nodeLevel != null && nodeLevel > highestLevel && nodeLevel < conflictLevel) {
                highestLevel = nodeLevel;
            }
        }
        return highestLevel < 0 ? null : highestLevel;
    }

    public HashSet<Node<Variable>> getPreviousDecisions(int level) {
        return decisions.get(level);
    }

    public Variable pickUnassignedVariable(int level) {
        // TODO: Remove decision checking here
        HashSet<String> unassignedSymbols = new HashSet<>();
        variables.forEach(symbol -> {
            if (values.get(symbol) == null) {
                unassignedSymbols.add(symbol);
            }
        });
        HashSet<Node<Variable>> previousDecisions = decisions.get(level);
        for (String symbol : unassignedSymbols) {
            Node<Variable> positiveAssignment = getNode(symbol, true, level);
            Node<Variable> negativeAssignment = getNode(symbol, false, level);
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

    public Clause getAntecedentClause(Clause learntClause) {
        HashSet<Literal> literals = learntClause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> node = values.get(literal.getName());
            HashSet<Edge<Variable>> antecedentEdges = graph.getEdgesToNode(node);
            for (Edge<Variable> edge : antecedentEdges) {
                Clause antecedentClause = edge.getAntecedent();
                if (antecedentClause != null) {
                    return antecedentClause;
                }
            }
        }
        return null;
    }

    public HashMap<String, Boolean> getAssignment() {
        HashMap<String, Boolean> assignment = new HashMap<>();
        values.forEach((key, node) -> assignment.put(key, node.value.getValue()));
        return assignment;
    }

    public boolean evaluate(Clauses formula) {
        HashSet<Clause> clauses = formula.getClausesSet();
        for (Clause clause : clauses) {
            HashSet<Literal> literals = clause.getLiterals();
            boolean isClauseSatisfied = false;
            for (Literal literal : literals) {
                Node<Variable> node = values.get(literal.getName());
                if (node != null && literal.evaluate(node.value.getValue())) {
                    isClauseSatisfied = true;
                }
            }
            if (!isClauseSatisfied) {
                return false;
            }
        }
        return true;
    }

    private void assign(Node<Variable> node) {
        Variable assignment = node.value;
        graph.addNode(node);
        values.put(assignment.getSymbol(), node);
    }

    private Node<Variable> getNode(String symbol, boolean value, int level) {
        Variable variable = new Variable(symbol, value, level);
        return new Node<>(variable);
    }

}
