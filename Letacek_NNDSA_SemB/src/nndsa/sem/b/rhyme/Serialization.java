package nndsa.sem.b.rhyme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import jdk.nashorn.internal.ir.ContinueNode;

/**
 *
 * @author ludek
 */
public class Serialization {
    
    public static void save(String fileBase, RhymeDictionary dictionary) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter pw = new PrintWriter(fileBase, "UTF-8");
        dictionary.getWordsEndingIn("").forEach(pw::println);
        pw.close();
    }

    private static boolean resultOfLoad;
    public static boolean load(String fileBase, RhymeDictionary dictionary) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        resultOfLoad = true;
        dictionary.clear();

        try (Stream<String> file = Files.lines(Paths.get(fileBase), StandardCharsets.UTF_8)) {
            file.filter(t -> t != null)
                    .forEach((String line) -> {
                        try {
                            dictionary.insert(line);
                        } catch (Exception ex) {
                            resultOfLoad = false;
                        }
                    });
        }
        return resultOfLoad;
    }
    
}
