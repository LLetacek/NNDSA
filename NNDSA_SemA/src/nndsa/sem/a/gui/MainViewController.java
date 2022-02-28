package nndsa.sem.a.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nndsa.sem.a.railway.RailwayTrackType;
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
    private TableColumn<SimpleRailway, String> VRKey;
    @FXML
    private TableColumn<SimpleRailway, Integer> VRLength;
    @FXML
    private TableColumn<SimpleRailway, Integer> VROccupancy;
    @FXML
    private TableColumn<SimpleRailway, RailwayTrackType> VRType;
    @FXML
    private TableView<String> tbViewNeighbor;
    @FXML
    private TableColumn<String, String> VNKey;
    @FXML
    private TextField txtFieldKey;
    @FXML
    private TextField txtFieldLength;
    @FXML
    private TextField txtFieldOccupancy;
    @FXML
    private ChoiceBox<RailwayTrackType> txtFieldType;
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
        ObservableList<RailwayTrackType> options = FXCollections.observableArrayList(RailwayTrackType.values());
        txtFieldType.getItems().addAll(options);
        txtFieldType.setValue(RailwayTrackType.DIRECT);
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
