package controllers.bloodify;

import javafx.application.Platform;
import javafx.fxml.FXML;
import Utils.MyDataBase;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

import javafx.stage.StageStyle;
import models.user.User;
import Utils.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import services.UserService;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Platform;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController {
    @FXML
    private Label invalid;
    @FXML
    private TextField emailfield;
    @FXML
    private PasswordField passwordfield;
    @FXML
    private Hyperlink create_account;
    @FXML
    private Hyperlink forget_password;
    @FXML
    private Button googleSignInButton;
    @FXML
    private TextField showpassword;
    @FXML
    private CheckBox rememberMeCheckbox;

    private boolean isCaptchaValidated = false;



    @FXML
    private ImageView image;
    private UserService userService = new UserService(); // Instance of UserService
    private boolean imageSwitch = true;



    public static class SceneTransition {
        public static void fadeTransition(Scene scene, Stage stage) {
            // Create a fade transition with a duration of 1.5 seconds
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0), scene.getRoot());

            // Set the from value (fully transparent)
            fadeTransition.setFromValue(0.0);

            // Set the to value (fully opaque)
            fadeTransition.setToValue(1.0);

            // Set action on finished transition
            fadeTransition.setOnFinished(event -> stage.show());

            // Play the fade transition
            fadeTransition.play();
        }
    }
    @FXML
    private void initialize() {
        // Initialize your TextField to mirror the PasswordField and be invisible
        showpassword.setManaged(false);
        showpassword.setVisible(false);

        // Copy text between TextField and PasswordField as necessary
        passwordfield.textProperty().bindBidirectional(showpassword.textProperty());

    }



@FXML
private void changeImage() {
    if (imageSwitch) {
        // Show the password
        image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/invisible.png"))));
        // Hide the PasswordField and show the TextField
        showPassword(true);
    } else {
        // Hide the password
        image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/visible.png"))));
        // Show the PasswordField and hide the TextField
        showPassword(false);
    }
    // Toggle the switch for the next click
    imageSwitch = !imageSwitch;
}

    private void showPassword(boolean show) {
        if (show) {
            // Copying the password from the PasswordField to the TextField to show it
            showpassword.setText(passwordfield.getText());
            passwordfield.setManaged(false);
            passwordfield.setVisible(false);

            showpassword.setManaged(true);
            showpassword.setVisible(true);
            // Request focus and then set the caret position
            Platform.runLater(() -> {
                showpassword.requestFocus();
                showpassword.positionCaret(showpassword.getText().length());
            });
        } else {
            // Hide the password and show it in the PasswordField
            passwordfield.setText(showpassword.getText());
            showpassword.setManaged(false);
            showpassword.setVisible(false);

            passwordfield.setManaged(true);
            passwordfield.setVisible(true);
            Platform.runLater(() -> {
                passwordfield.requestFocus();
                // Set the caret position to the end of the text
                passwordfield.positionCaret(passwordfield.getText().length());
            });        }
    }

    @FXML
    protected void onSubmitClick() {
        String email = emailfield.getText();
        String password = passwordfield.getText();

        if (!email.isBlank() && !password.isBlank()) {

            try {
                User user = SessionManager.getInstance().auth(email, password);
                System.out.println("User session connect is " + SessionManager.getInstance().getUserId());

                if (user != null) {
                    // Assuming user.getRoles() returns a String of roles
                    String userRoles = user.getRoles();
                    String fxmlFile;

                    // Check if the user has the ROLE_ADMIN
                    if (userRoles.contains("\"ROLE_ADMIN\"")) {
                        fxmlFile = "/fxml/bloodify/admin.fxml"; // Path to your admin FXML file
                    } else {
                        fxmlFile = "/fxml/bloodify/home.fxml"; // Path to your home page FXML file
                    }

                    // Load FXML based on the role
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Parent root = loader.load();

                    // Create a new stage
                    Stage stage = new Stage();
                    stage.setTitle(userRoles.contains("\"ROLE_ADMIN\"") ? "Admin Page" : "Home Page"); // Set title based on role

                    // Set the scene
                    Scene scene = new Scene(root);
                    stage.setScene(scene);

                    // Close the current stage
                    Stage currentStage = (Stage) emailfield.getScene().getWindow();
                    currentStage.close();

                    // Show the new stage
                    stage.show();
                } else {
                    if (userService.emailExists(email)) {
                        passwordfield.getStyleClass().add("text-field-error");
                        showAlert(Alert.AlertType.ERROR, "Login Error", null, "Invalid  password");

                    }
                    // Handle authentication failure
                    else {
                        showAlert(Alert.AlertType.ERROR, "Login Error", null, "Invalid username or password");

                    }
                }
            } catch (SQLException | IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error message", null, "Please enter a valid email and password");
        }
    }

//    @FXML
//    protected void onSubmitClick() {
//        String email = emailfield.getText();
//        String password = passwordfield.getText();
//
//        if (!email.isBlank() && !password.isBlank()) {
//            try {
//                if (userService.emailExists(email)) {
//                    if (!isCaptchaValidated) { // Show CAPTCHA only if not validated before
//                        // Show CAPTCHA dialog
//                        FXMLLoader captchaLoader = new FXMLLoader(getClass().getResource("/fxml/bloodify/captcha.fxml"));
//                        Parent captchaRoot = captchaLoader.load();
//                        CaptchaController captchaController = captchaLoader.getController(); // Get the controller instance
//                        Stage captchaStage = new Stage();
//                        captchaStage.setScene(new Scene(captchaRoot));
//                        captchaStage.initStyle(StageStyle.UNDECORATED);
//                        captchaStage.initModality(Modality.APPLICATION_MODAL);
//                        captchaStage.showAndWait();
//
//                        // After CAPTCHA verification
//                        if (captchaController.isRotationCorrect()) {
//                            isCaptchaValidated = true; // Set flag to true after successful validation
//                        } else {
//                            showAlert(Alert.AlertType.ERROR, "Login Error", null, "Incorrect CAPTCHA rotation");
//                            return; // Exit the method without further processing
//                        }
//                    }
//
//                    // Proceed with the login process
//                    User user = SessionManager.getInstance().auth(email, password);
//                    System.out.println("User session connect is " + SessionManager.getInstance().getUserId());
//
//                    if (user != null) {
//                        String userRoles = user.getRoles();
//                        String fxmlFile = userRoles.contains("\"ROLE_ADMIN\"") ? "/fxml/bloodify/dashboard.fxml" : "/fxml/bloodify/home.fxml";
//
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
//                        Parent root = loader.load();
//
//                        Stage stage = new Stage();
//                        stage.setTitle(userRoles.contains("\"ROLE_ADMIN\"") ? "Admin Page" : "Home Page");
//                        Scene scene = new Scene(root);
//                        scene.getStylesheets().add(getClass().getResource("/Styles/style.css").toExternalForm());
//                        stage.setScene(scene);
//
//                        Stage currentStage = (Stage) emailfield.getScene().getWindow();
//                        currentStage.close();
//
//                        stage.show();
//                    } else {
//                        showAlert(Alert.AlertType.ERROR, "Login Error", null, "Invalid password");
//                    }
//                } else {
//                    showAlert(Alert.AlertType.ERROR, "Login Error", null, "Email does not exist");
//                }
//            } catch (SQLException | IOException e) {
//                System.out.println("Error: " + e.getMessage());
//            }
//        } else {
//            showAlert(Alert.AlertType.ERROR, "Error message", null, "Please enter a valid email and password");
//        }
//    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
//        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/profile.css").toExternalForm());
        // Get the dialog pane
        DialogPane dialogPane = alert.getDialogPane();

        // Add the custom style class

        // Show the alert
        alert.showAndWait();
    }

    @FXML
    public void handleCreateAccountClick() {
        try {
            // Load the sign-up window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/register.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(root));

            // Apply the transition animation
            SceneTransition.fadeTransition(stage.getScene(), stage);

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) create_account.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to load sign-up window.");
        }
    }


    @FXML
    public void resetpassword() {
        try {
            // Load the sign-up window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/forgetpassword.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));

            // Apply the transition animation
            SceneTransition.fadeTransition(stage.getScene(), stage);

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) create_account.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to load sign-up window.");
        }
    }



}
