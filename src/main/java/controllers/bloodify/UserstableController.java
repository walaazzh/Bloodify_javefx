package controllers.bloodify;


import Utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.user.User;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;

import javafx.util.Callback;


public class UserstableController implements Initializable {

    @FXML
    private TableColumn<User, String> email;

    @FXML
    private TableColumn<User, String> firstname;

    @FXML
    private TableColumn<User, String> lastname;

    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> role;
    @FXML
    private TableColumn<User, Void> actions;
    @FXML
    private Label userlabel;
    @FXML
    private Hyperlink settingslink;
    @FXML
    private Button logoutbtn ;
    @FXML
    private Button add ;

    public int a=1;




    private final UserService userService;
    private User currentUser;
    public UserstableController() {
        this.userService = new UserService();
    }

    private final Button deleteButton = new Button("Delete");
    private final Button updateButton = new Button("Update");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        currentUser = SessionManager.getInstance().getCurrentUser();
        // Get all users from the UserService
        List<User> allUsers = userService.getAllUsers();

        // Convert the list to an observable list
        ObservableList<User> observableUsers = FXCollections.observableArrayList(allUsers);

        // Set the items of the table view
        table.setItems(observableUsers);

        // Set cell value factories for each column
        email.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        firstname.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        lastname.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));
        role.setCellValueFactory(data -> {
            String value = data.getValue().getRoles();
            String roleValue = value.contains("\"ROLE_ADMIN\"") ? "admin" : "user";
            return new SimpleStringProperty(roleValue);
        });
        actions.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");

            // Initialize block for the cell, it is executed when the cell is created
            {
                deleteButton.getStyleClass().add("delete-button");
                updateButton.getStyleClass().add("update-button");
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    userService.deleteUserByEmail(user.getEmail());
                    refreshTable();
                });
                updateButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    openPage2("2", user);

                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                StackPane buttonStack = new StackPane();

                if (empty) {
                    setGraphic(null);
                } else {
                    StackPane.setAlignment(deleteButton, Pos.CENTER_LEFT);
                    StackPane.setAlignment(updateButton, Pos.CENTER_RIGHT);
                    buttonStack.getChildren().addAll(deleteButton,updateButton);
                    setGraphic(buttonStack);

                }
            }
        });

    }
    @FXML
    public void onsettings() {
        try {
            // Load the profile window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/profile.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(new Scene(root));

            // Apply the transition animatio

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) settingslink.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    private void openPage2(String buttonIdentifier, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/addedituser.fxml"));
            Parent root = loader.load();
            UserManagerController controller = loader.getController();

            // Set the button identifier and receive the user before initializing the controller
            controller.setButtonIdentifier(buttonIdentifier);

            // Pass the user object to the controller only if buttonIdentifier is "2"
            if ("2".equals(buttonIdentifier)) {
                controller.receiveUser(user);
            }

            // Call initialize after setting the button identifier and receiving the user
            controller.initialize();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage based on the buttonIdentifier
            if ("1".equals(buttonIdentifier)) {
                stage.setTitle("Add User");
                Stage currentStage = (Stage) add.getScene().getWindow();
                currentStage.close();
            } else {
                stage.setTitle("Update User");
                Stage currentStage = (Stage) settingslink.getScene().getWindow();
                currentStage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addon() {
        openPage2("1",null);
    }
    @FXML
    private void handleLogout() {
        // Clean the user session
        SessionManager.getInstance().cleanUserSession();

        // Access the user ID through the SessionManager
        int userId = SessionManager.getInstance().getUserId();
        System.out.println("User session connect is " + userId);
        try {
            // Load the sign-up window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/login.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("login ");
            stage.setScene(new Scene(root));

            // Apply the transition animation
            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) logoutbtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            e.printStackTrace(); // Handle the exception appropriately
        }


        // Add code to navigate to the login screen or perform any additional tasks after logout
    }
    private void refreshTable() {
        ObservableList<User> observableUsers = FXCollections.observableArrayList(userService.getAllUsers());
        table.setItems(observableUsers);
    }
    @FXML
    public void ondashboard() {
        try {
            // Load the profile window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/dashboard.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root));

            // Apply the transition animatio

            // Show the new stage
            stage.show();

            // Get the reference to the current window and close it
            Stage currentStage = (Stage) settingslink.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            // Handle the exception if the FXML file cannot be loaded
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}