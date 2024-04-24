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
import tn.esprit.models.BloodTransaction;
import tn.esprit.services.BloodTransactionService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ShowTransaction {
    @FXML
    private TableColumn<BloodTransaction, LocalDate> donationDateCol;

    @FXML
    private TableColumn<BloodTransaction, Float> quantityDonatedCol;

    @FXML
    private TableColumn<BloodTransaction, String> transactionTypeCol;

    @FXML
    private TableView<BloodTransaction> ShowTransaction;

    private final BloodTransactionService bt = new BloodTransactionService();

    @FXML
    void initialize() {
        try {
            List<BloodTransaction> bloodTransactions = bt.getAll();
            ObservableList<BloodTransaction> observableList = FXCollections.observableList(bloodTransactions);
            ShowTransaction.setItems(observableList);
            quantityDonatedCol.setCellValueFactory(new PropertyValueFactory<>("quantityDonated"));
            transactionTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
            donationDateCol.setCellValueFactory(new PropertyValueFactory<>("donationDate"));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void BackTransaction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AddTransaction.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void DeleteTransaction(ActionEvent event) {
        BloodTransaction selectedTransaction = ShowTransaction.getSelectionModel().getSelectedItem();

        if (selectedTransaction != null) {
            try {
                bt.delete(selectedTransaction);
                refreshTableView();
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error Deleting Transaction");
                errorAlert.setContentText("An error occurred while deleting the transaction: " + e.getMessage());
                errorAlert.showAndWait();
            }
        } else {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("No Selection");
            warningAlert.setContentText("Please select a transaction to delete.");
            warningAlert.showAndWait();
        }
    }

    private void refreshTableView() {
        try {
            List<BloodTransaction> bloodTransactions = bt.getAll();
            ObservableList<BloodTransaction> observableList = FXCollections.observableList(bloodTransactions);
            ShowTransaction.setItems(observableList);
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error Refreshing TableView");
            errorAlert.setContentText("An error occurred while refreshing the TableView: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    void UpdateTransaction(ActionEvent event) {
        BloodTransaction selectedTransaction = ShowTransaction.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddTransaction.fxml"));
                Parent root = loader.load();
                AddTransaction controller = loader.getController();
                controller.populateFields(selectedTransaction);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please select a transaction to update.");
            alert.showAndWait();
        }
    }
}
