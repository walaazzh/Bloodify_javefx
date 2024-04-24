package controllers;

import entities.centredecollect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceRendezvous;
import services.Servicecentrededecollect;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListeDeCentre implements Initializable {

    @FXML
    private Label userlabel;

    @FXML
    private GridPane grid;

    private List<centredecollect> centres;

    private final Servicecentrededecollect service = new Servicecentrededecollect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int columns=0;
        int rows=1;

        // Utilisez la variable de classe centres plutôt que de déclarer une nouvelle variable
        centres = getCentres();
        try {





            for (int i = 0; i < centres.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/exempleCentre.fxml"));
                VBox vbox = fxmlLoader.load();
                exempleCentreCollecte exempleCentreCollecte = fxmlLoader.getController();
                exempleCentreCollecte.setData(centres.get(i));



                if (columns==3){

                    columns=0;
                    rows++;
                }

                // Ajouter le VBox dans le GridPane
                grid.add( vbox, columns++, rows);

                // Ajouter des marges au VBox
                GridPane.setMargin( vbox, new Insets(15,15,15,15));
            }




            // Passer à la prochaine colonne ou ligne si nécessaire
            if (columns == 2) {
                columns = 0;
                rows++;
            }


        }
        catch (Exception e) {e.printStackTrace();}
    }

    private List<centredecollect> getCentres() {
        try {
            return service.afficher();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retourne null en cas d'erreur
        }
    }

    public void getAppointments(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeRendezvous.fxml"));
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

    public void addCenter(ActionEvent actionEvent) {
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
}