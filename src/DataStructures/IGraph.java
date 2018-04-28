package DataStructures;

import java.util.*;

// TODO: Optimise...

public class IGraph {
    private Graph<Variable> graph;
    private HashMap<String, Node<Variable>> values;
    private HashSet<String> variables;
    private Clause conflictClause;
    private Node<Variable> conflictNode;

    public IGraph(HashSet<String> variables) {
        this.variables = variables;
        graph = new Graph<>();
        values = new HashMap<>();
        conflictClause = null;
        conflictNode = null;
    }

    public void addDecision(Variable decision, int decisionLevel) {
        decision.setLevel(decisionLevel);
        System.out.println("Adding decision to level " + decision.getLevel() + " : " + decision);
        Node<Variable> decisionNode = new Node<>(decision);
        assign(decisionNode);
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
            if (assignedVariable.getLevel() > level) {
                graph.removeNode(node);
                values.remove(assignedVariable.getSymbol());
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

    public boolean isConflicted() {
        return conflictClause != null;
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
        System.out.println("Learnt clause: " + clause);
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

    public Variable pickNextUnassignedVariable(int level) {
        for (String symbol : variables) {
            if (values.get(symbol) == null) {
                Node<Variable> positiveAssignment = getNode(symbol, true, level);
                return positiveAssignment.getValue();
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

    // TODO: Check this...
    public Clause getNextAntecedentClause(Clause learntClause, Integer level) {
        if (level == null) {
            return null;
        }
        HashSet<Literal> literals = learntClause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> node = values.get(literal.getName());
            if (node.getValue().getLevel() != level) {
                continue;
            }
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
