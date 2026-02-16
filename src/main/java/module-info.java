module Scenes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jdk.jshell;


    opens Scenes to javafx.fxml;
    exports Scenes;
    exports Scenes.Controllers;
    opens Scenes.Controllers to javafx.fxml;
    exports Scenes.PatientControllers;
    opens Scenes.PatientControllers to javafx.fxml;

}