module at.ac.hcw.simplechattool {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.base;
    requires at.ac.hcw.simplechattool;


    opens at.ac.hcw.simplechattool to javafx.fxml;
    exports at.ac.hcw.simplechattool;
    exports at.ac.hcw.simplechattool.ChatControllers;
    opens at.ac.hcw.simplechattool.ChatControllers to javafx.fxml;
}