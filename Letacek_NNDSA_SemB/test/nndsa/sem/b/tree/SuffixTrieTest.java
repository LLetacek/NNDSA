package nndsa.sem.b.tree;

import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ludek
 */
public class SuffixTrieTest {

    public SuffixTrieTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of clear method, of class SuffixTrie.
     */
    @Test
    public void testClear() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "ahoj";
        instance.add(word, word);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    /**
     * Test of add method, of class SuffixTrie.
     */
    @Test
    public void testAdd1() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "ahoj";
        instance.add(word, word);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testAdd2() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "dar";
        instance.add(word, word);
        word = "nazdar";
        instance.add(word, word);
        assertEquals(instance.getValue(word), word);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd3() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "dar";
        instance.add(word, word);
        instance.add(word, word);
    }

    /**
     * Test of remove method, of class SuffixTrie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove1() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String result = "nazdar";
        instance.add(result, result);
        String word = "dar";
        instance.add(word, word);
        instance.remove(word);
        instance.getValue(word);
    }

    @Test
    public void testRemove2() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String result = "nazdar";
        instance.add(result, result);
        String word = "dar";
        instance.add(word, word);
        instance.remove(word);
        assertEquals(instance.getValue(result), result);
    }

    @Test
    public void testRemove3() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "nazdar";
        instance.add(word, word);
        String result = "dar";
        instance.add(result, result);
        instance.remove(word);
        assertEquals(instance.getValue(result), result);
    }

    /**
     * Test of isEmpty method, of class SuffixTrie.
     */
    @Test
    public void testIsEmpty() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "ahoj";
        instance.add(word, word);
        instance.remove(word);
        assertTrue(instance.isEmpty());
    }

    /**
     * Test of getNode method, of class SuffixTrie.
     */
    @Test
    public void testGetNode() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "ahoj";
        instance.add(word, word);
        assertEquals(instance.getValue(word), word);
    }

    /**
     * Test of getSimilarNodes method, of class SuffixTrie.
     */
    @Test
    public void testGetSimilarNodes1() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "dar";
        instance.add(word, word);
        word = "nazdar";
        instance.add(word, word);
        word = "bazar";
        instance.add(word, word);
        List<String> list = instance.getSimilarNodes("ar");
        assertEquals(3, list.size());
    }

    @Test
    public void testGetSimilarNodes2() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "dar";
        instance.add(word, word);
        word = "nazdar";
        instance.add(word, word);
        word = "bazar";
        instance.add(word, word);
        List<String> list = instance.getSimilarNodes("dar");
        assertEquals(2, list.size());
    }
    
    @Test
    public void testSize1() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "dar";
        instance.add(word, word);
        word = "nazdar";
        instance.add(word, word);
        word = "bazar";
        instance.add(word, word);
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testSize2() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "dar";
        instance.add(word, word);
        word = "nazdar";
        instance.add(word, word);
        word = "bazar";
        instance.add(word, word);
        instance.remove(word);
        assertEquals(2, instance.size());
    }

    @Test
    public void testSize3() {
        SuffixTrie<String, String> instance = new SuffixTrie<>();
        String word = "dar";
        instance.add(word, word);
        word = "nazdar";
        instance.add(word, word);
        word = "bazar";
        instance.add(word, word);
        instance.clear();
        assertEquals(0, instance.size());
    }
}
