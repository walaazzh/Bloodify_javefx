package controllers.bloodify;

import Utils.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import services.UserService;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import models.user.User;
import javafx.scene.text.Text;
import services.UserService;
import javafx.scene.control.Alert;


import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class PasswordController {
    @FXML
    private PasswordField current_password;
    @FXML
    private PasswordField new_password;
    @FXML
    private PasswordField confirm_new_password;
    @FXML
    private Label invalid_password;
    @FXML
    private Label length_label;
    @FXML
    private Label upper_case_label;
    @FXML
    private Label lower_case_label;
    @FXML
    private Label spetial_label;
    @FXML
    private Button profilebtn ;
    @FXML
    private Button logoutbtn ;
    @FXML
    private Button homebtn;
    @FXML
    private Button deletebtn;
    @FXML
    private AnchorPane messagebar;
    private Timeline messageTimer;
    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;
    @FXML
    private TextField newtext;

    @FXML
    private TextField confirmnewtext;
    @FXML
    private TextField currenttext;
    private boolean imageSwitch1 = true;
    private boolean imageSwitch2 = true;

    private boolean imageSwitch3 = true;

    private UserService userService = new UserService();
    private User currentUser;
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Success");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Load custom CSS for the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/Styles/profile.css").toExternalForm());

        // Show the alert and wait for user response
        alert.showAndWait();
    }



    public void initialize() {
        // Add listener to new password field to monitor changes in text
        messagebar.setVisible(false);
        messageTimer = new Timeline(new KeyFrame(Duration.seconds(5), event -> messagebar.setVisible(false)));
        messageTimer.setCycleCount(1); // Run only once
        length_label.setVisible(false);
        upper_case_label.setVisible(false);
        lower_case_label.setVisible(false);
        spetial_label.setVisible(false);
        newtext.setManaged(false);
        confirmnewtext.setVisible(false);
        currenttext.setVisible(false);

        newtext.setManaged(false);
        confirmnewtext.setVisible(false);
        currenttext.setVisible(false);
        new_password.textProperty().bindBidirectional(newtext.textProperty());
        confirm_new_password.textProperty().bindBidirectional(confirmnewtext.textProperty());
        current_password.textProperty().bindBidirectional(currenttext.textProperty());

        new_password.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if new password is not null or empty
            if (newValue != null && !newValue.isEmpty()) {
                // If new password is not null or empty, make labels visible
                length_label.setVisible(true);
                upper_case_label.setVisible(true);
                lower_case_label.setVisible(true);
                spetial_label.setVisible(true);
            } else {
                // If new password is null or empty, make labels invisible
                spetial_label.setVisible(false);
                length_label.setVisible(false);
                upper_case_label.setVisible(false);
                lower_case_label.setVisible(false);
            }
        });
        new_password.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validatePassword(newValue);
            }
        });
    }
    @FXML
    private void toggleCurrentPasswordVisibility() {
        if (imageSwitch1) {
            // Show the password
            image1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/invisible.png"))));
            showPassword(true, current_password, currenttext);
        } else {
            // Hide the password
            image1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/visible.png"))));
            showPassword(false, current_password, currenttext);
        }
        imageSwitch1 = !imageSwitch1;
    }

    @FXML
    private void toggleConfirmPasswordVisibility() {
        if (imageSwitch3) {
            // Show the confirm password
            image3.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/invisible.png"))));
            showPassword(true, confirm_new_password, confirmnewtext);
        } else {
            // Hide the confirm password
            image3.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/visible.png"))));
            showPassword(false, confirm_new_password, confirmnewtext);
        }
        imageSwitch3 = !imageSwitch3;
    }
    @FXML
    private void toggleNewPasswordVisibility() {
        if (imageSwitch2) {
            // Show the confirm password
            image2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/invisible.png"))));
            showPassword(true, new_password, newtext);
        } else {
            // Hide the confirm password
            image2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/visible.png"))));
            showPassword(false, new_password, newtext);
        }
        imageSwitch2 = !imageSwitch2;
    }
    private void showPassword(boolean show, PasswordField passwordField, TextField textField) {
        if (show) {
            // Copying the password to the TextField to show it
            textField.setText(passwordField.getText());
            passwordField.setVisible(false);
            textField.setVisible(true);
            textField.setManaged(true);
            Platform.runLater(() -> {
                textField.requestFocus();
                textField.positionCaret(textField.getText().length());
            });
        } else {
            // Hiding the TextField and showing the PasswordField
            passwordField.setText(textField.getText());
            textField.setVisible(false);
            textField.setManaged(false);
            passwordField.setVisible(true);
            Platform.runLater(() -> {
                passwordField.requestFocus();
                // Set the caret position to the end of the text
                passwordField.positionCaret(passwordField.getText().length());
            });
        }
    }

    private void validatePassword(String password) {
        // Validate length
        if (password.length() >= 8) {
            length_label.setStyle("-fx-text-fill: green;");
        } else {
            length_label.setStyle("-fx-text-fill: red;");
        }

        // Validate uppercase character
        if (password.matches(".*[A-Z].*")) {
            upper_case_label.setStyle("-fx-text-fill: green;");
        } else {
            upper_case_label.setStyle("-fx-text-fill: red;");
        }

        // Validate lowercase character
        if (password.matches(".*[0-9].*")) {
            lower_case_label.setStyle("-fx-text-fill: green;");
        } else {
            lower_case_label.setStyle("-fx-text-fill: red;");
        }

        // Validate special character
        if (password.matches(".*[^A-Za-z0-9].*")) {
            spetial_label.setStyle("-fx-text-fill: green;");
        } else {
            spetial_label.setStyle("-fx-text-fill: red;");
        }
    }

    // Method to handle the save button click event
    @FXML
    protected void onSaveButtonClick() {
        String currentPassword = current_password.getText();
        String newPassword = new_password.getText();
        String confirmNewPassword = confirm_new_password.getText();
        invalid_password.setText("");

        // Validate the current password against the one stored in the database
        if (!validateCurrentPassword(currentPassword)) {
            invalid_password.setStyle("-fx-text-fill: red;");
            invalid_password.setText("Invalid current password");
            showAlert("Invalid current password", Alert.AlertType.ERROR);
            return;
        }

        // Validate if the new password and confirm password match
        if (!newPassword.equals(confirmNewPassword)) {
            showAlert("Passwords do not match", Alert.AlertType.ERROR);

            return;
        }
        if (!validatePasswordStrength(newPassword)) {
            showAlert("Password must have at least 8 characters, including 1 uppercase letter, 1 lowercase letter, and 1 special character");
            return;
        }


        // Update the password in the database
        int userId = SessionManager.getInstance().getUserId(); // Assuming you have a method to get the current user's ID
        userService.updatePassword(userId, newPassword);
        userService.updatePassword(userId, newPassword);
        messagebar.setVisible(true); // Show the message bar
        messageTimer.playFromStart(); // Start the timer
        current_password.setText("");
        new_password.setText("");
        confirm_new_password.setText("");

    }

    // Method to validate the current password against the one stored in the database
    private boolean validateCurrentPassword(String currentPassword) {
        // Retrieve the user's current password from the database
        currentUser = SessionManager.getInstance().getCurrentUser();
        String storedPassword = currentUser.getPassword();
// You need to implement this method in your UserService class

        // Compare the retrieved password with the one entered by the user
        return storedPassword.equals(currentPassword);
    }
    // Method to update the user's password in the database
    private boolean validatePasswordStrength(String password) {
        // Implement your password strength validation logic here
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[^A-Za-z0-9].*");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    public void editchange() {
        try {
            // Load the sign-up window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/profile.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Edit profile ");
            stage.setScene(new Scene(root));

            // Apply the transition animation
            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) profilebtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    @FXML
    public void onhome() {
        String fxmlFile;
        currentUser = SessionManager.getInstance().getCurrentUser();


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
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception, could log error or show an error message
        }
    }
}
