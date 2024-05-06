package controllers.bloodify;

import Utils.SessionManager;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import models.user.User;
import services.UserService;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.io.IOException;

public class DashboardController  {
    @FXML
    private Button settingslink;
    @FXML
    private Button logoutbtn ;
    @FXML
    private Button usersbtn ;
    private User currentUser;
    @FXML
    private PieChart piechart;
    @FXML
    private AreaChart<?, ?> areachart;
    private UserService userService = new UserService(); // Instance of UserService


    @FXML
    private BarChart<String, Number> barchart;

    @FXML
    public void initialize() {
        visualizePasswordStrength(userService.getAllUsers()); // Call visualizePasswordStrength here
        loadPieChartData();
        loadBarChartData();
        for (XYChart.Series<String, Number> series : barchart.getData()) {
            // Get the list of data points in the series
            for (XYChart.Data<String, Number> data : series.getData()) {
                // Set the color for each bar
                data.getNode().setStyle("-fx-bar-fill: #007bff;");
            }
        }
        barchart.legendVisibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                for (Node legendItem : barchart.lookupAll(".chart-legend-item")) {
                    legendItem.setStyle("-fx-background-color: #007bff;");
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

        // Define mappings from roles to more readable labels
        Map<String, String> roleLabels = new HashMap<>();
        roleLabels.put("\"ROLE_ADMIN\"", "Admin");
        roleLabels.put("\"ROLE_USER\"", "User");
        roleLabels.put("\"ROLE_HOSPITAL\"", "Hospital");

        // Group users by roles and count them
        Map<String, Long> roleCounts = users.stream()
                .map(User::getRoles)
                .collect(Collectors.groupingBy(role -> roleLabels.getOrDefault(role, role), Collectors.counting()));

        // Create series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        roleCounts.forEach((role, count) -> series.getData().add(new XYChart.Data<>(role, count)));

        // Clear existing data and add the series to the bar chart
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
    public void visualizePasswordStrength(List<User> users) {
        // Create a map to store the sum of password strengths and the count of users for each role
        Map<String, Pair<Double, Integer>> roleStrengthMap = new HashMap<>();

        // Iterate through the list of users
        for (User user : users) {
            // Calculate the password strength for the user
            double passwordStrength = calculatePasswordStrength(user.getPassword());

            // Update the sum of password strengths and the count of users for the user's role
            roleStrengthMap.compute(user.getRoles(), (role, pair) -> {
                if (pair == null) {
                    return new Pair<>(passwordStrength, 1);
                } else {
                    double sum = pair.getKey() + passwordStrength;
                    int count = pair.getValue() + 1;
                    return new Pair<>(sum, count);
                }
            });
        }

        // Create a series for the AreaChart
        XYChart.Series series = new XYChart.Series();
        series.setName("Password Strength");

        // Calculate the average password strength for each role and add it to the series
        for (Map.Entry<String, Pair<Double, Integer>> entry : roleStrengthMap.entrySet()) {
            String role = entry.getKey();
            double averageStrength = entry.getValue().getKey() / entry.getValue().getValue(); // Calculate average
            series.getData().add(new XYChart.Data<>(role, averageStrength));
        }

        // Clear existing data and add the series to the AreaChart
        areachart.getData().clear();
        areachart.getData().add(series);
    }

    private double calculatePasswordStrength(String password) {
        // Check if the password meets the minimum length requirement
        if (password.isEmpty() || password.length() < 8) {
            return 0.0; // Return zero strength if the password is too short
        }

        // Initialize counters for different types of characters
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // Define the set of special characters
        String specialChars = "!@#$%^&*()-_=+[{]};:'\"|,<.>/?";

        // Loop through each character in the password
        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (specialChars.contains(String.valueOf(ch))) {
                hasSpecialChar = true;
            }
        }

        // Calculate the strength based on the presence of different types of characters
        int strength = 0;
        if (hasLowercase) strength++;
        if (hasUppercase) strength++;
        if (hasDigit) strength++;
        if (hasSpecialChar) strength++;

        // Return the strength as a percentage of the maximum possible strength
        return (double) strength / 4.0;
    }
    @FXML
    void eventdashboard(ActionEvent event) {
        try {
            // Load the profile window FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/showevent.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Event table");
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

    @FXML
    void oncategory(ActionEvent event) {
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
}
