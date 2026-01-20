package at.ac.hcw.simplechattool.ChatControllers;

import at.ac.hcw.simplechattool.ChatApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.application.Platform;

import java.io.IOException;

//Controller for first Screen where the user is able to pick their nickname and enter the ID of the person it wants to communicate with
public class StartScreenController {

    @FXML
    private TextField IpField;

    @FXML
    private Label ownIdLabel;

    @FXML
    private TextField nicknameField;

    //Handles "Connect" Button click.
    @FXML
    protected void onConnectionClick(ActionEvent event) throws IOException {
        String ID = IpField.getText();
        String nickname = nicknameField.getText();

        //Check if fields are empty
        if (ID == null || ID.trim().isEmpty() || nickname == null || nickname.trim().isEmpty()) {
            showAlert("Error", "Please enter a valid ID");
            return;
        }
        try {
            int targetID = Integer.parseInt(ID);
            if (ChatApp.connection != null) {
                //Sets the users details
                ChatApp.connection.setNickname(nickname);
                ChatApp.connection.setConnectID2(targetID);

                System.out.println("Connecting with ID: " + targetID);
                SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/ChatScreen.fxml");
            } else {
                showAlert("ERROR", "No connection to server");
            }
        } catch (NumberFormatException e) {
            showAlert("ERROR", "ID must be a number");
        }
    }


    //Error display
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Enables the controller with the main app to receive updates. Gets called when FXML is loaded
    @FXML
    public void initialize() {
        ChatApp.print(763);
            if (ChatApp.connection != null) {
                ChatApp.connection.setStartController(this);
                ChatApp.printText(String.valueOf(this.getClass()));
        }
    }

    //Updates the UI with the user's ID assigned by the server connection
    public void updateId(int id) {
        Platform.runLater(() -> {
            if (ownIdLabel != null) {
                ownIdLabel.setText("Your ID: " + String.valueOf(id));
            }
        });
    }
}
