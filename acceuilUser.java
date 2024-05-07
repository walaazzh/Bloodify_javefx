package controllers;

import entities.centredecollect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import services.Servicecentrededecollect;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class acceuilUser implements Initializable {
    @FXML
    private GridPane grid;

    private List<centredecollect> centres;

    private final Servicecentrededecollect service = new Servicecentrededecollect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int columns=0;
        int rows=1;

        // Utilisez la variable de classe centres plutôt que de déclarer une nouvelle variable
        centres = getCentres();
        try {





            for (int i = 0; i < centres.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/exempleCentreUser.fxml"));
                VBox vbox = fxmlLoader.load();
                ExempleCentreUser exempleCentreCollecteUser = fxmlLoader.getController();
                exempleCentreCollecteUser.setData(centres.get(i));



                if (columns==3){

                    columns=0;
                    rows++;
                }

                // Ajouter le VBox dans le GridPane
                grid.add( vbox, columns++, rows);

                // Ajouter des marges au VBox
                GridPane.setMargin( vbox, new Insets(15,15,15,15));
            }




            // Passer à la prochaine colonne ou ligne si nécessaire
            if (columns == 2) {
                columns = 0;
                rows++;
            }


        }
        catch (Exception e) {e.printStackTrace();}
    }

    private List<centredecollect> getCentres() {
        try {
            return service.afficher();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retourne null en cas d'erreur
        }
    }
}
