package at.ac.hcw.simplechattool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


public class AfterLoginScreenController {

    @FXML
    private TextField ipField;

    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "LoginScreen.fxml");
    }

    @FXML
    protected void onContactClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "ContactList.fxml");
    }

    @FXML
    protected void onIpConnect(ActionEvent event) {
        String ip = ipField.getText();

        if (ip != null && !ip.trim().isEmpty()) {
            SceneSwitcher.switchScene(event, "ChatScreen.fxml");
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
