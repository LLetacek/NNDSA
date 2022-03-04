package nndsa.sem.a.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author st58262 Letacek
 * @param <V> vertex data
 * @param <KV> vertex key value
 */
public class Graph<KV, V> implements IGraph<KV, V> {

    private final HashMap<KV, Vertex> vertexes;
    private int vertexCounter;

    public Graph() {
        vertexes = new HashMap<>();
        vertexCounter = 0;
    }

    @Override
    public void addEdge(KV start, KV destination) {
        if (start == null || destination == null) {
            throw new IllegalArgumentException();
        }

        Vertex startVertex = vertexes.get(start);
        Vertex destinationVertex = vertexes.get(destination);
        if (startVertex == null || destinationVertex == null) {
            throw new IllegalArgumentException("Unknown vertex!");
        }

        Edge edge = new Edge(destinationVertex);
        startVertex.adjacentEdges.add(edge);
    }

    @Override
    public void addVertex(KV key, V data) {
        if (key == null || data == null) {
            throw new IllegalArgumentException("Unknown vertex!");
        }
        
        if(vertexes.containsKey(key))
            throw new IllegalArgumentException("Key already exists!");

        Vertex vertex = new Vertex(key, data);
        vertexes.put(key, vertex);
        ++vertexCounter;
    }

    @Override
    public void deleteEdge(KV start, KV destination) {
        Vertex vertex = getVertex(start);

        int idEdge = getEdgeIndex(vertex, destination);
        if (idEdge == vertex.adjacentEdges.size()) {
            throw new IllegalArgumentException("Destination vertex not found!");
        }

        vertex.adjacentEdges.remove(idEdge);
    }

    @Override
    public void deleteEdgeIfExists(KV start, KV destination) {
        Vertex vertex = getVertex(start);

        int idEdge = getEdgeIndex(vertex, destination);
        if (idEdge == vertex.adjacentEdges.size()) {
            return;
        }

        vertex.adjacentEdges.remove(idEdge);
    }

    private int getEdgeIndex(Vertex vertex, KV destination) {
        int idEdge = 0;
        for (; idEdge < vertex.adjacentEdges.size(); ++idEdge) {
            if (vertex.adjacentEdges.get(idEdge).nextVertex.key.equals(destination)) {
                break;
            }
        }
        return idEdge;
    }

    @Override
    public void deleteVertex(KV keyVertex) {
        vertexes.remove(keyVertex);
        vertexes.forEach((k, vertex) -> {
            List<Edge> indexList = new LinkedList<>();
            for (int i = 0; i < vertex.adjacentEdges.size(); i++) {
                Edge edge = vertex.adjacentEdges.get(i);
                if (edge.nextVertex.key.equals(keyVertex)) {
                    indexList.add(edge);
                }
            }
            vertex.adjacentEdges.removeAll(indexList);
        });
        --vertexCounter;
    }

    @Override
    public V getVertexData(KV key) {
        Vertex vertex = getVertex(key);
        return vertex.data;
    }

    @Override
    public List<KV> getAdjencyVertexKeys(KV key) {
        Vertex vertex = getVertex(key);
        List<KV> keyVertexes = new LinkedList<>();
        vertex.adjacentEdges.forEach((edge) -> {
            keyVertexes.add(edge.nextVertex.key);
        });
        return keyVertexes;
    }

    @Override
    public List<KV> getAllVertexKeys() {
        return new LinkedList<>(vertexes.keySet());
    }

    @Override
    public int getVertexCounter() {
        return vertexCounter;
    }

    private Vertex getVertex(KV key) {
        Vertex vertex = vertexes.get(key);

        if (vertex == null) {
            throw new IllegalArgumentException("Key of vertex not found!");
        }

        return vertex;
    }
    
    @Override
    public void clear(){
        vertexes.clear();
        vertexCounter = 0;
    }

    private class Vertex {

        KV key;
        V data;
        List<Edge> adjacentEdges;

        public Vertex(KV key, V data) {
            this.key = key;
            this.data = data;
            this.adjacentEdges = new ArrayList<>();
        }
    }

    private class Edge {

        Vertex nextVertex;

        public Edge(Vertex nextVertex) {
            this.nextVertex = nextVertex;
        }
    }
}
