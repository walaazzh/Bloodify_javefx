package controllers.bloodify.events;

import Utils.SessionManager;
import controllers.bloodify.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.user.Event;
import models.user.Participation;
import services.CategoryService;
import services.EventService;
import services.ParticipationService;

import java.io.IOException;
import java.sql.SQLException;

public class Eventdetails {
    @FXML
    private Hyperlink settingsbtn;
    @FXML
    private Button logoutbtn ;

    @FXML
    private Label categorylabel;
    @FXML
    private Label beginlabel;

    @FXML
    private Label contactlabel;

    @FXML
    private Label descriptionlabel;

    @FXML
    private Label endlabel;

    @FXML
    private Label namelabel;

    @FXML
    private StackedBarChart<?, ?> stackevents;

    @FXML
    private Label username;

    @FXML
    private Label ticketssold;
    @FXML
    private Label ticketsleft;
    private static int eventId;
    private boolean hasTicket = false;

    public static void setEventId(int eventId) {
        Eventdetails.eventId = eventId;
    }

    @FXML
    void initialize() {
        try {
            // Create an instance of EventService
            EventService eventService = new EventService();

            // Fetch event details using the stored event ID
            Event event = eventService.getEventById(eventId);

            if (event != null) {
                // Populate UI elements with event details
                namelabel.setText(event.getName());
                beginlabel.setText(event.getDatedebut());
                endlabel.setText(event.getDatefin());
                contactlabel.setText(event.getContact());
                descriptionlabel.setText(event.getDescription());
                try {
                    CategoryService categoryService = new CategoryService();
                    String categoryName = categoryService.getCategoryName(event.getIdcategory());
                    categorylabel.setText(categoryName);
                } catch (SQLException e) {
                    e.printStackTrace();
                    categorylabel.setText("Unknown Category");
                }
            } else {
                // Handle case where event is not found
                namelabel.setText("Event Not Found");
                // You can clear other labels or show appropriate message
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }



    @FXML

    void getyourticket(ActionEvent event) {
        try {
            // Check if the user has already obtained a ticket for this event
            if (hasTicket) {
                showAlert("Already Obtained", null, "You have already obtained a ticket for this event.");
                return; // Exit the method to prevent further processing
            }

            // Fetch event details using the stored event ID
            EventService eventService = new EventService();
            Event fetchedEvent = eventService.getEventById(eventId); // Renamed variable to avoid conflict

            // Check if the event exists
            if (fetchedEvent != null) {
                // Create an instance of ParticipationService
                ParticipationService participationService = new ParticipationService();

                // Check if the current user has already participated in this event
                if (participationService.hasParticipated(SessionManager.getInstance().getUserId(), eventId)) {
                    showAlert("Already Participated", null, "You have already participated in this event.");
                    return; // Exit the method to prevent further processing
                }

                // Decrease maxParticipant by 1
                fetchedEvent.setMaxParticipant(fetchedEvent.getMaxParticipant() - 1);

                // Update the database with the new maxParticipant count
                eventService.update(fetchedEvent);

                // Update UI with new maxParticipant count and calculate tickets sold and tickets left
                int maxParticipants = fetchedEvent.getMaxParticipant();
                int ticketsSold = fetchedEvent.getMaxParticipant() - maxParticipants;
                int ticketsLeft = maxParticipants - ticketsSold;

                ticketssold.setText(Integer.toString(ticketsSold));
                ticketsleft.setText(Integer.toString(ticketsLeft));

                // Set the flag to true indicating that the user has obtained a ticket
                hasTicket = true;

                // Save participation in the database
                Participation participation = new Participation(eventId, SessionManager.getInstance().getUserId(), 0); // Adjust the third parameter as needed
                participationService.saveParticipation(participation);
            } else {
                showAlert("Event Not Found", null, "The event could not be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
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



    @FXML
    void handleLogout(ActionEvent event) {
        SessionManager.getInstance().cleanUserSession();

        // Access the user ID through the SessionManager
        int userId = SessionManager.getInstance().getUserId();
        try {
            // Load the sign-up window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/login.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("login ");
            stage.setScene(new Scene(root));

            // Apply the transition animation
            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) logoutbtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            e.printStackTrace(); // Handle the exception appropriately
        }

    }

    @FXML
    void oneventpage(ActionEvent event) {
        Node source = (Node) event.getSource();

        // Get the stage
        Stage stage = (Stage) source.getScene().getWindow();

        try {
            // Load the FXML file of the other page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/Eventpage.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML content
            Scene scene = new Scene(root);

            // Set the scene to the stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential exceptions
        }

    }

    @FXML
    void onsettings(ActionEvent event) {
        try {
            // Load the profile window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/profile.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            stage.setScene(new Scene(root));

            // Apply the transition animatio

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) settingsbtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            e.printStackTrace(); // Handle the exception appropriately
        }

    }


}
