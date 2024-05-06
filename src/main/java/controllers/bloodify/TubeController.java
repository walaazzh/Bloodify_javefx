package controllers.bloodify;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TubeController implements Initializable {

    @FXML
    private Rectangle tubeBody;
    private Rectangle bloodFill;
    private Timeline timeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create a colored rectangle representing the blood fill
        bloodFill = new Rectangle(tubeBody.getWidth(), 0, Color.RED);
        bloodFill.setY(tubeBody.getY() + tubeBody.getHeight()); // Position the blood fill at the bottom of the tube
        tubeBody.setFill(Color.TRANSPARENT); // Set the tube body fill to transparent

        // Create a timeline to animate the blood fill
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    bloodFill.setHeight(0); // Start with an empty blood fill
                }),
                new KeyFrame(Duration.seconds(5), e -> {
                    bloodFill.setHeight(tubeBody.getHeight()); // Fill the tube with blood
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }
}
