package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.models.BloodStock;
import tn.esprit.services.BloodStockService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ShowStock {
    @FXML
    private TableColumn<BloodStock, LocalDate> DateCol;

    @FXML
    private TableColumn<BloodStock, String> NameCol;

    @FXML
    private TableColumn<BloodStock, Float> QantityCol;

    @FXML
    private TableView<BloodStock> ShowStock;

    @FXML
    private TableColumn<BloodStock, String> TypeCol;

    private final BloodStockService bs= new BloodStockService();
    @FXML
    void initialize(){
        try {
            List<BloodStock> bloodStocks = bs.getAll();
            ObservableList<BloodStock> observableList = FXCollections.observableList(bloodStocks);
            ShowStock.setItems(observableList);
            NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            QantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
            TypeCol.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
            DateCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }
    @FXML
    void back(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AddStock.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void delete(ActionEvent event) {
        BloodStock selectedStock = ShowStock.getSelectionModel().getSelectedItem();

        if (selectedStock != null) {
            try {

                bs.delete(selectedStock);


                refreshTableView();
            } catch (Exception e) {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur lors de la suppression");
                errorAlert.setContentText("Une erreur s'est produite lors de la suppression du stock : " + e.getMessage());
                errorAlert.showAndWait();
            }
        } else {

            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Aucune sélection");
            warningAlert.setContentText("Veuillez sélectionner un stock à supprimer.");
            warningAlert.showAndWait();
        }

    }
    private void refreshTableView() {
        try {
            List<BloodStock> bloodStocks = bs.getAll();
            ObservableList<BloodStock> observableList = FXCollections.observableList(bloodStocks);
            ShowStock.setItems(observableList);
        } catch (Exception e) {

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur lors du rafraîchissement");
            errorAlert.setContentText("Une erreur s'est produite lors du rafraîchissement de la TableView : " + e.getMessage());
            errorAlert.showAndWait();
        }
    }


    @FXML
    void update(ActionEvent event) {
        BloodStock selectedStock = ShowStock.getSelectionModel().getSelectedItem();
        if (selectedStock != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddStock.fxml"));
                Parent root = loader.load();
                AddStock controller = loader.getController();
                controller.populateFields(selectedStock);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please select a stock to update.");
            alert.showAndWait();
        }
    }

}

