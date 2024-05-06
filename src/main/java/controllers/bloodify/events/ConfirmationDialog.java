package controllers.bloodify.events;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

public class ConfirmationDialog {
    public static boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.setTitle("Confirmation");

        // Add Confirm and Cancel buttons
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);

        // Show and wait for user interaction
        alert.showAndWait();

        // Return true if Confirm button is clicked, false otherwise
        return alert.getResult() == confirmButtonType;
    }
}
