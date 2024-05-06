module controllers.bloodify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.dlsc.formsfx;
    requires java.mail;
    requires org.apache.logging.log4j;
    requires org.apache.poi.poi;
    requires jbcrypt;
    requires bcrypt; // Add this line to include bcrypt module

//    requires javafx.web; // Add this line
    // ... other dependencies

    opens controllers.bloodify to javafx.fxml;
    opens main; // Open the main package to other modules
    exports controllers.bloodify;
    exports controllers.bloodify.events;
    opens controllers.bloodify.events to javafx.fxml;
}
