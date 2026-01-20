package at.ac.hcw.simplechattool.ChatControllers;

import at.ac.hcw.simplechattool.ChatApp;
import at.ac.hcw.simplechattool.ChatMessage;
import at.ac.hcw.simplechattool.Contact;
import at.ac.hcw.simplechattool.ContactHandler;
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
import javafx.scene.control.Button;

import java.io.IOException;

//Controller for Chat UI
//Handles sending messages, receiving messages and the contact sidebar
public class ChatScreenController {

    @FXML
    private VBox message;

    @FXML
    private TextField messageField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label chatHeaderLabel;

    @FXML
    private VBox contactSideBar;

    //Handles sending a message
    //Works with pressing "Enter" or the send button
    @FXML
    protected void onSendMessage(ActionEvent event) throws IOException {
        String message = messageField.getText();
        ChatApp.connection.sendMessage(message, 1);
        if (!message.trim().isEmpty()) {
            addSentMessage(message);
            messageField.clear();
        }
    }

    //creates message bubble for sent messages (right aligned and blue)
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

    //creates message bubble for received messages (left aligned and grey)
    private void addReceivedMessage(String text) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text textNode = new Text(text);
        textNode.setStyle("-fx-fill: black; -fx-font-size: 14px;");

        TextFlow textFlow = new TextFlow(textNode);
        textFlow.setStyle("-fx-background-color: #A9A9A9;" + "-fx-background-radius: 20; " + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);"
                + "-fx-padding: 10px;");

        textFlow.setPadding(new Insets(10));

        hbox.getChildren().add(textFlow);
        Platform.runLater(() -> message.getChildren().add(hbox));
    }

    //Method called by Connection when a message arrives
    public void displayMessage(ChatMessage message) {
        Platform.runLater(() -> addReceivedMessage(message.getContent()));
        Platform.runLater(this::updateChatHeader);
    }

    //initialize Method that also adds pressing Enter as a viable way of sending a message and implementing Auto scroll when a new message appears
    @FXML
    public void initialize() {
        if (ChatApp.connection != null) {
            ChatApp.connection.setController(this);
            updateChatHeader();
        }

        loadSideBarContacts();

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

    //Switches to the previous scene
    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/StartScreen.fxml");
    }

    //Redirects to HubScreen.fxml
    @FXML
    protected void onNewChatClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/HubScreen.fxml");
    }

    //Adds the current chat partner to the contact list
    @FXML
    protected void onAddContactClick(ActionEvent event) {
        if (ChatApp.connection != null) {
            int otherID = ChatApp.connection.getConnectID2();
            String otherName = ChatApp.connection.getOtherNickname();

            if (otherName == null || otherName.isEmpty()) {
                otherName = "User " + otherID;
            }

            if (ChatApp.contactHandler != null) {
                ChatApp.contactHandler.addContact(otherID, otherName);

                loadSideBarContacts();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Saved");
                alert.setHeaderText(null);
                alert.setContentText(otherName + " has been added to your contacts!");
                alert.showAndWait();
            }
        }
    }

    //updates the header label to show who we are currently talking to (shows ID and nickname picked by the user)
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

    //loads saved contacts into the sidebar as buttons that also allow the user to switch chats
    private void loadSideBarContacts() {
        if (contactSideBar == null) {
            return;
        }
        contactSideBar.getChildren().clear();

        if (ChatApp.contactHandler != null && ChatApp.contactHandler.getContactList() != null) {
            for (Contact c : ChatApp.contactHandler.getContactList()) {
                Button contactButton = new Button(c.getName());

                contactButton.setPrefWidth(160);

                contactButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT;");

                contactButton.setOnAction(e -> {
                    switchChatTo(c.getID(), c.getName());
                });
                contactSideBar.getChildren().add(contactButton);
            }
        }
    }

    //switches to a different user ID
    private void switchChatTo(int targetID, String targetName) {
        System.out.println("Switching chat to: " + targetName);
        try {
            if (ChatApp.connection != null) {
                ChatApp.connection.setConnectID2(targetID);
                ChatApp.connection.setOtherNickname(targetName);
                updateChatHeader();
                message.getChildren().clear();
            }
        }   catch (IOException except) {
            except.printStackTrace();
        }
    }
}
