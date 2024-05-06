package controllers.bloodify.events;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import models.user.Event;
import services.CategoryService;
import services.EventService;
import javafx.scene.control.Label;

import java.io.File;
import java.sql.SQLException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
public class Addevent {

    private final CategoryService categoryService = new CategoryService();
    private final EventService eventService = new EventService();
    // Field to store the event being updated
    private Event eventToUpdate;

    @FXML
    private ImageView imageview;
    @FXML
    private Button addeventbtn;

    @FXML
    private ComboBox<String> categoryde;

    @FXML
    private TextField contactde;

    @FXML
    private DatePicker datedebutpicker;

    @FXML
    private DatePicker datefinpicker;

    @FXML
    private TextArea descriptionde;

    @FXML
    private TextField maxparticipantde;

    @FXML
    private TextField namede;

    @FXML
    private TextField organizede;

    @FXML
    private TextField statusde;
    @FXML
    private Label file;



    @FXML
    void initialize() {
        initializeCategoryComboBox();


    }

/*
    @FXML
    void addeventaction(ActionEvent event) {
        try {
            // Retrieve field values
            String name = namede.getText().trim();
            String description = descriptionde.getText().trim();
            LocalDate beginDate = datedebutpicker.getValue();
            LocalDate endDate = datefinpicker.getValue();
            String organisateur = organizede.getText().trim();
            String contact = contactde.getText().trim();
            String status = statusde.getText().trim();
            int maxParticipant = Integer.parseInt(maxparticipantde.getText().trim());
            String image = file.getText().trim();

            // Check for empty fields
            if (name.isEmpty() || description.isEmpty() || organisateur.isEmpty() || contact.isEmpty() || status.isEmpty() || image.isEmpty() || beginDate == null || endDate == null) {
                showAlert("Error", "Please fill in all fields", "All fields are required.");
                return;
            }

            // Check if begin date is after end date
            if (beginDate.isAfter(endDate)) {
                showAlert("Error", "Invalid Date Range", "Begin date must be before end date.");
                return;
            }

            // Check if contact field is in email format
            if (!isValidEmail(contact)) {
                showAlert("Error", "Invalid Contact Email", "Please enter a valid email address for the contact.");
                return;
            }

            String categoryName = categoryde.getValue(); // Retrieve the selected category name
            int categoryId = categoryService.getCategoryIdByName(categoryName);

            if (eventToUpdate == null) {
                // If eventToUpdate is null, it means we're adding a new event
                // Create a new event object and add it to the database
                Event newEvent = new Event(categoryId, name, description, beginDate.toString(), endDate.toString(), organisateur, contact, status, maxParticipant, image);
                eventService.create(newEvent);
                showInformationAlert("Success", "Event added successfully");
            } else {
                // If eventToUpdate is not null, it means we're updating an existing event
                // Update the existing event object with the new data and update it in the database
                eventToUpdate.setIdcategory(categoryId);
                eventToUpdate.setName(name);
                eventToUpdate.setDescription(description);
                eventToUpdate.setDatedebut(beginDate.toString());
                eventToUpdate.setDatefin(endDate.toString());
                eventToUpdate.setOrganisateur(organisateur);
                eventToUpdate.setContact(contact);
                eventToUpdate.setStatus(status);
                eventToUpdate.setMaxParticipant(maxParticipant);
                eventToUpdate.setImage(image);

                eventService.update(eventToUpdate);
                showInformationAlert("Success", "Event updated successfully");
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to process event", e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        // Regular expression for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    */
@FXML
void addeventaction(ActionEvent event) {
    try {
        // Retrieve field values
        String name = namede.getText().trim();
        String description = descriptionde.getText().trim();
        String datedebut = datedebutpicker.getValue().toString();
        String datefin = datefinpicker.getValue().toString();
        String organisateur = organizede.getText().trim();
        String contact = contactde.getText().trim();
        String status = statusde.getText().trim();
        int maxParticipant = Integer.parseInt(maxparticipantde.getText().trim());
        String image = file.getText().trim();

        if (name.isEmpty() || description.isEmpty() || organisateur.isEmpty() || contact.isEmpty() || status.isEmpty() || image.isEmpty() || datedebut.isEmpty() || datefin.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill in all fields.");
            return;
        }

        if (!isValidEmail(contact)) {
            showAlert("Error", "Invalid Contact Email", "Please enter a valid email address for the contact.");
            return;
        }


        String categoryName = categoryde.getValue(); // Retrieve the selected category name
        int categoryId = categoryService.getCategoryIdByName(categoryName);


        if (eventToUpdate == null) {
            // If eventToUpdate is null, it means we're adding a new event
            // Create a new event object and add it to the database
            Event newEvent = new Event(categoryId, name, description, datedebut, datefin, organisateur, contact, status, maxParticipant, image);
            eventService.create(newEvent);
            showInformationAlert("Success", "Event added successfully");
        } else {
            // If eventToUpdate is not null, it means we're updating an existing event
            // Update the existing event object with the new data and update it in the database
            eventToUpdate.setIdcategory(categoryId);
            eventToUpdate.setName(name);
            eventToUpdate.setDescription(description);
            eventToUpdate.setDatedebut(datedebut);
            eventToUpdate.setDatefin(datefin);
            eventToUpdate.setOrganisateur(organisateur);
            eventToUpdate.setContact(contact);
            eventToUpdate.setStatus(status);
            eventToUpdate.setMaxParticipant(maxParticipant);
            eventToUpdate.setImage(image);

            eventService.update(eventToUpdate);
            showInformationAlert("Success", "Event updated successfully");
        }
    } catch (SQLException e) {
        showAlert("Error", "Failed to process event", e.getMessage());
    }
}
    private boolean isValidEmail(String email) {
        // Regular expression for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    @FXML
    void uploadaction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);


        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

            Image image = new Image(selectedFile.toURI().toString());
            imageview.setImage(image);
            file.setText(selectedFile.getAbsolutePath());
        }

    }
    @FXML
    void closewindow(ActionEvent event) {
        try {
            Button button = (Button) event.getSource();
            Scene scene = button.getScene();
            Parent root = FXMLLoader.load(getClass().getResource("/showevent.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void initializeCategoryComboBox() {
        try {
            List<String> categoryNames = categoryService.getAllCategoryNames();
            categoryde.getItems().addAll(categoryNames);

            categoryde.setOnAction(e -> {
                String selectedCategory = categoryde.getSelectionModel().getSelectedItem();
                try {
                    int categoryId = categoryService.getCategoryIdByName(selectedCategory);
                } catch (SQLException ex) {
                    showAlert("Error", "Error Getting Category ID", ex.getMessage());
                }
            });
        } catch (SQLException ex) {
            showAlert("Error", "Error Loading Categories", ex.getMessage());
        }
    }



    public void initDataForUpdate(Event event) {
        eventToUpdate = event;

        // Populate form fields with event data
        namede.setText(event.getName());
        descriptionde.setText(event.getDescription());
        contactde.setText(event.getContact());
        if (event.getDatedebut() != null) {
            datedebutpicker.setValue(LocalDate.parse(event.getDatedebut()));
        }
        if (event.getDatefin() != null) {
            datefinpicker.setValue(LocalDate.parse(event.getDatefin()));
        }
        organizede.setText(event.getOrganisateur());
        statusde.setText(event.getStatus());
        maxparticipantde.setText(String.valueOf(event.getMaxParticipant()));
        file.setText(event.getImage());
        Image image = new Image(new File(event.getImage()).toURI().toString());
        imageview.setImage(image);
    }




    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

