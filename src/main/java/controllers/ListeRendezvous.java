package controllers;

import entities.Rendezvous;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.ServiceRendezvous;
import services.Servicecentrededecollect;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ListeRendezvous implements Initializable {

    @FXML
    private TableColumn<Rendezvous, String> bloodType;

    @FXML
    private TableColumn<Rendezvous, String> collectionCenter;

    @FXML
    private TableColumn<Rendezvous, String> date;

    @FXML
    private TableColumn<Rendezvous, String> email;

    @FXML
    private TableColumn<Rendezvous, String> name;

    @FXML
    private TableView<Rendezvous> table;

    @FXML
    private TableColumn<Rendezvous, String> heure;

    @FXML
    private Label userlabel;

    private final ServiceRendezvous service = new ServiceRendezvous();

    public void showDashboard(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeDeCentre.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle depuis l'événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
           List<Rendezvous> RDVs= service.afficher();
            ObservableList<Rendezvous> listRDV= FXCollections.observableArrayList(RDVs);
            name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
            bloodType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBloodType()));
            heure.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeure()));
            collectionCenter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCentredecollectName()));
            date.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));




            table.setItems(listRDV);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
