package nndsa.sem.a.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author st58262 Letacek
 * @param <N> node data
 * @param <KN> node key value
 */
public class Graph<KN, N> implements IGraph<KN, N> {

    private final HashMap<KN, Node> nodes;
    private int nodeCounter;
    
    public Graph() {
        nodes = new HashMap<>();
        nodeCounter = 0;
    }

    @Override
    public void addEdge(KN start, KN destination) {
        if (start == null || destination == null)
            throw new IllegalArgumentException();

        Node startNode = nodes.get(start);
        Node destinationNode = nodes.get(destination);
        if(startNode == null || destinationNode == null)
            throw new IllegalArgumentException("Unknown node!");
        
        Edge edge = new Edge(destinationNode);
        startNode.adjacentEdges.add(edge);
    }

    @Override
    public void addNode(KN key, N data) {
        if (key == null || data == null)
            throw new IllegalArgumentException("Unknown node!");
        
        Node node = new Node(key, data);
        nodes.put(key, node);
        ++nodeCounter;
    }

    @Override
    public void deleteEdge(KN start, KN destination) {
        Node node = getNode(start);
        
        int idEdge = 0;
        for (; idEdge < node.adjacentEdges.size(); ++idEdge) {
            if (node.adjacentEdges.get(idEdge).nextNode.key == destination)
                break;
        }
        
        if (idEdge==node.adjacentEdges.size())
            throw new IllegalArgumentException("Destination node not found!");
        
        node.adjacentEdges.remove(idEdge);
    }

    @Override
    public void deleteNode(KN keyNode) {
        nodes.remove(keyNode);
        nodes.forEach((k, node) -> {
            List<Edge> indexList = new LinkedList<>();
            for (int i = 0; i < node.adjacentEdges.size(); i++) {
                Edge edge = node.adjacentEdges.get(i);
                if(edge.nextNode.key.equals(keyNode))
                    indexList.add(edge);    
            }
            node.adjacentEdges.removeAll(indexList);    
        });
        --nodeCounter;
    }
    
    @Override
    public N getNodeData(KN key) {
        Node node = getNode(key);
        return node.data;
    }

    @Override
    public List<KN> getAdjencyNodeKeys(KN key) {
        Node node = getNode(key);
        List<KN> keyNodes = new LinkedList<>();
        node.adjacentEdges.forEach((edge) -> {
            keyNodes.add(edge.nextNode.key);
        });
        return keyNodes;
    }
    
    @Override
    public List<KN> getAllNodeKeys() {
        return new LinkedList<>(nodes.keySet());
    }
    
    @Override
    public int getNodeCounter() {
        return nodeCounter;
    }
    
    private Node getNode(KN key) {
        Node node = nodes.get(key);
        
        if (node==null)
            throw new IllegalArgumentException("Key of node not found!");
        
        return node;
    }

    private class Node {
        KN key;
        N data;
        List<Edge> adjacentEdges;

        public Node(KN key, N data) {
            this.key = key;
            this.data = data;
            this.adjacentEdges = new LinkedList<>();
        }
    }

    private class Edge {
        Node nextNode;

        public Edge(Node nextNode) {
            this.nextNode = nextNode;
        }
    }
}
