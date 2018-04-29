package DataStructures;

import java.util.*;

public class IGraph {

    private Graph<Variable> graph;

    private HashSet<String> variables;
    private HashMap<String, Node<Variable>> assignedVariables;
    private HashSet<String> unassignedVariables;

    private Clause conflictClause;
    private Node<Variable> conflictNode;

    public IGraph(HashSet<String> variables) {
        graph = new Graph<>();

        this.variables = variables;
        assignedVariables = new HashMap<>();
        unassignedVariables = new HashSet<>();
        unassignedVariables.addAll(variables);

        conflictClause = null;
        conflictNode = null;
    }

    public void addDecision(Variable decision, int decisionLevel) {
        decision.setLevel(decisionLevel);
        //// System.out.println("Adding decision to level " + decision.getLevel() + " : " + decision);

        Node<Variable> decisionNode = new Node<>(decision);
        assign(decisionNode);
    }

    public void addImplication(Clause antecedent, Variable impliedVariable) {
        Node<Variable> impliedNode = new Node<>(impliedVariable);
        assign(impliedNode);

        HashSet<Literal> literals = antecedent.getLiterals();
        literals.forEach(literal -> {
            Node<Variable> antecedentNode = assignedVariables.get(literal.getName());
            if (antecedentNode != null
                    && !literal.getName().equals(impliedVariable.getSymbol())
                    && graph.containsNode(antecedentNode)) {
                graph.addEdge(new Edge<>(antecedentNode, impliedNode, antecedent));
            }
        });
    }

    public void addConflict(Variable conflictVariable, Clause conflictClause) {
        Node<Variable> conflictNode = assignedVariables.get(conflictVariable.getSymbol());
        this.conflictClause = conflictClause;
        this.conflictNode = conflictNode;
    }

    public void revertState(int level) {
        graph.getNodes().forEach(node -> {
            Variable assignedVariable = node.value;
            if (assignedVariable.getLevel() > level) {
                graph.removeNode(node);

                String symbol = assignedVariable.getSymbol();
                assignedVariables.remove(symbol);
                unassignedVariables.add(symbol);
            }
        });

        conflictClause = null;
        conflictNode = null;
    }

    public boolean isConflicted(Clause clause) {
        HashSet<Literal> literals = clause.getLiterals();

        for (Literal literal : literals) {
            Node<Variable> node = assignedVariables.get(literal.getName());
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
        int count = 0;

        HashSet<Literal> literals = clause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> node = assignedVariables.get(literal.getName());
            Integer assignedLevel = node == null ? null : node.value.getLevel();
            if (assignedLevel != null && assignedLevel == level) {
                count += 1;
            }
        }

        return count == 1;
    }

    public Clause getConflictClause() {
        return conflictClause;
    }

    public Node<Variable> getConflictNode() {
        return conflictNode;
    }

    public HashMap<String, Boolean> getAssignment() {
        HashMap<String, Boolean> assignment = new HashMap<>();
        assignedVariables.forEach((key, node) -> assignment.put(key, node.value.getValue()));
        return assignment;
    }

    public Variable getImpliedVariable(Clause clause, int level) {
        Variable impliedVariable = null;

        HashSet<Literal> literals = clause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> node = assignedVariables.get(literal.getName());

            if (node == null && impliedVariable == null) {
                impliedVariable = literal.toVariable(level);
            }

            else if (node == null && impliedVariable != null) {
                return null;
            }

            else if (node != null && literal.evaluate(node.value.getValue())) {
                return null;
            }
        }

        return impliedVariable;
    }

    public Integer getHighestLevel(Clause clause, int conflictLevel) {
        int highestLevel = -1;

        HashSet<Literal> literals = clause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> literalNode = assignedVariables.get(literal.getName());
            Integer nodeLevel = literalNode == null ? null : literalNode.value.getLevel();

            if (nodeLevel != null && nodeLevel > highestLevel && (literals.size() == 1 || nodeLevel < conflictLevel)) {
                highestLevel = nodeLevel;
            }
        }

        return highestLevel < 0 ? null : highestLevel;
    }

    public Variable getNextRandomUnassignedVariable(int level) {
        if (unassignedVariables.isEmpty()) {
            return null;
        }

        String symbol = new ArrayList<>(unassignedVariables).get(0);
        Node<Variable> positiveAssignment = getNode(symbol, true, level);

        return positiveAssignment.getValue();
    }

    public Variable getNextBestUnassignedVariable(int level) {

        // TODO: Pick an unassigned variable with most frequent occurrences in 2-clauses (see Clauses)
        // --- Maintain a 2-clause frequency table and get highest ranked

        return null;
    }

    public Clause getNextAntecedentClause(Clause learntClause, Integer level) {
        if (level == null) {
            return null;
        }

        HashSet<Literal> literals = learntClause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> node = assignedVariables.get(literal.getName());

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

    public boolean evaluate(Clauses formula) {
        HashSet<Clause> clauses = formula.getClausesSet();
        for (Clause clause : clauses) {
            boolean isClauseSatisfied = false;

            HashSet<Literal> literals = clause.getLiterals();
            for (Literal literal : literals) {
                Node<Variable> node = assignedVariables.get(literal.getName());
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

        String symbol = assignment.getSymbol();
        assignedVariables.put(symbol, node);
        unassignedVariables.remove(symbol);
    }

    private Node<Variable> getNode(String symbol, boolean value, int level) {
        Variable variable = new Variable(symbol, value, level);
        return new Node<>(variable);
    }

}
