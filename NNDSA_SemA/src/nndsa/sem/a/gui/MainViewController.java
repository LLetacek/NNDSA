package nndsa.sem.a.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nndsa.sem.a.railway.SimpleRailway;

/**
 * FXML Controller class
 *
 * @author ludek
 */
public class MainViewController implements Initializable {

    @FXML
    private TableView<SimpleRailway> tbViewRailway;
    @FXML
    private TableColumn<?, ?> VRKey;
    @FXML
    private TableColumn<?, ?> VRLength;
    @FXML
    private TableColumn<?, ?> VROccupancy;
    @FXML
    private TableColumn<?, ?> VRType;
    @FXML
    private TableView<?> tbViewNeighbor;
    @FXML
    private TableColumn<?, ?> VNKey;
    @FXML
    private TextField txtFieldKey;
    @FXML
    private TextField txtFieldLength;
    @FXML
    private TextField txtFieldOccupancy;
    @FXML
    private ChoiceBox<?> txtFieldType;
    @FXML
    private TextField txtFieldNeighborsKey;
    @FXML
    private TextArea dijkstraResult;
    @FXML
    private TextField dijkstraStart;
    @FXML
    private TextField dijkstraEnd;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addRailway(ActionEvent event) {
    }

    @FXML
    private void addNeighbor(ActionEvent event) {
    }

    @FXML
    private void deleteRailway(ActionEvent event) {
    }

    @FXML
    private void deleteNeighbor(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
    }

    @FXML
    private void load(ActionEvent event) {
    }

    @FXML
    private void findShortestPath(ActionEvent event) {
    }
    
}
