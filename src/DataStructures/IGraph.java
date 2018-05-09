package DataStructures;

import java.util.*;

public class IGraph {

    private Graph<Variable> graph;

    private HashSet<String> variables;
    private HashMap<String, Node<Variable>> assignedVariables;
    private HashSet<String> unassignedVariables;

    private Clause conflictClause;
    private Node<Variable> conflictNode;

    private HashMap<Clause, Boolean> clauseTruthMap;

    public IGraph(HashSet<String> variables) {
        graph = new Graph<>();

        this.variables = variables;
        assignedVariables = new HashMap<>();
        unassignedVariables = new HashSet<>();
        unassignedVariables.addAll(variables);

        clauseTruthMap = new HashMap<>();

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

        clauseTruthMap = new HashMap<>();

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
            } else if (node == null) {
                return null;
            } else if (literal.evaluate(node.value.getValue())) {
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

    public Clause getNextAntecedentClause(Clause learntClause) {
        HashSet<Literal> literals = learntClause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> node = assignedVariables.get(literal.getName());
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

    public Boolean evaluate(Clause clause) {
        boolean isIncomplete = false;

        Boolean memoizedTruth = clauseTruthMap.get(clause);

        if (memoizedTruth != null) {
            return memoizedTruth;
        }

        HashSet<Literal> literals = clause.getLiterals();
        for (Literal literal : literals) {
            Node<Variable> node = assignedVariables.get(literal.getName());
            if (node == null) {
                isIncomplete = true;
            } else if (literal.evaluate(node.value.getValue())) {
                clauseTruthMap.put(clause, true);
                return true;
            }
        }

        if (!isIncomplete) {
            clauseTruthMap.put(clause, false);
            return false;
        }

        return null;
    }

    public boolean evaluate(Clauses formula) {
        HashSet<Clause> clauses = formula.getClausesSet();
        for (Clause clause : clauses) {
            Boolean clauseTruth = evaluate(clause);
            if (clauseTruth == null || !clauseTruth) {
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
        if (unassignedVariables.isEmpty()) {
            return null;
        }

        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(unassignedVariables.size());

        ArrayList<String> list = new ArrayList<>(unassignedVariables);
        String symbol = list.get(randomNumber);
        Node<Variable> node = getTruthNode(symbol, level);

        return node.getValue();
    }

    public Variable getNextMostUnassignedVariable(int level, TreeMap<String, Integer> table, boolean shouldBreakTie) {
        if (unassignedVariables.isEmpty()) {
            return null;
        }

        String selectedSymbol;

        if (shouldBreakTie) {
            HashMap<Integer, ArrayList<String>> countList = new HashMap<>();
            for (String variable : unassignedVariables) {
                countList.computeIfAbsent(table.get(variable),key -> new ArrayList<>()).add(variable);
            }

            Optional<Map.Entry<Integer, ArrayList<String>>> entry = countList.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByKey());

            if (!entry.isPresent()) {
                return null;
            }

            ArrayList<String> mostUnassignedSymbols = entry.get().getValue();

            Random randomGenerator = new Random();
            int randomNumber = randomGenerator.nextInt(mostUnassignedSymbols.size());

            selectedSymbol = mostUnassignedSymbols.get(randomNumber);
        } else {
            ArrayList<String> unassignedList = new ArrayList<>(unassignedVariables);
            unassignedList.sort(Comparator.comparingInt(table::get));
            selectedSymbol = unassignedList.get(unassignedList.size() - 1);
        }

        Node<Variable> positiveAssignment = getTruthNode(selectedSymbol, level);

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
