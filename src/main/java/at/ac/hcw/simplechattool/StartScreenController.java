package at.ac.hcw.simplechattool;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;

import java.io.IOException;

public class StartScreenController {

    @FXML
    private TextField IpField;

    @FXML
    protected void onConnectionClick(ActionEvent event) throws IOException {
        String ID = IpField.getText();
        if (ID == null || ID.trim().isEmpty()) {
            showAlert("Error", "Please enter a valid ID");
            return;
        }
        try {
            int targetID = Integer.parseInt(ID);
            if (ChatApp.connection != null) {
                ChatApp.connection.setConnectID2(targetID);
                System.out.println("Connecting with ID: " + targetID);
                SceneSwitcher.switchScene(event, "ChatScreen.fxml");
            } else {
                showAlert("ERROR", "No connection to server");
            }
        }   catch (NumberFormatException e) {
            showAlert("ERROR", "Please try again");
        }
    }

    @FXML
    protected void onProfileClick(ActionEvent event) {
        System.out.println("Switching to Profile Login");
        SceneSwitcher.switchScene(event, "LoginScreen.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
