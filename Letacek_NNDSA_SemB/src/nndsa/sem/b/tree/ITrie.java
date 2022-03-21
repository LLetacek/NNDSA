package nndsa.sem.b.tree;

import java.util.List;

/**
 *
 * @author ludek
 * @param <K> Key
 */
public interface ITrie<K extends CharSequence, V> extends ITable<K, V> {
    List<V> getSimilarNodes(K key);
}
