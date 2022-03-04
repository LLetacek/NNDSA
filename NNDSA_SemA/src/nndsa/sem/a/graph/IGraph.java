package nndsa.sem.a.graph;

import java.util.List;

public interface IGraph<KN, N> {
    public void addEdge(KN start, KN destination);
    public void addVertex(KN key, N data);
    public void deleteEdge(KN start, KN destination);
    public void deleteEdgeIfExists(KN start, KN destination);
    public void deleteVertex(KN keyNode);
    public N getVertexData(KN key);
    public List<KN> getAdjencyVertexKeys(KN key);
    public List<KN> getAllVertexKeys();
    public int getVertexCounter();
    public void clear();
}
