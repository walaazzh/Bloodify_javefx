package controllers;

import entities.centredecollect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.Servicecentrededecollect;

import java.io.IOException;
import java.sql.SQLException;

public class admin {

        @FXML
        private TextField captextfield;

        @FXML
        private ChoiceBox<String> citytxt; // Modifier le type de ChoiceBox pour qu'il corresponde au type de données que vous voulez afficher.

        @FXML
        private TextField codetxt;

        @FXML
        private TextField nomtxt;

        @FXML
        private Label userlabel;

        @FXML
        private Label validationName;

        private final Servicecentrededecollect service = new Servicecentrededecollect();

        @FXML
        void ajouter(ActionEvent event) {

                try {
                        System.out.println("dkhalet");
                        // Récupérer les données saisies par l'utilisateur
                        int codePostal = Integer.parseInt(codetxt.getText());
                        int capaciteMax = Integer.parseInt(captextfield.getText());
                        String name = nomtxt.getText();
                        String ville = citytxt.getValue(); // Récupérer la valeur sélectionnée dans le ChoiceBox.

                        // Créer un objet CentreCollecte avec les données saisies
                        centredecollect centreCollecte = new centredecollect(0, codePostal, capaciteMax, name, ville);

                        // Ajouter le centre de collecte à la base de données en utilisant le service approprié
                        service.ajouter(centreCollecte);

                        // Afficher un message de succès
                        System.out.println("Centre de collecte ajouté avec succès !");
                }  catch (Exception e) {
                        // Gérer les exceptions liées à la base de données
                        System.out.println("Erreur lors de l'ajout du centre de collecte : " + e.getMessage());
                }
        }



        @FXML
        void initialize() {
                // Créer une liste observable contenant les options de ville
                ObservableList<String> cities = FXCollections.observableArrayList(
                        "Ariana",
                        "Ben Arous",
                        "Beja"
                        // Ajoutez autant de villes que nécessaire
                );

                validationName.setText("hello");

                // Ajouter la liste des villes au ChoiceBox
                citytxt.setItems(cities);

                // Sélectionner la première ville par défaut
                citytxt.getSelectionModel().selectFirst();


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
}