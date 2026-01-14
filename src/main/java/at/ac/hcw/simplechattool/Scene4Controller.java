package at.ac.hcw.simplechattool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.application.Platform;

public class Scene4Controller {

    @FXML
    private VBox message;

    @FXML
    private TextField messageField;

    @FXML
    protected void onSendMessage(ActionEvent event) {
        String message = messageField.getText();
        if (!message.trim().isEmpty()) {
            addSentMessage(message);
            messageField.clear();
        }
    }

    private void addSentMessage(String text) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text textNode = new Text(text);
        textNode.setStyle("-fx-fill: white; -fx-font-size: 14px;");

        TextFlow textFlow = new TextFlow(textNode);
        textFlow.setStyle("-fx-background-color: linear-gradient(to right, #2af598, #009efd);" + "-fx-background-radius: 20;" + "-fx-padding: 10px;");
        textFlow.setPadding(new Insets(10));

        hbox.getChildren().add(textFlow);
        Platform.runLater(() -> message.getChildren().add(hbox));
    }

    private void addReceivedMessage(String text) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text textNode = new Text(text);
        textNode.setStyle("-fx-fill: black; -fx-font-size: 14px;");

        TextFlow textFlow = new TextFlow(textNode);
        textFlow.setStyle("-fx-background-color: white;" + "-fx-background-radius: 20; " + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1"
                + "-fx-padding: 10px");

        textFlow.setPadding(new Insets(10));

        hbox.getChildren().add(textFlow);
        Platform.runLater(() -> message.getChildren().add(hbox));
    }

    public void displayMessage(ChatMessage message) {
        Platform.runLater(() -> addReceivedMessage(message.getContent()));
    }
}
