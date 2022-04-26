package nndsa.sem.c.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import nndsa.sem.c.entity.SearchEngine;
import nndsa.sem.c.entity.SearchType;
import nndsa.sem.c.entity.Word;
import nndsa.sem.c.serialization.Serialization;

/**
 * FXML Controller class
 *
 * @author ludek
 */
public class MainViewController implements Initializable {

    @FXML
    private ListView<String> list;
    @FXML
    private TextField txtWord;
    
    private SearchEngine searchEngine;
    private Serialization serialization;
    private final ObservableList<String> wordStatistics = FXCollections.observableArrayList();
    private final String outBinaryFile = "./out";
    private List<Integer> statsBinary;
    int binarySum = 0;
    int binaryMin = Integer.MAX_VALUE;
    int binaryMax = Integer.MIN_VALUE;
    private List<Integer> statsInterpolation;
    int interpolationSum = 0;
    int interpolationMin = Integer.MAX_VALUE;
    int interpolationMax = Integer.MIN_VALUE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchEngine = new SearchEngine();
        serialization = new Serialization();
        list.setItems(wordStatistics);
        statsBinary = new LinkedList<>();
        statsInterpolation = new LinkedList<>();
    }     


    @FXML
    private void find(ActionEvent event) {
        clear(event);
        search(txtWord.getText(), outBinaryFile);
        clearStats();
    }

    @FXML
    private void transferWordsToBytes(ActionEvent event) {
        clear(event);
        File file = getFile("csv", "*.csv");

        if (file == null) {
            return;
        }
        
        try {
            serialization.buildBaseToBinaryFile(file.toPath().toString(), outBinaryFile);
        } catch (Exception ex) {
            wordStatistics.add("ERR: " + ex.getMessage());
        }
    }
    

    @FXML
    private void findFromFile(ActionEvent event) throws IOException {
        clearStats();
        clear(event);
        File file = getFile("txt", "*.txt");

        if (file == null) {
            return;
        }

        try (Stream<String> fileStream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
                fileStream.filter(t -> t != null).forEach(((line) -> { search(line, outBinaryFile); }));
        }
        
        printStats();
    }
    
    private void clearStats() {
        statsBinary.clear();
        binarySum = 0;
        binaryMin = Integer.MAX_VALUE;
        binaryMax = Integer.MIN_VALUE;
        statsInterpolation.clear();
        interpolationSum = 0;
        interpolationMin = Integer.MAX_VALUE;
        interpolationMax = Integer.MIN_VALUE;
    }
    
    private void printStats() {
        wordStatistics.add(0, "---------------");
        statsBinary.forEach((t) -> {
            binarySum+=t;
            if (t<binaryMin) binaryMin = t;
            if (t>binaryMax) binaryMax = t;
        });
        wordStatistics.add(0, "BINARY\t\t\t AVG:" + binarySum/statsBinary.size() +  "\tMIN:" + binaryMin + "\tMAX:" + binaryMax);
        statsInterpolation.forEach((t) -> {
            interpolationSum+=t; 
            if (t<interpolationMin) interpolationMin = t;
            if (t>interpolationMax) interpolationMax = t;
        });
        wordStatistics.add(0, "INTERPOLATION\t AVG:" + interpolationSum/statsInterpolation.size() +  "\tMIN: " + interpolationMin + "\tMAX: " + interpolationMax);
    }

    private File getFile(String description, String... extensions) {
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(description, extensions)
        );
        File file = choose.showOpenDialog(MainFX.stage);
        return file;
    }
    
    private void search(String word, String outBinaryFile) {
        Pair<Integer, Word> finder;
        try {
            finder = searchEngine.findInBinaryBase(word, outBinaryFile,SearchType.INTERPOLATION);
            statsInterpolation.add(finder.getKey());
            wordStatistics.add("INTERPOLATION:\t" + finder.getKey() + "\t: " + 
                    finder.getValue().getKey() + " | " + 
                    finder.getValue().getEnglishWord() + " | " + 
                    finder.getValue().getGermanWord());
            finder = searchEngine.findInBinaryBase(word, outBinaryFile,SearchType.BINARY);
            statsBinary.add(finder.getKey());
            wordStatistics.add("BINARY:\t\t\t" + finder.getKey() + "\t: " + finder.getValue().getKey());
            wordStatistics.add("------------");
        } catch (Exception ex) {
            wordStatistics.add("ERR: " + word + " -> " + ex.getMessage());
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        wordStatistics.clear();
        clearStats();
    }
}
