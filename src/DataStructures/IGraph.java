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

            else if (node == null) {
                return null;
            }

            else if (literal.evaluate(node.value.getValue())) {
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

    private Node<Variable> getTruthNode(String symbol, int level) {
        Variable variable = new Variable(symbol, true, level);
        return new Node<>(variable);
    }

    /**
     *  Branching Heuristics
     */

    public Variable getNextUnassignedVariable(int level) {
        return unassignedVariables.stream()
                .map(symbol -> getTruthNode(symbol, level))
                .findFirst()
                .map(Node::getValue)
                .orElse(null);
    }

    public Variable getNextRandomUnassignedVariable(int level) {

        // FIXME: Make entirely pseudo-random (with normal distribution)

        return unassignedVariables.stream()
                .map(symbol -> getTruthNode(symbol, level))
                .findAny()
                .map(Node::getValue)
                .orElse(null);
    }

    public Variable getNextTwoClauseUnassignedVariable(int level) {

        // TODO: Pick an unassigned variable with most occurrences in 2-clauses, ties broken randomly

        return null;
    }

    public Variable getNextMostUnassignedVariable(int level, HashMap<String, Integer> frequencyTable) {
        if (unassignedVariables.isEmpty()) {
            return null;
        }

        ArrayList<String> unassignedList = new ArrayList<>(unassignedVariables);
        Collections.sort(unassignedList, Comparator.comparingInt(frequencyTable::get));

        String mostUnassignedSymbol = unassignedList.get(unassignedList.size() - 1);
        Node<Variable> positiveAssignment = getTruthNode(mostUnassignedSymbol, level);

        return positiveAssignment.getValue();
    }

    /**
     #1. Variable State Independent Decaying Sum (state-of-the-art?)
     #2. Exponential Recency Weighted Average
     (https://www.aaai.org/ocs/index.php/AAAI/AAAI16/paper/download/12451/12112)
     */

    public Variable getNextRecencyUnassignedVariable(int level) {

        // TODO: Implement ERWA heuristics for picking branch variables

        return null;
    }

    public Variable getNextVSIDSUnassignedVariable(int level) {

        // TODO: Implement VSIDS heuristics for picking branch variables

        return null;
    }

}
