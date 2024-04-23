package controllers.bloodify;

import Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import models.user.User;
import services.UserService;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.io.IOException;

public class DashboardController  {
    @FXML
    private Hyperlink settingslink;
    @FXML
    private Button logoutbtn ;
    @FXML
    private Button usersbtn ;
    private User currentUser;
    @FXML
    private PieChart piechart;
    private UserService userService = new UserService(); // Instance of UserService


    @FXML
    private BarChart<String, Number> barchart;

    @FXML
    public void initialize() {
        loadPieChartData();
        loadBarChartData();
        for (XYChart.Series<String, Number> series : barchart.getData()) {
            // Get the list of data points in the series
            for (XYChart.Data<String, Number> data : series.getData()) {
                // Set the color for each bar
                data.getNode().setStyle("-fx-bar-fill: royalblue;");
            }
        }
        barchart.legendVisibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                for (Node legendItem : barchart.lookupAll(".chart-legend-item")) {
                    legendItem.setStyle("-fx-background-color: royalblue;");
                }
            }
        });
    }

    private void loadPieChartData() {
        List<User> users = userService.getAllUsers();
        Map<String, Long> domainCounts = users.stream()
                .map(user -> user.getEmail().substring(user.getEmail().indexOf("@") + 1))
                .collect(Collectors.groupingBy(domain -> domain, Collectors.counting()));

        PieChart.Data[] pieData = domainCounts.entrySet().stream()
                .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                .toArray(PieChart.Data[]::new);

        piechart.getData().addAll(pieData);
    }

    private void loadBarChartData() {
        List<User> users = userService.getAllUsers();
        Map<String, Long> roleCounts = users.stream()
                .map(User::getRoles)
                .collect(Collectors.groupingBy(role -> role, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        roleCounts.forEach((role, count) -> series.getData().add(new XYChart.Data<>(role, count)));

        barchart.getData().add(series);
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
    public void onusers() {
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
}
