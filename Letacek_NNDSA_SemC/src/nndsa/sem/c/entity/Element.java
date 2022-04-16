package nndsa.sem.c.entity;

/**
 *
 * @author ludek
 * @param <T>
 */
public interface Element<T> {
    
    T getKey();
    int getElementSizeInBytes();
    
}
