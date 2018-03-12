package DataStructures;

import java.util.*;

public class IGraph {
    private Graph<Variable> graph;
    private HashMap<String, Node<Variable>> values;
    private HashMap<Variable, Integer> levels;
    private HashMap<Integer, HashSet<Node<Variable>>> decisions;
    private HashSet<String> variables;
    private Clause conflictClause;

    private enum Type { CONFLICT }

    public IGraph(HashSet<String> variables) {
        this.variables = variables;
        graph = new Graph<>();
        values = new HashMap<>();
        levels = new HashMap<>();
        decisions = new HashMap<>();
        conflictClause = null;
    }

    public boolean addAssignment(Node<Variable> node, int level) {
        Variable assignment = node.value;
        if (graph.containsNode(node)) {
            return false; /** Guard against repeated or conflicting literal node assignment **/
        }
        graph.addNode(node);
        values.put(assignment.getSymbol(), node);
        levels.put(assignment, level);
        return true;
    }

    public void addDecision(Variable decision, int decisionLevel) {
        Node<Variable> decisionNode = new Node<>(decision);
        HashSet<Node<Variable>> previousDecisions = decisions.get(decisionLevel);
        if (previousDecisions != null && previousDecisions.contains(decisionNode)) {
            return; /** Guard against repeated decisions **/
        }
        if (!addAssignment(decisionNode, decisionLevel)) {
            return; /** Guard against repeated or conflicting assignment **/
        }
        decisions.computeIfAbsent(decisionLevel, key -> new HashSet<>()).add(decisionNode);
    }

    public void addImplication(Clause antecedent, Variable implication, int propagationLevel) {
        HashSet<Literal> literals = antecedent.getLiterals();
        Literal impliedLiteral = new Literal(implication.getSymbol(), implication.getValue());
        if (!literals.contains(impliedLiteral)) {
            return; /** Guard against wrong antecedent clause **/
        }
        Node<Variable> impliedNode = new Node<>(implication);
        if (!addAssignment(impliedNode, propagationLevel)) {
            return; /** Guard against repeated or conflicting assignment **/
        }
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
        if (!isConflicted(conflictClause)) {
            return; /** Guard against adding non-conflicted clause **/
        }
        this.conflictClause = conflictClause;
        Type conflictLabel = Type.CONFLICT;
        Variable conflict = new Variable(conflictLabel.name(), true);
        Node<Variable> conflictNode = new Node<>(conflict.hashCode(), conflict);
        graph.addNode(conflictNode);
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
            }
        });
        conflictClause = null;
    }

    public boolean isConflicted(Clause clause) {
        HashSet<Literal> literals = clause.getLiterals();
        for (Literal literal : literals) {
            Boolean value = values.get(literal.getName()).value.getValue();
            if (value == null || (value != null && literal.evaluate(value))) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllAssigned() {
        return graph.getNodes().size() == variables.size();
    }

    public Clause getConflictClause() {
        return conflictClause;
    }

    public HashMap<String, Boolean> getAssignment() {
        HashMap<String, Boolean> assignment = new HashMap<>();
        values.forEach((key, value) -> assignment.put(key, value.value.getValue()));
        return assignment;
    }

}
