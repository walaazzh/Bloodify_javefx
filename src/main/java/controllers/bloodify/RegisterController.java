package controllers.bloodify;

import Utils.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.user.User;
import javafx.scene.control.ProgressBar;

import services.UserService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterController {
    @FXML
    private TextField first_name;

    @FXML
    private TextField last_name;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirm_password;

    @FXML
    private Hyperlink signin;

    @FXML
    private ProgressBar progressbar;
    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;
    @FXML
    private TextField showpassword;

    @FXML
    private TextField showconfirmpassword;

    private UserService userService = new UserService(); // Instance of UserService
    private boolean imageSwitch1 = true;
    private boolean imageSwitch2 = true;



    @FXML
    public void initialize() {
        // Bind the progress of the ProgressBar to the password strength
        showpassword.setManaged(false);
        showpassword.setVisible(false);

        showconfirmpassword.setManaged(false);
        showconfirmpassword.setVisible(false);
        password.textProperty().bindBidirectional(showpassword.textProperty());
        confirm_password.textProperty().bindBidirectional(showconfirmpassword.textProperty());



        password.textProperty().addListener((obs, oldVal, newVal) -> updatePasswordStrength(newVal));
//        String siteKey = "6LesK4gpAAAAALosDjeMzwqyYcj_4G76QX0a3HdG"; // Replace with your actual Site Key
//        String reCAPTCHAUrl = "https://www.google.com/recaptcha/v2/checkbox?render=" + siteKey;
//        recaptchaWebView.getEngine().load(reCAPTCHAUrl);

    }

    @FXML
    private void togglePasswordVisibility() {
        if (imageSwitch1) {
            // Show the password
            image1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/invisible.png"))));
            showPassword(true, password, showpassword);
        } else {
            // Hide the password
            image1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/visible.png"))));
            showPassword(false, password, showpassword);
        }
        imageSwitch1 = !imageSwitch1;
    }

    @FXML
    private void toggleConfirmPasswordVisibility() {
        if (imageSwitch2) {
            // Show the confirm password
            image2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/invisible.png"))));
            showPassword(true, confirm_password, showconfirmpassword);
        } else {
            // Hide the confirm password
            image2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/visible.png"))));
            showPassword(false, confirm_password, showconfirmpassword);
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

    private void updatePasswordStrength(String password) {
        // Calculate the strength of the password (you can implement your own logic here)
        double strength = calculatePasswordStrength(password);

        // Set the progress of the ProgressBar based on the strength
        progressbar.setProgress(strength);

        // Customize the style of the ProgressBar based on the strength
        if (strength < 0.3) {
            progressbar.getStyleClass().clear();
            progressbar.getStyleClass().add("progress-bar");
            progressbar.getStyleClass().add("progress-bar-weak");
        } else if (strength < 0.7) {
            progressbar.getStyleClass().clear();
            progressbar.getStyleClass().add("progress-bar");
            progressbar.getStyleClass().add("progress-bar-medium");
        } else if(strength <= 0.9) {
            progressbar.getStyleClass().clear();
            progressbar.getStyleClass().add("progress-bar");
            progressbar.getStyleClass().add("progress-bar-good");
        }
        else {progressbar.getStyleClass().clear();
            progressbar.getStyleClass().add("progress-bar");
            progressbar.getStyleClass().add("progress-bar-strong");}
    }

    // Implement your own logic to calculate the password strength
    private double calculatePasswordStrength(String password) {
        // Check if the password meets the minimum length requirement
        if (password.isEmpty()) {
            return 0.0; // Return zero strength if the password is too short
        }
        if (password.length() < 3 ) {
            return 0.1; // Return zero strength if the password is too short
        }
        if (password.length() < 8) {
            return 0.2; // Return zero strength if the password is too short
        }

        // Initialize counters for different types of characters
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // Define the set of special characters
        String specialChars = "!@#$%^&*()-_=+[{]};:'\"|,<.>/?";

        // Loop through each character in the password
        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (specialChars.contains(String.valueOf(ch))) {
                hasSpecialChar = true;
            }
        }

        // Calculate the strength based on the presence of different types of characters
        int strength = 0;
        if (hasLowercase) strength++;
        if (hasUppercase) strength++;
        if (hasDigit) strength++;
        if (hasSpecialChar) strength++;

        // Return the strength as a percentage of the maximum possible strength
        return (double) strength / 4.0;
    }

    @FXML
    void onAjoute(ActionEvent event) {
        // Retrieve user input from text fields
        String firstName = first_name.getText().trim();
        String lastName = last_name.getText().trim();
        String userEmail = email.getText().trim();
        String userPassword = password.getText();
        String confirmPassword = confirm_password.getText();

        // Validate that no field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill in all fields.");
            return;
        }

        // Validate email format
        if (!isValidEmail(userEmail)) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Please enter a valid email address.");
            return;
        }

        // Check if the email already exists
        if (userService.emailExists(userEmail)) {
            email.getStyleClass().add("text-field-error");
            showAlert(Alert.AlertType.ERROR, "Error", null, "A user with this email already exists.");
            return;
        }
        if (!userService.emailExists(userEmail)) {
            email.getStyleClass().clear();
            email.getStyleClass().add("text-input");
        }

        // Validate password length
        if (userPassword.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Password must be at least 8 characters long.");
            return;
        }

        // Validate password match
        if (!userPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Passwords do not match.");
            return;
        }

        // Set default role
        String defaultRole = "[\"ROLE_USER\"]";

        User user = new User(userEmail, firstName, lastName, userPassword, defaultRole);

        // Insert user into database
        userService.insertUserPst(user);

        try {
            // Authenticate the new user and set as current user
            user = SessionManager.getInstance().auth(userEmail, userPassword);

            // Redirect to the home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/home.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Home Page");

            // Set the scene
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Close the current stage
            Stage currentStage = (Stage) first_name.getScene().getWindow();
            currentStage.close();

            // Show the new stage
            stage.show();
        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Error while connecting.");
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    @FXML
    public void signinOn() {
        try {
            // Load the sign-up window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/login.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Login in ");
            stage.setScene(new Scene(root));

            // Get the reference to the current window
            Stage currentStage = (Stage) signin.getScene().getWindow();
            currentStage.close();
            stage.show();
        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to load sign-up window.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
