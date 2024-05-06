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
import java.util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private Button settingslink;
    @FXML
    private Button logoutbtn ;
    @FXML
    private Button add ;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> suggestionListView;

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

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Show or hide suggestionListView based on user input
            if (newValue.isEmpty()) {
                suggestionListView.setVisible(false);
                suggestionListView.getItems().clear();
            } else {
                updateSuggestions(newValue);
                if (suggestionListView.getItems().isEmpty()) {
                    suggestionListView.setVisible(false);
                } else {
                    suggestionListView.setVisible(true);
                }
            }
            filterTable(newValue);
        });

        // Listener for focus changes in the searchField
        searchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // If searchField loses focus, hide suggestionListView
            if (!newValue) {
                suggestionListView.setVisible(false);
            }
        });
        suggestionListView.setVisible(false); // Initially hide suggestion list
        currentUser = SessionManager.getInstance().getCurrentUser();
        List<User> allUsers = userService.getAllUsers();
        ObservableList<User> observableUsers = FXCollections.observableArrayList(allUsers);
        table.setItems(observableUsers);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/profile.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) settingslink.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openPage2(String buttonIdentifier, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/addedituser.fxml"));
            Parent root = loader.load();
            UserManagerController controller = loader.getController();

            controller.setButtonIdentifier(buttonIdentifier);

            if ("2".equals(buttonIdentifier)) {
                controller.receiveUser(user);
            }

            controller.initialize();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

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
        SessionManager.getInstance().cleanUserSession();

        int userId = SessionManager.getInstance().getUserId();
        System.out.println("User session connect is " + userId);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("login ");
            stage.setScene(new Scene(root));

            LoginController.SceneTransition.fadeTransition(stage.getScene(), stage);

            // Show the new stage
            stage.show();

            Stage currentStage = (Stage) logoutbtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void refreshTable() {
        ObservableList<User> observableUsers = FXCollections.observableArrayList(userService.getAllUsers());
        table.setItems(observableUsers);
    }
    @FXML
    public void ondashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) settingslink.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateSuggestions(String searchText) {
        List<User> allUsers = userService.getAllUsers();
        List<String> suggestions = allUsers.stream()
                .filter(user -> user.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getLastName().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getRoles().toLowerCase().contains(searchText.toLowerCase()))
                .map(user -> user.getEmail()) // Extract email for suggestions
                .collect(Collectors.toList());
        ObservableList<String> observableSuggestions = FXCollections.observableArrayList(suggestions);
        suggestionListView.setItems(observableSuggestions);
    }


    private void filterTable(String searchText) {
        List<User> allUsers = userService.getAllUsers();
        List<User> filteredUsers = allUsers.stream()
                .filter(user -> user.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getLastName().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getRoles().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        ObservableList<User> observableFilteredUsers = FXCollections.observableArrayList(filteredUsers);
        table.setItems(observableFilteredUsers);
    }

}