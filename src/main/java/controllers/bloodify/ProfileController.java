package controllers.bloodify;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.user.User;
import Utils.SessionManager;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import services.UserService;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.animation.FadeTransition;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ProfileController implements Initializable {

    @FXML
    private TextField first_name;

    @FXML
    private TextField last_name;

    @FXML
    private TextField email;
    @FXML
    private Button passwordbtn ;
    @FXML
    private Button logoutbtn ;
    @FXML
    private Button homebtn;
    @FXML
    private Button deletebtn;
    @FXML
    private AnchorPane messagebar;
    private Timeline messageTimer;
    // Optional: You can create fields to hold the user information
    private User currentUser;
    private UserService userService = new UserService(); // Instantiate your UserService
    @FXML
    private Label messageLabel;

    @FXML
    private StackPane messageBarContainer; // The container for the message bar



    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    protected void onSaveButtonClick() {
        // Retrieve the updated user information from input fields
        String updatedFirstName = first_name.getText();
        String updatedLastName = last_name.getText();
        String updatedEmail = email.getText();

        // Check if any of the input fields are empty
        if (updatedFirstName.isEmpty() || updatedLastName.isEmpty() || updatedEmail.isEmpty()) {
            // Display an error message if any field is empty
            showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill in all fields.");
            return;
        }

        // Get the current user from the SessionManager
        currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            if (userService.emailExists(updatedEmail) && !currentUser.getEmail().equals(updatedEmail)) {
                email.getStyleClass().add("text-field-error");
                showAlert(Alert.AlertType.ERROR, "Error", null, "A user with this email already exists.");
                return;
            }
            if (!isValidEmail(updatedEmail)) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Please enter a valid email address.");
                return;


             }
            else {
                // Update the user's information
                currentUser.setFirstName(updatedFirstName);
                currentUser.setLastName(updatedLastName);
                currentUser.setEmail(updatedEmail);
                currentUser.setId(SessionManager.getInstance().getUserId());

                // Call the updateUser method in your UserService to update the user information in the database
                userService.updateUser(currentUser);
                email.getStyleClass().clear();
                email.getStyleClass().add("text-input");
                messagebar.setVisible(true); // Show the message bar
                messageTimer.playFromStart(); // Start the timer
            }
        } else {
            // Handle the case where no user is currently logged in
            System.out.println("No user is currently logged in.");
        }
    }


    @Override

    public void initialize(URL url, ResourceBundle rb) {
        // Retrieve the current user from the session manager
        currentUser = SessionManager.getInstance().getCurrentUser();
        messagebar.setVisible(false);
        messageTimer = new Timeline(new KeyFrame(Duration.seconds(5), event -> messagebar.setVisible(false)));
        messageTimer.setCycleCount(1); // Run only once


        // Check if a user is logged in
        if (currentUser != null) {
            // Set the text of text fields to corresponding values from the current user
            first_name.setText(currentUser.getFirstName());
            last_name.setText(currentUser.getLastName());
            email.setText(currentUser.getEmail());

        } else {
            System.out.println("No user is currently logged in.");
        }
    }


    // Optional: Create a method to set the user information externally
    public void setUser(User user) {
        // Set the user information in the controller
        this.currentUser = user;
        // Update UI components with user information if needed
        if (user != null) {
            first_name.setText(currentUser.getFirstName());
            last_name.setText(currentUser.getLastName());
            email.setText(currentUser.getEmail());
        }
    }

    // Optional: Create a method to get the current user
    public User getUser() {
        return currentUser;
    }
    @FXML
    public void passwordchange() {
        try {
            // Load the sign-up window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/password.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Edit password ");
            stage.setScene(new Scene(root));

            // Apply the transition animation
            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) passwordbtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    @FXML
    private void handleLogout() {
        // Clean the user session
        SessionManager.getInstance().cleanUserSession();

        // Access the user ID through the SessionManager
        int userId = SessionManager.getInstance().getUserId();
        System.out.println("User session connect is " + userId);
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


        // Add code to navigate to the login screen or perform any additional tasks after logout
    }
    @FXML
    public void onhome() {
        String fxmlFile;

        try {
            if (currentUser.getRoles().contains("\"ROLE_ADMIN\"")) {
                fxmlFile = "/fxml/bloodify/admin.fxml"; // Path to your admin FXML file
            } else {
                fxmlFile = "/fxml/bloodify/home.fxml"; // Path to your home page FXML file
            }
            // Load the profile window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle(currentUser.getRoles().contains("\"ROLE_ADMIN\"") ? "Admin Page" : "Home Page"); // Set title based on role

            stage.setScene(new Scene(root));

            // Apply the transition animatio

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) homebtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    @FXML
    public void deleteaccount(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete your account?");

        Optional<ButtonType> action = alert.showAndWait();

        // If the user confirms the deletion
        if (action.isPresent() && action.get() == ButtonType.OK) {
            int userId = SessionManager.getInstance().getUserId(); // Get current user ID
            userService.deleteUser(userId); // Delete the user account
            SessionManager.getInstance().cleanUserSession(); // Clean user session

            redirectToLogin(); // Method to redirect to the login page
        }

    }
    private void redirectToLogin() {
        try {
            // Load the login page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/login.fxml")); // Adjust path as necessary
            Parent root = loader.load();

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) deletebtn.getScene().getWindow(); // Assuming 'table' is a component in your current scene
//            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pd2.png"))); // Adjust the path as necessary
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, could log error or show an error message
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
