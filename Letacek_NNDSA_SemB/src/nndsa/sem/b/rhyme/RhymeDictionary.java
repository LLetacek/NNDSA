package nndsa.sem.b.rhyme;

import java.util.List;
import nndsa.sem.b.tree.ITrie;
import nndsa.sem.b.tree.SuffixTrie;

/**
 *
 * @author ludek
 */
public class RhymeDictionary {

    ITrie<String, String> words;

    public RhymeDictionary() {
        this.words = new SuffixTrie<>();
    }
    
    public void insert(String word) {
        words.add(word, word);
    }
    
    public void remove(String word) {
        words.remove(word);
    }
    
    public boolean contains(String word) {
        try {
            words.getValue(word);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    public int size() {
        return words.size();
    }
    
    public void clear() {
        words.clear();
    }
    
    public List<String> getWordsEndingIn(String finalSubstring) {
        return words.getValuesEndingWithKey(finalSubstring);
    }
}
