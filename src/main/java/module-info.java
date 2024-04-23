module controllers.bloodify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.dlsc.formsfx;
    requires java.mail;
//    requires javafx.web; // Add this line
    // ... other dependencies

    opens controllers.bloodify to javafx.fxml;
    opens main; // Open the main package to other modules
    exports controllers.bloodify;
}
