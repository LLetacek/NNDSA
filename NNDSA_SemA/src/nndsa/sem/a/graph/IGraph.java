package nndsa.sem.a.graph;

import java.util.List;

public interface IGraph<KN, N> {
    public void addEdge(KN start, KN destination);
    public void addNode(KN key, N data);
    public void deleteEdge(KN start, KN destination);
    public void deleteEdgeIfExists(KN start, KN destination);
    public void deleteNode(KN keyNode);
    public N getNodeData(KN key);
    public List<KN> getAdjencyNodeKeys(KN key);
    public List<KN> getAllNodeKeys();
    public int getNodeCounter();
}
