module controllers.bloodify {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens controllers.bloodify to javafx.fxml;
    exports controllers.bloodify;
}