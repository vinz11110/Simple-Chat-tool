package at.ac.hcw.simplechattool.ChatControllers;

import at.ac.hcw.simplechattool.ChatApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.application.Platform;

import java.io.IOException;

public class StartScreenController {

    @FXML
    private TextField IpField;

    @FXML
    private Label ownIdLabel;

    @FXML
    private TextField nicknameField;

    @FXML
    protected void onConnectionClick(ActionEvent event) throws IOException {
        String ID = IpField.getText();
        String nickname = nicknameField.getText();

        if (ID == null || ID.trim().isEmpty() || nickname == null || nickname.trim().isEmpty()) {
            showAlert("Error", "Please enter a valid ID");
            return;
        }
        try {
            int targetID = Integer.parseInt(ID);
            if (ChatApp.connection != null) {
                ChatApp.connection.setNickname(nickname);

                ChatApp.connection.setConnectID2(targetID);
                System.out.println("Connecting with ID: " + targetID);
                SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/ChatScreen.fxml");
            } else {
                showAlert("ERROR", "No connection to server");
            }
        }   catch (NumberFormatException e) {
            showAlert("ERROR", "ID must be a number");
        }
    }

//    @FXML
//    protected void onProfileClick(ActionEvent event) {
//        System.out.println("Switching to Profile Login");
//        SceneSwitcher.switchScene(event, "LoginScreen.fxml");
//    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        if (ChatApp.connection != null) {
            ChatApp.connection.setStartController(this);

            if (ChatApp.connection.getConnectID() != 0) {
                updateId(ChatApp.connection.getConnectID());
            }
        }
    }

    public void updateId(int id) {
        Platform.runLater(() -> {
            if (ownIdLabel != null) {
                ownIdLabel.setText("Your ID: " + id);
            }
        });
    }
}
