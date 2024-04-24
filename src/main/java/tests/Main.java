package tests;

import controllers.ListeDeCentre;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML de l'interface admin
        FXMLLoader ListeDeCentreLoader = new FXMLLoader(getClass().getResource("/ListeDeCentre.fxml"));

        Parent listeDeCentreRoot = ListeDeCentreLoader.load();

        // Créer la scène correspondante
        Scene ListeDeCentreScene = new Scene(listeDeCentreRoot);

        // Définir la scène initiale
        primaryStage.setScene(ListeDeCentreScene);
        primaryStage.setTitle("Rendez-vous disponible");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
