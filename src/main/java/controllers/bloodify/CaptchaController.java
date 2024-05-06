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
        Random random = new Random();
        int randomNumber ;
        do {
            randomNumber = random.nextInt(17);
            randomNumber -= 8;
        } while (randomNumber < -4 && randomNumber > 4);
        randomRotation = 10.0*randomNumber;
        System.out.println("Random Rotation Angle: " + randomRotation); // Print the random rotation angle
    }
    @FXML
    private void handleCaptchaVerification(ActionEvent event) {
        if (randomRotation == 0.0 || (Math.abs(randomRotation) % 360 == 0)){
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
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> stage.close()); // Close the stage when the transition finishes
        delay.play(); // Start the pause transition
    }
    @FXML
    private void initialize() {
        camelImage.setRotate(randomRotation);
        error.setVisible(false);
        right.setVisible(false);
        arrowtoright.setOnMouseClicked(this::rotateImageRight);
        arrowtoleft.setOnMouseClicked(this::rotateImageLeft);
    }
    private void rotateImageRight(MouseEvent event) {
        rotationAngle += 10.0; // Adjust the rotation angle as needed
        randomRotation+=10.0;
        camelImage.setRotate(camelImage.getRotate() + 10.0); // Rotate from the current angle
        System.out.println("rotation angle : " + rotationAngle); // Print the random rotation angle
        System.out.println("rotation angle : " + randomRotation); // Print the random rotation angle
        System.out.println(randomRotation % 360);
        ; // Print the random rotation angle
    }
    private void rotateImageLeft(MouseEvent event) {
        rotationAngle -= 10.0; // Adjust the rotation angle as
        randomRotation-=10.0;
        camelImage.setRotate(camelImage.getRotate() - 10.0); // Rotate from the current angle
        System.out.println("rotation angle : " + rotationAngle); // Print the random rotation angle
        System.out.println("rotation angle : " + randomRotation); // Print the random rotation
        System.out.println(randomRotation % 360);


    }
    private void checkRotation(double currentRotation) {
        if (randomRotation== 0.0) {
            error.setVisible(false);
            right.setVisible(true);
        } else {
            showError();
        }
    }
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
    @FXML
    private void handleCheckRotation(ActionEvent event) {
        checkRotation(camelImage.getRotate());
    }

}
