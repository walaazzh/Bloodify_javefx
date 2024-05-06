package controllers.bloodify;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontController {

    @FXML
    private Button signin;

    @FXML
    private Button signup;
    @FXML
    private Hyperlink home;
    @FXML
    private Hyperlink service;

    @FXML
    public void signinon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));
            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);
            stage.show();
            Stage currentStage = (Stage) signin.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
        }
    }
    @FXML
    public void signupon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/register.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));
            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);
            stage.show();
            Stage currentStage = (Stage) signup.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
        }
    }
    @FXML
    public void onhome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/front.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));
            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);
            stage.show();
            Stage currentStage = (Stage) signup.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
        }
    }
    @FXML
    public void onservice() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/services.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));
            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);
            stage.show();
            Stage currentStage = (Stage) signup.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
        }
    }
}
