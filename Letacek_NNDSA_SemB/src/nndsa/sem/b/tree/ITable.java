package nndsa.sem.b.tree;

/**
 *
 * @author ludek
 * @param <K> Key
 * @param <V> Value
 */
public interface ITable<K, V> {
    public void clear();
    public void add(K key, V value);
    public V remove(K key);
    public boolean isEmpty();
    public V getValue(K key);
}
