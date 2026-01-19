package at.ac.hcw.simplechattool.ChatControllers;

import at.ac.hcw.simplechattool.ChatApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;


public class HubScreenController {

    @FXML
    private TextField ipField;

    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/ChatScreen.fxml");
    }

    @FXML
    protected void onContactClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/ContactList.fxml");
    }

    @FXML
    protected void onIpConnect(ActionEvent event) throws IOException {
        String ip = ipField.getText();
        ChatApp.connection.setConnectID2(Integer.parseInt(ip));
        if (ip != null && !ip.trim().isEmpty()) {
            SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/ChatScreen.fxml");
        }
        else {
            showAlert("ERROR", "Please enter a valid ID");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
