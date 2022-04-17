
package nndsa.sem.c;

import java.io.IOException;
import javafx.util.Pair;
import nndsa.sem.c.entity.SearchEngine;
import nndsa.sem.c.entity.SearchType;
import nndsa.sem.c.entity.Word;
import nndsa.sem.c.serialization.Serialization;

/**
 *
 * @author ludek
 */
public class Letacek_NNDSA_SemC {

    public static void main(String[] args) {
        // TODO code application logic here
        Serialization serialization = new Serialization();
        SearchEngine searchEngine = new SearchEngine();
        
        try {
            String csvFile = "./main.csv";
            String outBinaryFile = "./out";
            serialization.buildBaseToBinaryFile(csvFile, outBinaryFile);
            Pair<Integer, Word> finder;
            
            testSearch(searchEngine, "kopie", outBinaryFile);
            testSearch(searchEngine, "abakus", outBinaryFile);
            testSearch(searchEngine, "absurdita", outBinaryFile);
            testSearch(searchEngine, "bezdomovec", outBinaryFile);
            testSearch(searchEngine, "žívanější", outBinaryFile);
            testSearch(searchEngine, "žaloba", outBinaryFile);
            testSearch(searchEngine, "mrak", outBinaryFile);
            
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static void testSearch(SearchEngine searchEngine, String word, String outBinaryFile) throws IOException {
        Pair<Integer, Word> finder;
        finder = searchEngine.findInBinaryBase(word, outBinaryFile,SearchType.BINARY);
        System.out.println(finder.getKey() + " : " + finder.getValue().getKey());
        finder = searchEngine.findInBinaryBase(word, outBinaryFile,SearchType.INTERPOLATION);
        System.out.println(finder.getKey() + " : " + finder.getValue().getKey());
    }
    
}
