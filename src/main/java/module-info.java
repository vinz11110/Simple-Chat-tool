module at.ac.hcw.simplechattool {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.ac.hcw.simplechattool to javafx.fxml;
    exports at.ac.hcw.simplechattool;
}