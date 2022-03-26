package nndsa.sem.b.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import nndsa.sem.b.rhyme.RhymeDictionary;
import nndsa.sem.b.rhyme.Serialization;

/**
 * FXML Controller class
 *
 * @author ludek
 */
public class MainViewController implements Initializable {

    @FXML
    private TextField input;
    @FXML
    private ListView<String> listSuggestion;

    private ObservableList<String> wordData = FXCollections.observableArrayList();
    private RhymeDictionary dictionary;
    @FXML
    private Label counterAll;
    @FXML
    private Label counterFiltered;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        counterFiltered.setText(String.valueOf(0));
        counterAll.setText(String.valueOf(0));
        listSuggestion.setItems(wordData);
        dictionary = new RhymeDictionary();
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                wordData.clear();
                return;
            }
            refreshData(newValue);
        });
    }

    @FXML
    private void load(ActionEvent event) {
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("txt", "*.txt")
        );
        File file = choose.showOpenDialog(FXMain.stage);

        if (file == null) {
            return;
        }

        try {
            if (!Serialization.load(file.toString(), dictionary)) {
                error("Load", "Some words did not enter. The file may contain blank lines or duplicates.");
            }
        } catch (IOException e) {
            error("Load", e.getMessage());
        }
        counterAll.setText(String.valueOf(dictionary.size()));
        refreshData(input.getText());
    }

    private void refreshData(String substring) {
        wordData.clear();
        wordData.addAll(dictionary.getWordsEndingIn(substring));
        counterAll.setText(String.valueOf(dictionary.size()));
        counterFiltered.setText(String.valueOf(wordData.size()));
    }

    @FXML
    private void save(ActionEvent event) {
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("txt", "*.txt")
        );
        File file = choose.showSaveDialog(FXMain.stage);

        if (file == null) {
            return;
        }

        try {
            Serialization.save(file.toString(), dictionary);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            error("Save", e.getMessage());
        }
    }

    @FXML
    private void showAll(ActionEvent event) {
        refreshData("");
    }

    @FXML
    private void deleteWord(ActionEvent event) {
        String word = listSuggestion.getSelectionModel().getSelectedItem();
        if (word == null) {
            error("Delete", "Please select the word you want to remove from the list!");
            return;
        }

        try {
            dictionary.remove(word);
            refreshData(input.getText());
        } catch (Exception ex) {
            error("Delete", ex.getMessage());
        }
    }

    @FXML
    private void addWord(ActionEvent event) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Dictionary");
        dialog.setHeaderText("Add word");
        dialog.setResizable(false);

        Label label = new Label("word: ");
        TextField text = new TextField();

        GridPane grid = new GridPane();
        grid.add(label, 1, 1);
        grid.add(text, 2, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, Boolean>() {
            @Override
            public Boolean call(ButtonType param) {
                Boolean result = false;
                if (param == buttonTypeOk) {
                    result = true;
                    try {
                        dictionary.insert(text.getText());
                    } catch (Exception ex) {
                        error("Add", ex.getMessage());
                    }
                }
                return result;
            }
        });

        Optional<Boolean> result = dialog.showAndWait();
        if(!result.isPresent() || result.get()==false) {
            error("Add", "Something went wrong.");  
            return;
        }
        
        refreshData(input.getText());
    }

    private void error(String headline, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headline);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void clear(ActionEvent event) {
        dictionary.clear();
        refreshData("");
    }
}
