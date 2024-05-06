package controllers.bloodify.events;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import models.user.Event;
import services.EventService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Eventpage {


    @FXML
    private GridPane eventgrid;



    @FXML
    private Button searchbtn;





    @FXML
    public void initialize() {
        populateEventPage();
    }

    public void populateEventPage() {
        try {
            // Fetch events from the data source
            EventService eventService = new EventService();
            List<Event> events = eventService.read();

            // Clear existing items from the grid
            eventgrid.getChildren().clear();

            // Set column constraints for the grid pane
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(33); // Divide the grid into 3 equal columns
            eventgrid.getColumnConstraints().addAll(columnConstraints, columnConstraints, columnConstraints);

            // Set row constraints for the grid pane
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(236.0); // Set the preferred height for each row
            eventgrid.getRowConstraints().addAll(Collections.nCopies(events.size() / 3 + 1, rowConstraints));

            // Add event items to the grid
            int col = 0;
            int row = 0;
            for (Event event : events) {
                try {
                    // Load event item FXML for each iteration
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/items.fxml"));

                    // Load event item
                    AnchorPane item = loader.load();
                    Items controller = loader.getController();
                    controller.setEvent(event); // Set event data in the item controller
                    eventgrid.add(item, col, row);

                    // Increment column for grid layout
                    col++;
                    if (col == 3) { // Example: Display 3 items per row
                        col = 0;
                        row++;
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Handle error loading item FXML
                }
            }
            // Set padding between rows
            eventgrid.setVgap(10); // Adjust the value as needed
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }



    @FXML
    void findeventsaction(ActionEvent event) {

    }
    @FXML
    void handleLogout(ActionEvent event) {

    }

    @FXML
    void oneventpage(ActionEvent event) {

    }

    @FXML
    void onsettings(ActionEvent event) {

    }

/*
    @FXML
    void searchaction(ActionEvent event) {
        try {

            EventService eventService = new EventService();
            List<Event> events = eventService.read();

            // Get search criteria from text fields
            LocalDate startDate = StartD.getValue();
            LocalDate endDate = EndD.getValue();
            String organizer = Organizer.getText();

            // Filter events based on search criteria
            List<Event> filteredEvents = eventService.filterev(events, startDate, endDate, organizer);

            // Clear existing items from the grid
            eventgrid.getChildren().clear();

            // Add filtered event items to the grid
            int row = 0;
            int col = 0;
            for (Event event1: filteredEvents) {
                try {
                    // Load event item FXML for each iteration
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/items.fxml"));

                    // Load event item
                    Node item = loader.load();
                    Items controller = loader.getController();
                    controller.setEvent(event1); // Set event data in the item controller
                    eventgrid.add(item, col, row);

                    // Increment row and column for grid layout
                    col++;
                    if (col == 4) {
                        col = 0;
                        row++;
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Handle error loading item FXML
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

*/


}
