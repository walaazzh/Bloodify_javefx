package controllers.bloodify;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CaptchaController {

    @FXML
    private ImageView camelImage;
    @FXML
    private ImageView right;

    @FXML
    private ImageView arrowtoright;

    @FXML
    private ImageView arrowtoleft;

    @FXML
    private AnchorPane error;
    @FXML
    private Button checkRotationButton;

    private boolean rotationCorrect = false;



    public void setRotationCorrect(boolean correct) {
        rotationCorrect = correct;
    }
    public boolean isRotationCorrect() {
        return rotationCorrect;
    }


    private double rotationAngle = 0.0;
    private double randomRotation = 0.0;
    private int rotationCount = 0;

    public CaptchaController() {
        // Initialize random rotation angle to 10.0
        Random random = new Random();

        // Generate a random integer between 1 and 9
        int randomNumber ;
        do {
            // Generate a random number in the range [0, 16) (non-inclusive)
            randomNumber = random.nextInt(17);

            // Shift the range to [-8, 8) (non-inclusive) by subtracting 8
            randomNumber -= 8;
        } while (randomNumber < -4 && randomNumber > 4);

        randomRotation = 10.0*randomNumber;

        System.out.println("Random Rotation Angle: " + randomRotation); // Print the random rotation angle

    }
    @FXML
    private void handleCaptchaVerification(ActionEvent event) {
        if (randomRotation == 0.0) {
            setRotationCorrect(true);
            closeCaptchaDialog();
            error.setVisible(false);
            right.setVisible(true);
        } else {
            showError();
        }
    }
    private void closeCaptchaDialog() {
        Stage stage = (Stage) checkRotationButton.getScene().getWindow();

        // Create a PauseTransition with a duration of 2 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> stage.close()); // Close the stage when the transition finishes

        delay.play(); // Start the pause transition
    }
    @FXML
    private void initialize() {
        // Rotate the camel image randomly
        camelImage.setRotate(randomRotation);
        error.setVisible(false);
        right.setVisible(false);

        // Add event handlers for the left and right arrow ImageView components
        arrowtoright.setOnMouseClicked(this::rotateImageRight);
        arrowtoleft.setOnMouseClicked(this::rotateImageLeft);
    }

    // Method to rotate the camel image to the right
    // Method to rotate the camel image to the right
    private void rotateImageRight(MouseEvent event) {
        rotationAngle += 10.0; // Adjust the rotation angle as needed
        randomRotation+=10.0;
        camelImage.setRotate(camelImage.getRotate() + 10.0); // Rotate from the current angle
        System.out.println("rotation angle : " + rotationAngle); // Print the random rotation angle
        System.out.println("rotation angle : " + randomRotation); // Print the random rotation angle




    }

    // Method to rotate the camel image to the left
    private void rotateImageLeft(MouseEvent event) {
        rotationAngle -= 10.0; // Adjust the rotation angle as
        randomRotation-=10.0;
        camelImage.setRotate(camelImage.getRotate() - 10.0); // Rotate from the current angle
        System.out.println("rotation angle : " + rotationAngle); // Print the random rotation angle
        System.out.println("rotation angle : " + randomRotation); // Print the random rotation angle

    }


    // Method to check if the rotation is correct
    // Method to check if the rotation is correct
    private void checkRotation(double currentRotation) {
        if (randomRotation== 0.0) {
            error.setVisible(false);
            right.setVisible(true);
        } else {
            showError();
        }
    }


    // Method to show the error message
    private void showError() {
        error.setVisible(true); // Make the error anchor pane visible
        // Schedule a task to hide the error anchor pane after 4 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                error.setVisible(false); // Make the error anchor pane invisible
                timer.cancel(); // Cancel the timer
            }
        }, 3000); // 4000 milliseconds = 4 seconds
    }

    // Method to handle the button click event
    @FXML
    private void handleCheckRotation(ActionEvent event) {
        checkRotation(camelImage.getRotate());
    }


}
