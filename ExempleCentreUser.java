package controllers;

import entities.centredecollect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.Servicecentrededecollect;

import java.io.IOException;
import java.sql.SQLException;

public class ExempleCentreUser {

    @FXML
    private Label capacitytxt;

    @FXML
    private Label citytxt;

    @FXML
    private Label nametxt;

    private int centreId;

    private final Servicecentrededecollect service = new Servicecentrededecollect();


    private final ListeDeCentre centresController = new ListeDeCentre();






    void setData(centredecollect centre){

        nametxt.setText(centre.getName());
        citytxt.setText(centre.getVille());
        capacitytxt.setText(String.valueOf(centre.getCapaciteMax()));
        this.centreId = centre.getId();


    }



    @FXML
    public void updateTxt(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin.fxml"));
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

    @FXML
    void deletetxt(ActionEvent event) throws SQLException {

        service.supprimer(this.centreId);

        refreshPage(event);

    }


    void refreshPage(ActionEvent event){

        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeDeCentre.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle depuis l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createApp(ActionEvent actionEvent) {

        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rendezvous.fxml"));
            Parent root = loader.load();
            Rendezvous RDV = loader.getController();
            RDV.setCenter(this.centreId);

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
}
