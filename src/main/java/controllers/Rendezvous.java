package controllers;

import entities.centredecollect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import services.ServiceRendezvous;
import java.text.SimpleDateFormat;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import java.util.Date;
import java.util.List;

public class Rendezvous {
    private final ServiceRendezvous serviceRendezvous = new ServiceRendezvous();

    @FXML
    private Button cancelbutton;

    @FXML
    private TextField dateField;

    @FXML
    private TextField enteremailField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button savebutton;



    @FXML
    private ChoiceBox<String> typeField;

    @FXML
    private ChoiceBox<String> timefield;


    int centreId;

    @FXML
    void initialize() {
        // Liste des types de groupe sanguin
        List<String> bloodTypes = Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        List<String> heure = Arrays.asList("", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

        // Ajout des options au ChoiceBox
        typeField.getItems().addAll(bloodTypes);
        timefield.getItems().addAll(heure);
    }

    @FXML
    void ajouter(ActionEvent event) {
        try {
            // Récupérer les données saisies par l'utilisateur
            String name = nameTextField.getText();
            String email = enteremailField.getText();
            String dateText = dateField.getText();
            String heure = timefield.getValue();
            String bloodType = typeField.getValue();


            // Validation des données
            if (name.isEmpty() || email.isEmpty() || dateText.isEmpty() || heure == null || bloodType == null) {
                // Afficher un message d'erreur si des champs obligatoires sont vides
                System.out.println("Veuillez remplir tous les champs.");
                return;
            }
            System.out.println(name);
            System.out.println(email);
            System.out.println(dateText);
            System.out.println(heure);
            System.out.println(bloodType);
            System.out.println(this.centreId);

            entities.Rendezvous RDV=new entities.Rendezvous(dateText,heure,name,email,bloodType,this.centreId);
            serviceRendezvous.ajouter(RDV);
            // Afficher un message de succès
            System.out.println("Rendez-vous ajouté avec succès !");
        }  catch (Exception e) {
            // Gérer les exceptions liées à la base de données
            System.out.println("Erreur lors de l'ajout du rendez-vous : " + e.getMessage());
        }
    }

    @FXML
    void annuler(ActionEvent event) {
        // Effacer le contenu des champs de saisie
        nameTextField.clear();
        enteremailField.clear();
        dateField.clear();
        timefield.getSelectionModel().clearSelection();
        typeField.getSelectionModel().clearSelection();
    }

    void setCenter(int centreId){

        this.centreId = centreId;


    }
}
