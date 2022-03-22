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

/**
 *
 * @author ludek
 */
public class Serialization {
    
    public static void save(String fileBase, RhymeDictionary dictionary) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter pw = new PrintWriter(fileBase, "UTF-8");
        List<String> connection = new LinkedList<>();
        
        dictionary.getWordsThatRhyme("").forEach((word) -> {
            // TODO
        });
        connection.forEach(pw::print);
        pw.close();
    }

    public static void load(String fileBase, RhymeDictionary dictionary) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        dictionary.clear();

        try (Stream<String> file = Files.lines(Paths.get(fileBase), StandardCharsets.UTF_8)) {
            file.filter(t -> t != null)
                    .forEach((String line) -> {
                            // TODO
                    });
        }
    }
    
}
