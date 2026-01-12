package at.ac.hcw.simplechattool;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;

import javax.swing.*;

public class Scene1Controller {

    @FXML
    private TextField IpField;

    @FXML
    protected void onConnectionClick(ActionEvent event) {
        String ip = IpField.getText();

        if (ip == null || ip.trim().isEmpty()) {
            showAlert("Error", "Please enter an IP-address!");
            return;
        }
        System.out.println("Trying to connect to " + ip);
        SceneSwitcher.switchScene(event, "chat-view.fxml");
    }

    @FXML
    protected void onProfileClick(ActionEvent event) {
        System.out.println("Switching to Profile Login");
        SceneSwitcher.switchScene(event, "login-view.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
