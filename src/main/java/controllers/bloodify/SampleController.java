package controllers.bloodify;



import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class SampleController {

    @FXML
    private Pane messageBar;

    @FXML
    private Label messageLabel;

    public void showMessage(String message) {
        // Set the message text
        messageLabel.setText(message);

        // Show the message bar
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), messageBar);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Hide the message bar after 3 seconds
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), messageBar);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(3));
        fadeOut.setOnFinished(event -> messageBar.setVisible(false));
        fadeOut.play();
    }
}
