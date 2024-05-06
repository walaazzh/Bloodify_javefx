package controllers.bloodify;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;
import java.util.*;

public class ForgetpasswordController {
    @FXML
    private TextField emailfield;
    @FXML
    private Button sendbtn;
    @FXML
    private Button login;
    private UserService userService = new UserService(); // Instance of UserService



    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        String email = emailfield.getText();
        if (userService.emailExists(email)) {
            int randomNum = generateRandomNumber();
            userService.addToken(email, String.valueOf(randomNum));
            System.out.println("Random number: " + randomNum);
            userService.mail(email,String.valueOf(randomNum));
            // Store the email in a variable
            String user_email = email;

            // Load the FXML file for the second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/passwordreset.fxml"));
            try {
                Parent root = loader.load();

                // Get the controller associated with the second scene
                ResetPasswordController controller2 = loader.getController();

                // Pass the email to Controller2
                controller2.setUserEmail(user_email);

                // Show the second scene
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Reset Password");
                stage.show();

                // Close the current scene
                Stage currentStage = (Stage) emailfield.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            emailfield.getStyleClass().add("text-field-error");
            showAlert("Email Not Found", "Error", "The email does not exist ");
        }
    }




    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
;
    public static int generateRandomNumber() {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            digits.add(i);
        }

        // Shuffle the list to randomize the order of digits
        Collections.shuffle(digits);

        StringBuilder randomNumber = new StringBuilder();

        // Make sure the first digit is not zero
        int firstDigit = digits.get(0) == 0 ? digits.get(1) : digits.get(0);
        randomNumber.append(firstDigit);

        // Shuffle again to randomize the remaining digits
        Collections.shuffle(digits);

        // Add 5 more digits to the random number
        for (int i = 0; i < 5; i++) {
            randomNumber.append(digits.get(i));
        }

        // Convert the string to an integer and return
        return Integer.parseInt(randomNumber.toString());
    }
    private static int randomDigit(List<Integer> digits) {
        Random random = new Random();
        int digit = random.nextInt(10);
        while (digits.subList(0, 4).contains(digit)) {
            digit = random.nextInt(10);
        }
        return digit;
    }

    public static void main(String[] args) {
        int randomNumber = generateRandomNumber();
        System.out.println(randomNumber);
    }

    @FXML
    void loginon() throws IOException {

            // Load the sign-up window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/login.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Log in");
            stage.setScene(new Scene(root));

            // Apply the transition animation
            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) login.getScene().getWindow();
            currentStage.close();



    }

}
