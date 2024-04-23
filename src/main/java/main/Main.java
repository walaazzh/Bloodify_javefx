package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Load the custom font
            Font.loadFont(getClass().getResourceAsStream("/Styles/SFCompactDisplay-Regular.ttf"), 20);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/bloodify/login.fxml"));
//            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/password.png"))); // Adjust the path as necessary
            Scene scene = new Scene(fxmlLoader.load());

            // Apply CSS to the scene
            scene.getStylesheets().add(getClass().getResource("/Styles/style.css").toExternalForm());

            stage.setTitle("Login");
            stage.setScene(scene);
            stage.setResizable(false); // Prevent resizing of the login window
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
