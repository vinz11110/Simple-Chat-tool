package at.ac.hcw.simplechattool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


public class Scene6Controller {

    @FXML
    private TextField ipField;

    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "Scene1.fxml");
    }

    @FXML
    protected void onContactclick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "Scene5contactlist.fxml");
    }

    @FXML
    protected void onIpConnect(ActionEvent event) {
        String ip = ipField.getText();

        if (ip != null && !ip.trim().isEmpty()) {
            SceneSwitcher.switchScene(event, "Scene4.fxml");
        }
        else {
            showAlert("ERROR", "Please enter a valid IP Address");
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
