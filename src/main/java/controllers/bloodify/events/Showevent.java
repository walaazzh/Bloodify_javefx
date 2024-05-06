package controllers.bloodify.events;
import Utils.SessionManager;
import controllers.bloodify.LoginController;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.user.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import services.CategoryService;
import services.EventService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.control.TableColumn;

public class Showevent {
    @FXML
    private Hyperlink settingslink;
    @FXML
    private Button logoutbtn ;
    private static final Logger logger = LogManager.getLogger(Showevent.class);
    private final EventService eventService = new EventService();
    private final CategoryService categoryService = new CategoryService();
    private ObservableList<Event> eventObservableList ;

    @FXML
    private TableColumn<Event, Boolean> checkboxColumn;

    @FXML
    private TableColumn<Event, Void> actioncolumn;

    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private TableColumn<Event, String> categoryev;

    @FXML
    private TableColumn<Event, String> contactev;

    @FXML
    private TableColumn<Event, String> datedebutev;

    @FXML
    private TableColumn<Event, String> datefinev;

    @FXML
    private TableColumn<Event, String> descriptionev;

    @FXML
    private TableView<Event> eventtable;

    @FXML
    private TableColumn<Event, Integer> idev;

    @FXML
    private TableColumn<Event, String> imageev;

    @FXML
    private TableColumn<Event, Integer> maxparticipantev;

    @FXML
    private TableColumn<Event, String> nameev;

    @FXML
    private TableColumn<Event, String> organisateurev;



    @FXML
    private TextField searchevent;
    @FXML
    private Button navigateToAddButton;

    @FXML
    private TableColumn<Event, String> statusev;
    @FXML
    private Label errorfield;
    @FXML
    private ComboBox<String> categoryfilter;
    @FXML
    private DatePicker datedebutfilter;
    @FXML
    private DatePicker datefinfilter;
    @FXML
    private TextField namefilter;
    @FXML
    private TextField organizefilter;
    @FXML
    private TextField statusfilter;
    @FXML
    private CheckBox matchallbtn;

    @FXML
    private CheckBox matchatleastonebtn;
    @FXML
    private Button exportevent;


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

    @FXML
    void exportevents(ActionEvent event) {
        logger.info("Export events method started.");
        ObservableList<Event> selectedEvents = eventObservableList.filtered(Event::isSelected);
        if (selectedEvents.isEmpty()) {
            showAlert("Warning", "No events selected", "Please select one or more events to export.");
            return;
        }
        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Selected Events");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Event Name", "Description", "Start Date", "End Date", "Category", "Max Participants", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Populate data rows
            int rowNum = 1;
            for (Event event3 : selectedEvents) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(event3.getName() != null ? event3.getName() : "");
                row.createCell(1).setCellValue(event3.getDescription() != null ? event3.getDescription() : "");
                row.createCell(2).setCellValue(event3.getDatedebut() != null ? event3.getDatedebut().toString() : "");
                row.createCell(3).setCellValue(event3.getDatefin() != null ? event3.getDatefin().toString() : "");
                try {
                    // Attempt to get category name, handle SQLException
                    String categoryName = categoryService.getCategoryName(event3.getIdcategory());
                    row.createCell(4).setCellValue(categoryName != null ? categoryName : "Unknown Category");
                } catch (SQLException ex) {
                    // Handle the SQL exception
                    row.createCell(4).setCellValue("Unknown Category");
                }
                row.createCell(5).setCellValue(event3.getMaxParticipant());
                row.createCell(6).setCellValue(event3.getStatus() != null ? event3.getStatus() : "");
            }

            // Specify the absolute file path where the Excel file should be saved
            String filePath = "C:\\Users\\user\\Desktop\\walaa\\bloodify\\src\\main\\resources\\selected_events.xls";

            // Write the workbook to the specified file path
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            showInformationAlert("Success", "Selected events exported to Excel successfully. File saved at: " + filePath);
        } catch (IOException e) {
            logger.error("Error exporting selected events to Excel", e);
            showAlert("Error", "Error exporting selected events to Excel", e.getMessage());
        }
    }

    @FXML
    void initialize() {
        initializeEventTab();
        populateCategoryComboBox();
        initializeCheckBoxes();
        setupActionColumn();




    }

    @FXML
    void refreshevaction(ActionEvent event) {
        try {

            List<Event> updatedEventList = eventService.read();
            eventObservableList.setAll(updatedEventList);
            eventtable.setItems(eventObservableList);
            eventtable.refresh();

            showInformationAlert("Success", "Table View refreshed successfully with updated data");
        } catch (SQLException e) {

            showAlert("Error", "Error refreshing Table View", e.getMessage());
        }



    }

    @FXML



    void navigatetoadd(ActionEvent event) {
        try {
            // Load the add event window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/addevent.fxml"));
            Parent root = loader.load();

            // Create a new stage for the add event window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Event");

            // Set the modality of the stage to APPLICATION_MODAL
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the add event window
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load add event window", e.getMessage());
        }
    }

    void initializeEventTab()  {

        try {
            eventtable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            checkboxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

            // Setup checkbox in the header
            selectAllCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                for (Event event : eventObservableList) {
                    event.setSelected(newValue);
                }
            });

            List<Event> eventList = eventService.read();
            eventObservableList = FXCollections.observableArrayList(eventList);
            eventtable.setItems(eventObservableList);

            // Add checkbox to the table column header



            nameev.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            descriptionev.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
            datedebutev.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatedebut()));
            datefinev.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatefin()));
            organisateurev.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrganisateur()));
            contactev.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContact()));
            statusev.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
            maxparticipantev.setCellValueFactory(data -> {
                int maxParticipant = data.getValue().getMaxParticipant();
                return new SimpleIntegerProperty(maxParticipant).asObject();
            });

            imageev.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getImage()));
            imageev.setCellFactory(column -> new TableCell<Event, String>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                }

                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);

                    if (empty || imagePath == null) {
                        setGraphic(null);
                    } else {
                        Image image = new Image(new File(imagePath).toURI().toString());
                        imageView.setImage(image);
                        setGraphic(imageView);
                    }
                }
            });

            // Retrieve the category name based on the category ID and display it in the table
            categoryev.setCellValueFactory(cellData -> {
                int categoryId = cellData.getValue().getIdcategory();
                try {
                    String categoryName = categoryService.getCategoryName(categoryId);
                    return new SimpleStringProperty(categoryName);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new SimpleStringProperty("Unknown Category");
                }
            });

            // Setup columns (checkbox, etc.)
            setupColumns();
        } catch (SQLException e) {
            showAlert("Error", "Error Loading Events", e.getMessage());
        }
    }


    @FXML
    void applyfilter(ActionEvent event) {
        try {

            String categoryFilter = categoryfilter.getValue() != null ? categoryfilter.getValue().toString() : null;

            LocalDate datedebutFilter = datedebutfilter.getValue();
            LocalDate datefinFilter = datefinfilter.getValue();
            String nameFilter = namefilter.getText().trim();
            String organizeFilter = organizefilter.getText().trim();
            String statusFilter = statusfilter.getText().trim();

            boolean matchAll = matchallbtn.isSelected();

            // Filter the events based on the entered criteria
            List<Event> filteredEvents = eventService.filterEvents(categoryFilter, datedebutFilter, datefinFilter, nameFilter, organizeFilter, statusFilter, matchAll);

            // Update the event table with the filtered events
            eventObservableList.setAll(filteredEvents);
            eventtable.setItems(eventObservableList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the SQL exception, e.g., show an error message
            showAlert("Error", "Error applying filter", ex.getMessage());
        }
    }

    @FXML
    void matchall(ActionEvent event) {matchatleastonebtn.setSelected(false);
    }

    @FXML
    void matchatleastone(ActionEvent event) {matchallbtn.setSelected(false);
    }
    @FXML
    void removefilter(ActionEvent event) {
        categoryfilter.getSelectionModel().clearSelection();
        datedebutfilter.setValue(null);
        datefinfilter.setValue(null);
        namefilter.clear();
        organizefilter.clear();
        statusfilter.clear();
        matchallbtn.setSelected(false);
        matchatleastonebtn.setSelected(false);

        initializeEventTab();

    }
    private void populateCategoryComboBox() {
        try {
            List<String> categories = categoryService.getAllCategoryNames();
            categoryfilter.setItems(FXCollections.observableArrayList(categories));
        } catch (SQLException e) {
            showAlert("Error", "Failed to load categories", e.getMessage());
        }
    }

    void initializeCheckBoxes() {
        // Listener for column header checkbox
        selectAllCheckBox.setOnAction(event -> {
            for (Event event1 : eventObservableList) {
                event1.setSelected(selectAllCheckBox.isSelected());
            }
        });
    }

    //checkbuttons
    void setupColumns() {
        // Setup checkbox column
        checkboxColumn.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            if (event == null) {
                return new SimpleBooleanProperty(false).asObject();
            } else {
                return event.selectedProperty();
            }
        });

        // Set cell factory conditionally to avoid checkboxes in empty cells
        checkboxColumn.setCellFactory(column -> new TableCell<Event, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    Event rowData = getTableRow().getItem();
                    if (rowData != null) {
                        rowData.setSelected(checkBox.isSelected());
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(checkBox);
                    if (item != null) {
                        checkBox.setSelected(item);
                    }
                } else {
                    setGraphic(null);
                }
            }
        });

        // Setup checkbox in the header
        CheckBox selectAllCheckBox = new CheckBox();
        selectAllCheckBox.setOnAction(event -> {
            for (Event eventItem: eventObservableList) {
                eventItem.setSelected(selectAllCheckBox.isSelected());
            }
        });
        checkboxColumn.setGraphic(selectAllCheckBox);
    }

    //actionbuttons
    void setupActionColumn() {
        actioncolumn.setCellFactory(param -> new TableCell<Event, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");

            {
                // Define action for delete button
                deleteButton.setOnAction(event -> {
                    Event event2 = getTableView().getItems().get(getIndex());
                    // Perform delete action
                    deleteEvent(event2);
                });

                // Define action for update button
                updateButton.setOnAction(event -> {
                    Event event2 = getTableView().getItems().get(getIndex());
                    // Perform update action
                    openUpdateWindow(event2);
                });
                // Apply style classes to buttons
                // Apply styles to buttons

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Add both buttons to a HBox for layout
                    HBox buttonsBox = new HBox(deleteButton, updateButton);
                    buttonsBox.setSpacing(5); // Adjust spacing between buttons if needed
                    setGraphic(buttonsBox);
                }
            }
        });
    }

    void deleteEvent(Event event) {
        boolean confirmed = ConfirmationDialog.showConfirmationDialog("Are you sure you want to delete this event?");
        if (confirmed) {
            // Implement the logic to delete the event
            try {
                // Assuming you have a method in your service class to delete an event by its ID
                eventService.delete(event.getIdevent());
                // If deletion is successful, you might want to update your UI accordingly
                eventObservableList.remove(event);
                showInformationAlert("Success", "Event deleted successfully");
            } catch (SQLException e) {
                showAlert("Error", "Error deleting event", e.getMessage());
            }
        }
    }






    void openUpdateWindow(Event event) {
        try {
            // Load the update event window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/addevent.fxml"));
            Parent root = loader.load();

            // Access the controller of the update event window
            Addevent addevent= loader.getController();

            // Pass the event data to the update window controller
            addevent.initDataForUpdate(event);

            // Create a new stage for the update event window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Event");

            // Set the modality of the stage to APPLICATION_MODAL
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the update event window
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load update event window", e.getMessage());
        }
    }
    @FXML
    void backtouser(ActionEvent event) {
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
            Stage currentStage = (Stage) settingslink.getScene().getWindow();
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
    void navigatetoCategories(ActionEvent event) {
        try {
            // Load the profile window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/CategoryCrud.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("category table");
            stage.setScene(new Scene(root));

            // Apply the transition animation
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), root);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();
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


