package controllers.bloodify.events;
import Utils.SessionManager;
import controllers.bloodify.LoginController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.user.Category;
import models.user.Event;
import services.CategoryService;
import services.EventService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryCrud {

    private final CategoryService ps = new CategoryService();
    private ObservableList<Category> observableList;
    private ObservableList<Event> eventList;

    private final CategoryService categoryService = new CategoryService();
    private EventService eventService = new EventService();


    @FXML
    private TableColumn<Category, Void> actioncol;

    @FXML
    private Button btnsubmit;
    @FXML
    private Label catNum;
    @FXML
    private Label leastcat;
    @FXML
    private Label mostcat;

    @FXML
    private TableColumn<Category, String> descriptioncol;

    @FXML
    private Button logoutbtn;

    @FXML
    private TableColumn<Category, String> namecol;


    @FXML
    private PieChart piechartcategories;

    @FXML
    private TextField searchfield;

    @FXML
    private Hyperlink settingslink;

    @FXML
    private TableView<Category> tablecategory;

    @FXML
    void initialize() {

        initializeCategoryTab();
        setupActionColumn();
        updateCategoryCount();
        updateMostEventCategory();
        updateLeastEventCategory();
        setPiechartcategories();
    }

    @FXML
    void categorywindow(ActionEvent event) {
        try {
            // Load the add event window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/addcategory.fxml"));
            Parent root = loader.load();

            // Create a new stage for the add event window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Category");

            // Set the modality of the stage to APPLICATION_MODAL
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the add event window
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load add category window", e.getMessage());
        }

    }

    @FXML
    void handleLogout(ActionEvent event) {
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

    }


    @FXML
    void ondashboard(ActionEvent event) {
        try {
            // Load the profile window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bloodify/dashboard.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Users table");
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

    @FXML
    void onsettings(ActionEvent event) {
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

    private void initializeCategoryTab() {
        try {
            // Read categories from the database
            List<Category> categoryList = ps.read();

            // Initialize observableList with the categoryList
            observableList = FXCollections.observableArrayList(categoryList);

            // Set items to the TableView
            tablecategory.setItems(observableList);

            // Set cell value factories for each column
            namecol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            descriptioncol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));


        } catch (SQLException e) {
            // Show an error alert if loading categories fails
            showAlert("Error", "Error Loading Categories", e.getMessage());
        }
        // Add listener to search field
        searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
            // Call method to filter TableView based on search text
            filterTableView(newValue);
        });
    }


    private void filterTableView(String searchText) {
        // Create a filtered list to hold the filtered items
        FilteredList<Category> filteredList = new FilteredList<>(observableList, p -> true);

        // Set predicate to filter based on search text
        filteredList.setPredicate(category -> {
            // If search text is empty, show all items
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            // Convert search text to lowercase for case-insensitive comparison
            String lowerCaseFilter = searchText.toLowerCase();

            // Check if category name or description contains the search text
            if (category.getName().toLowerCase().contains(lowerCaseFilter) ||
                    category.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Show item if it matches the search text
            }
            return false; // Hide item if it doesn't match the search text
        });

        // Wrap the filtered list in a SortedList
        SortedList<Category> sortedList = new SortedList<>(filteredList);

        // Bind the sorted list to the TableView
        sortedList.comparatorProperty().bind(tablecategory.comparatorProperty());
        tablecategory.setItems(sortedList);
    }


    void setupActionColumn() {
        actioncol.setCellFactory(param -> new TableCell<Category, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");

            {
                deleteButton.setOnAction(category -> {
                    Category category1 = getTableView().getItems().get(getIndex());
                    deleteCategory(category1);
                });

                updateButton.setOnAction(category -> {
                    Category category1 = getTableView().getItems().get(getIndex());
                    openUpdateWindow(category1);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(deleteButton, updateButton);
                    buttonsBox.setSpacing(5);
                    setGraphic(buttonsBox);
                }
            }
        });
    }

    void deleteCategory(Category category) {
        boolean confirmed = ConfirmationDialog.showConfirmationDialog("Are you sure you want to delete this category?");
        if (confirmed) {
            try {
                categoryService.delete(category.getIdcategory()); // Assuming getId() returns the category's ID
                observableList.remove(category);
                showInformationAlert("Success", "Category deleted successfully");
            } catch (SQLException e) {
                showAlert("Error", "Error deleting category", e.getMessage());
            }
        }
    }

    void openUpdateWindow(Category category) {
        try {
            // Load the update event window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/addcategory.fxml"));
            Parent root = loader.load();

            // Access the controller of the update event window
            Addcategory addcategory = loader.getController();

            // Pass the event data to the update window controller
            addcategory.initDataForUpdate(category);

            // Create a new stage for the update event window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Category");

            // Set the modality of the stage to APPLICATION_MODAL
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the update event window
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load update category window", e.getMessage());
        }
    }

    @FXML
    void refreshTableView(ActionEvent event) {
        try {
            // Retrieve the updated list of categories from the database
            List<Category> updatedCategoryList = ps.read();

            // Update the data in the observableList with the new categories
            observableList.setAll(updatedCategoryList);

            // Set the updated observableList as the items of the TableView
            tablecategory.setItems(observableList);

            // Trigger a refresh of the TableView to reflect the changes
            tablecategory.refresh();

            // Show success alert or any other notification if needed
            showInformationAlert("Success", "Table View refreshed successfully with updated data");
        } catch (SQLException e) {
            // Show an error alert if refreshing the TableView fails
            showAlert("Error", "Error refreshing Table View", e.getMessage());
        }
    }

    private void updateCategoryCount() {
        if (observableList != null) {
            int totalCategories = observableList.size(); // Obtenir le nombre total de catégories
            catNum.setText(String.valueOf(totalCategories)); // Mettre à jour le texte du Label avec le nombre total de catégories
        } else {
            // Gérez le cas où votre liste observable n'est pas initialisée
            catNum.setText("N/A"); // Par exemple, affichez "N/A" si la liste n'est pas disponible
        }
    }

    private void updateMostEventCategory() {
        try {
            // Appelez la méthode dans CategoryService pour obtenir la catégorie avec le plus d'événements
            Category mostEventsCategory = categoryService.getCategoryWithMostEvents();

            // Mettre à jour le Label avec le nom de la catégorie
            if (mostEventsCategory != null) {
                mostcat.setText(mostEventsCategory.getName());
            } else {
                mostcat.setText("Aucune catégorie trouvée");
            }
        } catch (SQLException e) {
            mostcat.setText("Erreur lors de la récupération de la catégorie avec le plus d'événements");
            e.printStackTrace();
        }
    }

    private void updateLeastEventCategory() {
        try {
            // Appelez la méthode dans CategoryService pour obtenir la catégorie avec le plus d'événements
            Category leastEventsCategory = categoryService.getCategoryWithLeastEvents();

            // Mettre à jour le Label avec le nom de la catégorie
            if (leastEventsCategory != null) {
                leastcat.setText(leastEventsCategory.getName());
            } else {
                leastcat.setText("Aucune catégorie trouvée");
            }
        } catch (SQLException e) {
            mostcat.setText("Erreur lors de la récupération de la catégorie avec le moins d'événements");
            e.printStackTrace();
        }
    }
private void setPiechartcategories() {
    try {
        // Retrieve all categories
        List<Category> categories = categoryService.read();

        // Create a list to store PieChart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Retrieve the total number of events
        int totalEvents = eventService.read().size();

        // Calculate the percentage of events in each category and add to PieChart data
        for (Category category : categories) {
            int eventsInCategory = eventService.getEventsByCategory(category.getIdcategory()).size();
            if (eventsInCategory > 0) {
                double percentage = (double) eventsInCategory / totalEvents * 100.0;
                pieChartData.add(new PieChart.Data(category.getName(), percentage));
            }
        }

        // Set the PieChart data
        piechartcategories.setData(pieChartData);
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the error
    }
}


    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
