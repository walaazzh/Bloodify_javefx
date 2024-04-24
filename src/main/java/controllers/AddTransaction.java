package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import tn.esprit.models.BloodStock;
import tn.esprit.models.BloodTransaction;
import tn.esprit.services.BloodStockService;
import tn.esprit.services.BloodTransactionService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class AddTransaction implements Initializable {

    @FXML
    private DatePicker donationDateTF;

    @FXML
    private TextField quantityDonatedTF;

    @FXML
    private TextField transactionTypeTF;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label typeLabel;
    private final BloodTransactionService bt = new BloodTransactionService();

    private BloodTransaction selectedTransactionToUpdate;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };


        TextFormatter<String> textFormatter = new TextFormatter<>(filter);


        quantityDonatedTF.setTextFormatter(textFormatter);}

    public void populateFields(BloodTransaction transaction) {
        selectedTransactionToUpdate = transaction;
        quantityDonatedTF.setText(String.valueOf(transaction.getQuantityDonated()));
        transactionTypeTF.setText(transaction.getTransactionType());
        donationDateTF.setValue(transaction.getDonationDate());
    }

    @FXML
    void AddTransaction(ActionEvent event) {
        if (isValidInput()) {
            try {
                if (selectedTransactionToUpdate != null) {
                    selectedTransactionToUpdate.setQuantityDonated(Float.parseFloat(quantityDonatedTF.getText()));
                    selectedTransactionToUpdate.setTransactionType(transactionTypeTF.getText());
                    selectedTransactionToUpdate.setDonationDate(donationDateTF.getValue());
                    bt.update(selectedTransactionToUpdate);


                } else {

                    bt.add(new BloodTransaction(Float.parseFloat(quantityDonatedTF.getText()), transactionTypeTF.getText(), donationDateTF.getValue()));
                }

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }

    }

    @FXML
    void ShowTransaction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowTransaction.fxml"));
            transactionTypeTF.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private boolean isValidInput() {
        boolean isValid = true;

        if (quantityDonatedTF.getText().isEmpty()) {
            quantityLabel.setText("Quantity requested!");
            isValid = false;
        } else {
            quantityLabel.setText("");
        }

        if (transactionTypeTF.getText().isEmpty()) {
            typeLabel.setText("Transaction type requested!");
            isValid = false;
        } else {
            typeLabel.setText("");
        }

        if (donationDateTF.getValue()== null) {
            dateLabel.setText("Date requested!");
            isValid = false;
        } else {
            dateLabel.setText("");
        }



        return isValid;
    }

}
