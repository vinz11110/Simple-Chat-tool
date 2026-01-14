package at.ac.hcw.simplechattool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import javax.swing.*;

public class Scene2Controller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "Scene1.fxml");
    }

    @FXML
    protected void onLoginClick(ActionEvent event) {
        String user = usernameField.getText();
        String password = passwordField.getText();

        if (user.isEmpty() || password.isEmpty()) {
            showAlert("ERROR", "Please enter username and password!");
            return;
        }
        System.out.println("Login try: " + user);
        SceneSwitcher.switchScene(event, "chat-view.fxml");
    }
}
