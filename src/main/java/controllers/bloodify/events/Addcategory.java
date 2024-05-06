package controllers.bloodify.events;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.user.Category;
import models.user.Event;
import services.CategoryService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

public class Addcategory {
    @FXML
    private Button btnsubmit;

    @FXML
    private TextArea descriptionc;

    @FXML
    private TextField namec;

    @FXML
    private ImageView nameemoji;

    @FXML
    private ImageView nameemoji1;
    @FXML
    private ImageView desemoji;

    @FXML
    private ImageView desemoji1;

    @FXML
    private Label eventdeserror;

    @FXML
    private Label eventnameerror;

    private final CategoryService ps = new CategoryService();
    private ObservableList<Category> observableList;
    private Category categoryToUpdate;


    @FXML
    void initialize() {
        // Hide sad emojis initially
        nameemoji.setVisible(false);
        desemoji.setVisible(false);

        // Hide happy emojis initially
        nameemoji1.setVisible(false);
        desemoji1.setVisible(false);

        // Listen for changes in the name text field
        namec.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                nameemoji.setVisible(false);
                nameemoji1.setVisible(true);
            } else {
                nameemoji.setVisible(true);
                nameemoji1.setVisible(false);
            }
        });

        // Listen for changes in the description text area
        descriptionc.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                desemoji.setVisible(false);
                desemoji1.setVisible(true);
            } else {
                desemoji.setVisible(true);
                desemoji1.setVisible(false);
            }
        });
    }

    @FXML
    void submitaction(ActionEvent event) {
        try {
            // Retrieve data from input fields
            String categoryName = namec.getText().trim();
            String categoryDescription = descriptionc.getText().trim();

            // Validate input data
            if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
                eventnameerror.setText("Name cannot be empty");
                eventdeserror.setText("Description cannot be empty");
                nameemoji.setVisible(true);
                desemoji.setVisible(true);
                return;
            } else {
                eventnameerror.setText("");
                eventdeserror.setText("");
                nameemoji1.setVisible(true);
                desemoji1.setVisible(true);
            }

            // Check if the categoryToUpdate object is null
            if (categoryToUpdate == null) {
                // If null, create a new category
                Category newCategory = new Category(categoryName, categoryDescription);
                ps.create(newCategory);
                showInformationAlert("Success", "Category added successfully");
            } else {
                // If not null, update the existing category
                categoryToUpdate.setName(categoryName);
                categoryToUpdate.setDescription(categoryDescription);
                ps.update(categoryToUpdate);
                showInformationAlert("Success", "Category updated successfully");
            }

        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), "");
        }
    }

/*
    @FXML

    void submitaction(ActionEvent event) {
        try {
            // Retrieve data from input fields
            String categoryName = namec.getText().trim();
            String categoryDescription = descriptionc.getText().trim();

            // Validate input data
            if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
                showAlert("Error", "Fields cannot be empty", "");
                return;
            }

            // Check if the categoryToUpdate object is null
            if (categoryToUpdate == null) {
                // If null, create a new category
                Category newCategory = new Category(categoryName, categoryDescription);
                ps.create(newCategory);
                showInformationAlert("Success", "Category added successfully");
            } else {
                // If not null, update the existing category
                categoryToUpdate.setName(categoryName);
                categoryToUpdate.setDescription(categoryDescription);
                ps.update(categoryToUpdate);
                showInformationAlert("Success", "Category updated successfully");
            }

        } catch (SQLException e) {
            showAlert("Error", e.getMessage(), "");
        }
    }


 */




    public void initDataForUpdate(Category category) {
        categoryToUpdate = category;

        // Populate form fields with event data
        descriptionc.setText(category.getName());
        namec.setText(category.getDescription());


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


