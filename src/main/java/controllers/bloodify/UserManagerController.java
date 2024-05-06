package controllers.bloodify;

import Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.user.User;
import services.UserService;


import java.io.IOException;

public class UserManagerController {
    @FXML
    private Hyperlink listlink;
    @FXML
    private Button savebtn ;
    @FXML
    private TextField first_name;

    @FXML
    private TextField last_name;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirm_password;
    @FXML
    private ToggleGroup role;

    @FXML
    private RadioButton radioButton1;

    @FXML
    private RadioButton radioButton2;

    private String buttonIdentifier;
    private UserService userService = new UserService(); // Instance of UserService
    private User user; // Define a field to hold the user object

    public void setButtonIdentifier(String buttonIdentifier) {
        this.buttonIdentifier = buttonIdentifier;
    }
    public void receiveUser(User user) {
        this.user = user; // Store the user object in the field
        System.out.println("Received user: " + user.getEmail());
    }

    public void initialize() {
        System.out.println("btn value is " + buttonIdentifier);
        if ("1".equals(buttonIdentifier)) {
            System.out.println("Page opened from add");
        } else {
            System.out.println("Page opened from update ");
            if (user != null) {
                first_name.setText(user.getFirstName());
                last_name.setText(user.getLastName());
                email.setText(user.getEmail());
                password.setText(user.getPassword());
                confirm_password.setText(user.getPassword());
                if (user != null && user.getRoles() != null && user.getRoles().contains("\"ROLE_USER\"")) {
                    // If the user has the role "ROLE_USER", select radioButton2
                    radioButton2.setSelected(true);
                    radioButton1.setSelected(false);

                } if (user != null && user.getRoles() != null && user.getRoles().contains("\"ROLE_ADMIN\"")) {
                    // Otherwise, select radioButton1 or deselect radioButton2 based on your requirement
                    radioButton1.setSelected(true);
                    radioButton2.setSelected(false);
                }
            } else {
                System.err.println("User object is null.");
            }
        }
    }



    @FXML
    public void onlist() {
        try {
            // Load the profile window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/admin.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Users table");
            stage.setScene(new Scene(root));

            // Apply the transition animatio

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) listlink.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    @FXML
    public void onSave(){

        if ("1".equals(buttonIdentifier)) {
            System.out.println("add action ");
            String firstName = first_name.getText().trim();
            String lastName = last_name.getText().trim();
            String userEmail = email.getText().trim();
            String userPassword = password.getText();
            String confirmPassword = confirm_password.getText();

            // Validate that no field is empty
            if (firstName.isEmpty() || lastName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || confirmPassword.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill in all fields.");
                return;
            }

            // Validate email format
            if (!isValidEmail(userEmail)) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Please enter a valid email address.");
                return;
            }

            // Check if the email already exists
            if (userService.emailExists(userEmail)) {
                email.getStyleClass().add("text-field-error");
                showAlert(Alert.AlertType.ERROR, "Error", null, "A user with this email already exists.");
                return;
            }
            if (!userService.emailExists(userEmail)) {
                email.getStyleClass().clear();
                email.getStyleClass().add("text-input");
            }

            // Validate password length
            if (userPassword.length() < 8) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Password must be at least 8 characters long.");
                return;
            }

            // Validate password match
            if (!userPassword.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Passwords do not match.");
                return;
            }
            String defaultRole = "";
            if (radioButton2.isSelected()) {
                defaultRole = "[\"ROLE_USER\"]";
            }

            if (radioButton1.isSelected()) {
                defaultRole = "[\"ROLE_ADMIN\"]";
            }
            // Set default role

            User user = new User(userEmail, firstName, lastName, userPassword, defaultRole);

            // Insert user into database
            userService.insertUserPst(user);
            try {
                // Load the profile window FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/admin.fxml"));
                Parent root = loader.load();

                // Create a new stage
                Stage stage = new Stage();
                stage.setTitle("Users table");
                stage.setScene(new Scene(root));

                // Apply the transition animatio

                // Show the new stage
                stage.show();

                // Get the reference to the current window and close it
                Stage currentStage = (Stage) listlink.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                // Handle the exception if the FXML file cannot be loaded
                e.printStackTrace(); // Handle the exception appropriately
            }

        }
        else {
            System.out.println("update action  ");
            String firstName = first_name.getText().trim();
            String lastName = last_name.getText().trim();
            String userEmail = email.getText().trim();
            String userPassword = password.getText();
            String confirmPassword = confirm_password.getText();

            String defaultRole = "";
            if (radioButton2.isSelected()) {
                defaultRole = "[\"ROLE_USER\"]";
            }

            if (radioButton1.isSelected()) {
                defaultRole = "[\"ROLE_ADMIN\"]";
            }
            if (firstName.isEmpty() || lastName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || confirmPassword.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill in all fields.");
                return;
            }

            // Validate email format
            if (!isValidEmail(userEmail)) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Please enter a valid email address.");
                return;
            }

            // Check if the email already exists
            if (userService.emailExists(userEmail) && !user.getEmail().equals(userEmail)) {
                email.getStyleClass().add("text-field-error");
                showAlert(Alert.AlertType.ERROR, "Error", null, "A user with this email already exists.");
                return;
            }
            if (!userService.emailExists(userEmail)) {
                email.getStyleClass().clear();
                email.getStyleClass().add("text-input");
            }

            // Validate password length
            if (userPassword.length() < 8) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Password must be at least 8 characters long.");
                return;
            }

            // Validate password match
            if (!userPassword.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Passwords do not match.");
                return;
            }
            user.setId(userService.getUserIdByEmail(user.getEmail()));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(userEmail);
            user.setPassword(userPassword);
            user.setRoles(defaultRole);
            userService.updateUser1(user);
            try {
                // Load the profile window FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/admin.fxml"));
                Parent root = loader.load();

                // Create a new stage
                Stage stage = new Stage();
                stage.setTitle("Users table");
                stage.setScene(new Scene(root));

                // Apply the transition animatio

                // Show the new stage
                stage.show();

                // Get the reference to the current window and close it
                Stage currentStage = (Stage) listlink.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                // Handle the exception if the FXML file cannot be loaded
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
