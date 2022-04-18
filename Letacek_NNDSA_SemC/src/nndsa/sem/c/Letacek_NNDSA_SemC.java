
package nndsa.sem.c;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
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
            List<String> listOfWordsToFind = new LinkedList<>();
            String wordsToFindFile = "./find.txt";
            try (Stream<String> file = Files.lines(Paths.get(wordsToFindFile), StandardCharsets.UTF_8)) {
                file.filter(t -> t != null).forEach(((line) -> { listOfWordsToFind.add(line); }));
            }
            
            String csvFile = "./main.csv";
            String outBinaryFile = "./out";
            serialization.buildBaseToBinaryFile(csvFile, outBinaryFile);
            Pair<Integer, Word> finder;
            
            listOfWordsToFind.forEach((t) -> {
                try {
                    testSearch(searchEngine, t, outBinaryFile);
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            });
            
            /*testSearch(searchEngine, "kopie", outBinaryFile);
            testSearch(searchEngine, "abakus", outBinaryFile);
            testSearch(searchEngine, "absurdita", outBinaryFile);
            testSearch(searchEngine, "bezdomovec", outBinaryFile);
            testSearch(searchEngine, "žívanější", outBinaryFile);
            testSearch(searchEngine, "žaloba", outBinaryFile);
            testSearch(searchEngine, "mrak", outBinaryFile);*/
            
        } catch(Exception ex) {
            System.err.println("ERR:" + ex.getMessage());
        }
    }

    private static void testSearch(SearchEngine searchEngine, String word, String outBinaryFile) throws IOException {
        Pair<Integer, Word> finder;
        finder = searchEngine.findInBinaryBase(word, outBinaryFile,SearchType.BINARY);
        System.out.println("BINARY:\t\t" + finder.getKey() + "\t: " + finder.getValue().getKey());
        finder = searchEngine.findInBinaryBase(word, outBinaryFile,SearchType.INTERPOLATION);
        System.out.println("INTERPOLATION:\t" + finder.getKey() + "\t: " + 
                finder.getValue().getKey() + " | " + 
                finder.getValue().getEnglishWord() + " | " + 
                finder.getValue().getGermanWord());
        System.out.println("------------");
    }
    
}
