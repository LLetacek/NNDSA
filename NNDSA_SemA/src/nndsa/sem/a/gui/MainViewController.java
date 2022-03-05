package nndsa.sem.a.gui;

import java.io.File;
import java.net.URL;
import java.util.List;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import nndsa.sem.a.railway.RailwayTrackType;
import nndsa.sem.a.railway.Railway;
import nndsa.sem.a.railway.RailwayDirectionType;
import nndsa.sem.a.railway.RailwayInfrastructure;
import nndsa.sem.a.railway.Serialization;

/**
 * FXML Controller class
 *
 * @author ludek
 */
public class MainViewController implements Initializable {

    @FXML
    private TableView<Railway> tbViewRailway;
    @FXML
    private TableColumn<Railway, String> columnKey;
    @FXML
    private TableColumn<Railway, Integer> columnLength;
    @FXML
    private TableColumn<Railway, Integer> columnOccupancy;
    @FXML
    private TableColumn<Railway, RailwayTrackType> columnType;
    @FXML
    private ListView<String> shortestPath;
    @FXML
    private ListView<String> listNeighbor;

    private ObservableList<Railway> railwayData = FXCollections.observableArrayList();
    private ObservableList<String> railwayNeighbor = FXCollections.observableArrayList();
    private RailwayInfrastructure railwayInfrastructure;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        railwayInfrastructure = new RailwayInfrastructure();

        tbViewRailway.setItems(railwayData);
        columnKey.setCellValueFactory(new PropertyValueFactory("key"));
        columnLength.setCellValueFactory(new PropertyValueFactory("length"));
        columnOccupancy.setCellValueFactory(new PropertyValueFactory("occupancy"));
        columnType.setCellValueFactory(new PropertyValueFactory("type"));

        listNeighbor.setItems(railwayNeighbor);

        tbViewRailway.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            railwayNeighbor.clear();
            if (newValue != null) {
                railwayNeighbor.addAll(railwayInfrastructure.getConnectedRailwayKeys(newValue.getKey()));
            }
        });
    }

    @FXML
    private void addRailway(ActionEvent event) {
        Dialog<Railway> dialog = new Dialog<>();
        dialog.setTitle("Railway");
        dialog.setHeaderText("Add railway");
        dialog.setResizable(false);

        Label label1 = new Label("Key: ");
        Label label2 = new Label("Length: ");
        Label label3 = new Label("Occupancy: ");
        Label label4 = new Label("Type: ");
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        TextField text3 = new TextField();
        ChoiceBox choice = new ChoiceBox(FXCollections.observableArrayList(RailwayTrackType.values()));
        choice.setValue(RailwayTrackType.DIRECT);

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(text3, 2, 3);
        grid.add(label4, 1, 4);
        grid.add(choice, 2, 4);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, Railway>() {
            @Override
            public Railway call(ButtonType b) {
                int length;
                int occupancy;

                if (b == buttonTypeOk) {
                    Railway railway = null;
                    try {
                        length = Integer.parseInt(text2.getText());
                        occupancy = Integer.parseInt(text3.getText());
                        railway = new Railway(text1.getText(), length, RailwayDirectionType.BOTH, (RailwayTrackType) choice.getSelectionModel().getSelectedItem(), occupancy);
                    } catch (NumberFormatException e) {
                        error("Add railway", "Occupancy and length must be number!");
                    } catch (Exception e) {
                        error("Add railway", e.getMessage());
                    }
                    return railway;
                }
                return null;
            }
        });
        Optional<Railway> result = dialog.showAndWait();

        try {
            if (result.isPresent() && result.get() != null) {
                railwayInfrastructure.addRailway(result.get());
                refreshData();
            }
        } catch (Exception e) {
            error("Add railway", e.getMessage());
        }
    }

    @FXML
    private void addNeighbor(ActionEvent event) {
        Railway selected = tbViewRailway.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("Add", "Not selected item!");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Add neighbor");
        dialog.setContentText("Key: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                railwayInfrastructure.addConnection(selected.getKey(), result.get());
                refreshNeighbors(selected);
            } catch (Exception e) {
                error("Add neighbor", e.getMessage());
            }
        }
    }

    @FXML
    private void deleteRailway(ActionEvent event) {
        Railway selected = tbViewRailway.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("Delete", "Not selected item!");
            return;
        }

        try {
            railwayInfrastructure.deleteRailway(selected.getKey());
            refreshData();
        } catch (Exception e) {
            error("Delete", e.getMessage());
        }
    }

    @FXML
    private void deleteNeighbor(ActionEvent event) {
        Railway selectedRailway = tbViewRailway.getSelectionModel().getSelectedItem();
        String selectedKey = listNeighbor.getSelectionModel().getSelectedItem();
        if (selectedRailway == null || selectedKey == null) {
            error("Delete", "Not selected item!");
            return;
        }

        try {
            railwayInfrastructure.deleteConnection(selectedRailway.getKey(), selectedKey);
            refreshNeighbors(selectedRailway);
        } catch (Exception e) {
            error("Delete", e.getMessage());
        }
    }

    private void refreshNeighbors(Railway selectedRailway) {
        railwayNeighbor.clear();
        railwayNeighbor.addAll(railwayInfrastructure.getConnectedRailwayKeys(selectedRailway.getKey()));
    }

    @FXML
    private void save(ActionEvent event) {
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("csv", "*.csv")
        );
        File file = choose.showSaveDialog(Main.stage);

        if (file == null) {
            return;
        }

        try {
            Serialization.saveToCSV(file.toString(), railwayInfrastructure);
        } catch (Exception e) {
            error("Save", e.getMessage());
        }
    }

    @FXML
    private void load(ActionEvent event) {
        FileChooser choose = new FileChooser();
        choose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("csv", "*.csv")
        );
        File file = choose.showOpenDialog(Main.stage);

        if (file == null) {
            return;
        }

        try {
            Serialization.loadFromCSV(file.toString(), railwayInfrastructure);
        } catch (Exception e) {
            error("Load", e.getMessage());
        }

        refreshData();
    }

    private void refreshData() {
        railwayData.clear();
        railwayNeighbor.clear();
        List<Railway> list = railwayInfrastructure.getSimpleRailways();
        railwayData.addAll(list);
    }

    @FXML
    private void findShortestPath(ActionEvent event) {
        /*Dialog<Railway> dialog = new Dialog<>();
        dialog.setTitle("Railway");
        dialog.setHeaderText("Add railway");
        dialog.setResizable(false);

        Label label1 = new Label("start: ");
        Label label2 = new Label("start: ");
        Label label3 = new Label("end: ");
        //length of train
        TextField text1 = new TextField();
        text1.setText(String.valueOf(selected.getLength()));
        TextField text2 = new TextField();
        text2.setText(String.valueOf(selected.getOccupancy()));


        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        dialog.getDialogPane().setContent(grid);*/
    }

    @FXML
    private void updateRailway(ActionEvent event) {
        Railway selected = tbViewRailway.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("Add", "Not selected item!");
            return;
        }
        Dialog<Railway> dialog = new Dialog<>();
        dialog.setTitle("Railway");
        dialog.setHeaderText("Add railway");
        dialog.setResizable(false);

        Label label1 = new Label("Length: ");
        Label label2 = new Label("Occupancy: ");
        TextField text1 = new TextField();
        text1.setText(String.valueOf(selected.getLength()));
        TextField text2 = new TextField();
        text2.setText(String.valueOf(selected.getOccupancy()));


        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, Railway>() {
            @Override
            public Railway call(ButtonType b) {
                int length;
                int occupancy;

                if (b == buttonTypeOk) {
                    try {
                        length = Integer.parseInt(text1.getText());
                        occupancy = Integer.parseInt(text2.getText());
                        selected.setLength(length);
                        selected.setOccupancy(occupancy);
                    } catch (NumberFormatException e) {
                        error("Add railway", "Occupancy and length must be number!");
                        return null;
                    } catch (Exception e) {
                        error("Add railway", e.getMessage());
                        return null;
                    }
                    return selected;
                }
                return null;
            }
        });
        Optional<Railway> result = dialog.showAndWait();

        try {
            if (result.isPresent() && result.get() != null) {
                railwayInfrastructure.updateRailway(result.get().getKey(), result.get().getLength(), result.get().getOccupancy());
                refreshData();
            }
        } catch (Exception e) {
            error("Add railway", e.getMessage());
        }
    }

    private void error(String headline, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headline);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
