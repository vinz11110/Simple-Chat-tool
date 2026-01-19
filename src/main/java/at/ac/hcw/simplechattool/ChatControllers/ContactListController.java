package at.ac.hcw.simplechattool.ChatControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import at.ac.hcw.simplechattool.ChatApp;
import java.io.IOException;

public class ContactListController {

    @FXML
    private VBox contactContainer;

    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "HubScreen.fxml");
    }

    private void addContactCard(int ID, String name) {
        if (contactContainer == null) {
            return;
        }

        HBox card = new HBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setSpacing(15);
        card.setPadding(new Insets(10,10,10,20));

        card.setStyle("-fx-background-color: white;" +
                      "-fx-background-radius: 15;" +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1)");

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button chatButton = new Button("Chat");
        chatButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 20;");
        chatButton.setOnAction(e -> {
            System.out.println("Start chat with: " + name + " (" + ID + ")");
            if (ChatApp.connection != null) {
                try {
                    ChatApp.connection.setConnectID2(ID);

                    ChatApp.connection.setOtherNickname(name);

                    SceneSwitcher.switchScene(e, "/at/ac/hcw/simplechattool/ChatScreen.fxml");
                }   catch (IOException except) {
                    except.printStackTrace();
                }
            }   else {
                System.out.println("ERROR: No connection to server");
            }
        });

        card.getChildren().addAll(nameLabel, spacer, chatButton);
        contactContainer.getChildren().add(card);
    }
    public void addContact(int ID, String name){
            addContactCard(ID, name);
    }

}
