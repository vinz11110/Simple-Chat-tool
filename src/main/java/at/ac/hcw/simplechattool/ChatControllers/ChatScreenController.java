package at.ac.hcw.simplechattool.ChatControllers;

import at.ac.hcw.simplechattool.ChatApp;
import at.ac.hcw.simplechattool.ChatMessage;
import at.ac.hcw.simplechattool.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;

import java.io.IOException;

public class ChatScreenController {

    @FXML
    private VBox message;

    @FXML
    private TextField messageField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label chatHeaderLabel;

//    @FXML
//    private Label typing;

    @FXML
    protected void onSendMessage(ActionEvent event) throws IOException {
        String message = messageField.getText();
        ChatApp.connection.sendMessage(message, 1);
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
        textFlow.setStyle("-fx-background-color: linear-gradient(to right, #373b44, #4286f4);" + "-fx-background-radius: 20;" + "-fx-padding: 10px;");
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
        textFlow.setStyle("-fx-background-color: #A9A9A9;" + "-fx-background-radius: 20; " + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1"
                + "-fx-padding: 10px");

        textFlow.setPadding(new Insets(10));

        hbox.getChildren().add(textFlow);
        Platform.runLater(() -> message.getChildren().add(hbox));
    }

    public void displayMessage(ChatMessage message) {
        Platform.runLater(() -> addReceivedMessage(message.getContent()));
        Platform.runLater(this::updateChatHeader);
    }

    @FXML
    public void initialize() {
        if (ChatApp.connection != null) {
            ChatApp.connection.setController(this);
            updateChatHeader();
        }
        messageField.setOnAction(event -> {
            try {
                onSendMessage(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
            message.heightProperty().addListener((observable, oldValue, newValue) -> {
                scrollPane.setVvalue(1.0);
            });
        });
    }


    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/StartScreen.fxml");
    }

//    public void updateTyping(int status) {
//        Platform.runLater(() -> {
//            if (typing != null) {
//                if (status == 1) {
//                    typing.setText("is typing...");
//                    typing.setVisible(true);
//                }   else {
//                    typing.setText("");
//                    typing.setVisible(false);
//                }
//            }
//        });
//    }

    public void showRequest(String content) {

    }

    @FXML
    protected void onNewChatClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/HubScreen.fxml");
    }

    @FXML
    protected void onAddContactClick(ActionEvent event) {
        if (ChatApp.connection != null) {
            int otherID = ChatApp.connection.getConnectID2();

            String otherName = ChatApp.connection.getOtherNickname();
            if (otherName == null || otherName.isEmpty()) {
                otherName = "User " + otherID;
            }
            ChatApp.contactHandler.addContact(otherID, otherName);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Saved");
            alert.setHeaderText(null);
            alert.setContentText(otherName + " has been added to your contacts!");
            alert.showAndWait();
        }
    }

    public void updateChatHeader() {
        if (ChatApp.connection != null) {
            int otherID = ChatApp.connection.getConnectID2();
            String nickname = ChatApp.connection.getOtherNickname();

            if (nickname == null || nickname.isEmpty()) {
                Platform.runLater(() -> chatHeaderLabel.setText("Connected to: ID " + otherID));
            }   else {
                Platform.runLater(() -> chatHeaderLabel.setText("Connected to: " + nickname + " (" + otherID + ")"));
            }
        }
    }
}
