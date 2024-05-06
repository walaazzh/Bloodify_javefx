package controllers.bloodify.events;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.user.Event;
import services.CategoryService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class Items {
    @FXML
    private Label begindateinfo;

    @FXML
    private Label categoryinfo;

    @FXML
    private Label enddateinfo;

    @FXML
    private ImageView imageifo;

    @FXML
    private Label nameinfo;

    @FXML
    private Button registerbtn;
    private static final double IMAGE_WIDTH = 200.0;
    private static final double IMAGE_HEIGHT = 100.0;
    private Event event;



    @FXML
    void registeraction(ActionEvent event) {
        try {
            // Store the event ID in a static variable
            Eventdetails.setEventId(this.event.getIdevent());

            // Load the Eventdetails FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/eventdetails.fxml"));

            // Load the root node of the Eventdetails scene
            Parent eventDetailsParent = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new scene with the Eventdetails layout
            Scene eventDetailsScene = new Scene(eventDetailsParent);

            // Set the scene to the stage
            stage.setScene(eventDetailsScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setEvent(Event event) {
        this.event = event;
        // Set event data in the UI elements
        begindateinfo.setText(event.getDatedebut());
        enddateinfo.setText(event.getDatefin());
        try {
            CategoryService categoryService = new CategoryService();
            String categoryName = categoryService.getCategoryName(event.getIdcategory());
            categoryinfo.setText(categoryName);
        } catch (SQLException e) {
            e.printStackTrace();
            categoryinfo.setText("Unknown Category");
        }
        nameinfo.setText(event.getName());

        String imagePath = event.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toURL().toString());
                    imageifo.setFitWidth(IMAGE_WIDTH);
                    imageifo.setFitHeight(IMAGE_HEIGHT);
                    imageifo.setImage(image);
                } else {
                    // Handle case where image file does not exist
                    System.out.println("Image file does not exist: " + imagePath);
                }
            } catch (MalformedURLException e) {
                // Handle MalformedURLException
                e.printStackTrace();
            }
        } else {
            // Handle case where imagePath is null or empty
            System.out.println("Image path is null or empty");
        }
    }

}
