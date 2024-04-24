package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.BloodStock;
import tn.esprit.services.BloodStockService;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ClientInfoStatus;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class AddStock  implements Initializable {
    @FXML
    private DatePicker dateTF;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField quantityTF;

    @FXML
    private TextField typeTF;

    @FXML
    private Label nameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label dateLabel;


    private final BloodStockService bs = new BloodStockService();

    private BloodStock selectedStockToUpdate;
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


        quantityTF.setTextFormatter(textFormatter);}

    public void populateFields(BloodStock stock) {
        selectedStockToUpdate = stock;
        nameTF.setText(stock.getName());
        quantityTF.setText(String.valueOf(stock.getQuantityAvailable()));
        typeTF.setText(stock.getBloodType());
        dateTF.setValue(stock.getExpiryDate());
    }
    @FXML
    void Add(ActionEvent event) {
        if (isValidInput()) {
            try {
                if (selectedStockToUpdate != null) {
                    selectedStockToUpdate.setName(nameTF.getText());
                    selectedStockToUpdate.setQuantityAvailable(Float.parseFloat(quantityTF.getText()));
                    selectedStockToUpdate.setBloodType(typeTF.getText());
                    selectedStockToUpdate.setExpiryDate(dateTF.getValue());
                    bs.update(selectedStockToUpdate);


                } else {

                    bs.add(new BloodStock(nameTF.getText(), Float.parseFloat(quantityTF.getText()), typeTF.getText(), dateTF.getValue()));
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
    void show(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowStock.fxml"));
            nameTF.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    private boolean isValidInput() {
        boolean isValid = true;

        if (nameTF.getText().isEmpty()) {
            nameLabel.setText("Name requested!");
            isValid = false;
        } else {
            nameLabel.setText("");
        }

        if (quantityTF.getText().isEmpty()) {
            quantityLabel.setText("Quantity requested!");
            isValid = false;
        } else {
            quantityLabel.setText("");
        }

        if (typeTF.getText().isEmpty()) {
            typeLabel.setText("Type requested!");
            isValid = false;
        } else {
            typeLabel.setText("");
        }

        if (dateTF.getValue() == null) {
            dateLabel.setText("Date requested!");
            isValid = false;
        } else {
            dateLabel.setText("");
        }

        return isValid;
    }




}

