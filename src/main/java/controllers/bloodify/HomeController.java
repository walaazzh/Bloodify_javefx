package controllers.bloodify;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import models.user.User;
import Utils.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.control.Button;
import services.UserService;

public class HomeController implements Initializable {

    @FXML
    private Label username;

    @FXML
    private Hyperlink settingsbtn;
    @FXML
    private Button logoutbtn ;

    private User currentUser;
    private UserService userService = new UserService(); // Instantiate your UserService

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Retrieve the current user from the session manager
//        currentUser = SessionManager.getInstance().getCurrentUser();
//        username.setText(currentUser.getEmail());
//        username.setAlignment(Pos.CENTER_RIGHT);

    }

    @FXML
    public void onsettings() {
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
    @FXML
    private void handleLogout() {
        // Clean the user session
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
}
