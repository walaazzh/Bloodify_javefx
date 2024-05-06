package controllers.bloodify;


import Utils.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.user.User;
import services.UserService;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.scene.layout.AnchorPane;

public class ResetPasswordController implements Initializable {
    @FXML
    private AnchorPane anchor1;
    @FXML
    private AnchorPane anchor2;
    @FXML
    private TextField codetext;
    @FXML
    private PasswordField password;
    @FXML
    private ProgressBar progressbar;

    @FXML
    private PasswordField confirm_password;
    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;
    @FXML
    private TextField showpassword;

    @FXML
    private TextField showconfirmpassword;

    @FXML
    private Button login;
    private boolean imageSwitch1 = true;
    private boolean imageSwitch2 = true;

    private UserService userService = new UserService(); // Instance of UserService
    private String userEmail;

    // Getter method for the email variable
    public String getUserEmail() {
        return userEmail;
    }

    // Setter method for the email variable
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // Allow only digits (0-9)
                return change;
            }
            return null; // Reject any change that is not a digit
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        codetext.setTextFormatter(textFormatter);
        anchor2.setVisible(false);
        password.textProperty().addListener((obs, oldVal, newVal) -> updatePasswordStrength(newVal));
        showpassword.setManaged(false);
        showpassword.setVisible(false);

        showconfirmpassword.setManaged(false);
        showconfirmpassword.setVisible(false);
        password.textProperty().bindBidirectional(showpassword.textProperty());
        confirm_password.textProperty().bindBidirectional(showconfirmpassword.textProperty());
    }
    private void updatePasswordStrength(String password) {
        double strength = calculatePasswordStrength(password);
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

    @FXML
    protected void onSubmitClick() {
        String code = codetext.getText();
        String verify = userService.findResetTokenByEmail(userEmail);
        System.out.println("this is user email " + userEmail);

        // Check if the code matches the verification code
        if (verify != null && verify.equals(code)) {
            anchor2.setVisible(true);
            anchor1.setVisible(false);
        }
        else {
            showAlert(Alert.AlertType.ERROR, "Invalid Code", "Error", "The reset code is incorrect.");
        }
    }

    @FXML
    void onChange(ActionEvent event){

        String userPassword = password.getText();
        String confirmPassword = confirm_password.getText();
        double strength = calculatePasswordStrength(userPassword);

        if (strength < 0.7) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Password must be at least 8 characters long, include at least one number, one special character, and one uppercase letter.");
            return;
        }

        // Validate password match
        if (!userPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Passwords do not match.");
            return;
        }
        userService.resetPassword(userEmail,userPassword);
        try {
            // Load the sign-up window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/login.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Login in ");
            stage.setScene(new Scene(root));

            // Get the reference to the current window
            Stage currentStage = (Stage) anchor1.getScene().getWindow();

            // Close the current window
            currentStage.close();

            // Show the new stage
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
    void loginon() throws IOException {

        // Load the sign-up window FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/login.fxml"));
        Parent root = loader.load();

        // Create a new stage
        Stage stage = new Stage();
        stage.setTitle("Log in");
        stage.setScene(new Scene(root));

        // Apply the transition animation
        LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);

        // Show the new stage
        stage.show();

        // Get the reference to the current window and close it
        Stage currentStage = (Stage) login.getScene().getWindow();
        currentStage.close();



    }

}