package at.ac.hcw.simplechattool.ChatControllers;

import at.ac.hcw.simplechattool.ContactHandler;
import at.ac.hcw.simplechattool.Contact;
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

//Manages display and management of saved contacts
public class ContactListController {

    //saves the list of the contacts
    @FXML
    private VBox contactContainer;

    //Switches to the previous scene
    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/at/ac/hcw/simplechattool/HubScreen.fxml");
    }

    //Creates a card and adds it to the list of contacts
    //Contains the users name, a "chat" button to connect to the user and a "remove" button to delete the contact from the list.
    private void addContactCard(int ID, String name) {
        if (contactContainer == null) {
            return;
        }

        //Creates the card
        HBox card = new HBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setSpacing(15);
        card.setPadding(new Insets(10,10,10,20));

        //Design of the card
        card.setStyle("-fx-background-color: #A9A9A9;" +
                      "-fx-background-radius: 15;" +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1)");

        //Design of the users name
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        //Region to put the buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        //Creates "chat" Button
        Button chatButton = new Button("Chat");
        chatButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 20;");
        chatButton.setOnAction(e -> {
            System.out.println("Start chat with: " + name + " (" + ID + ")");
            if (ChatApp.connection != null) {
                try {
                    //updates connection target
                    ChatApp.connection.setConnectID2(ID);
                    ChatApp.connection.setOtherNickname(name);

                    //switches the Scene to the user you want to connect with
                    SceneSwitcher.switchScene(e, "/at/ac/hcw/simplechattool/ChatScreen.fxml");
                }   catch (IOException except) {
                    except.printStackTrace();
                }
            }   else {
                System.out.println("ERROR: No connection to server");
            }
        });

        //Creates "remove" button
        Button deleteButton = new Button("Remove");
        deleteButton.setStyle("-fx-background-color: #D00000; -fx-text-fill; white; -fx-background-radius: 20;");
        deleteButton.setOnAction(e -> {

            //finds contact in the handler and removes it
            if (ChatApp.contactHandler != null && ChatApp.contactHandler.getContactList() != null) {
                Contact toRemove = null;
                for (Contact c : ChatApp.contactHandler.getContactList()) {
                    if (c.getID() == ID) {
                        toRemove = c;
                        break;
                    }
                }
                if (toRemove != null) {
                    ChatApp.contactHandler.removeContact(toRemove);
                    //updates the UI
                    contactContainer.getChildren().remove(card);
                    System.out.println("Contact removed: " + name);
                }
            }
        });

        card.getChildren().addAll(nameLabel, spacer, chatButton, deleteButton);
        contactContainer.getChildren().add(card);
    }

    //Adds contact to the list
    public void addContact(int ID, String name){
        addContactCard(ID, name);
    }

    //Initializes the controller by filling the list with the contacts
    @FXML
    public void initialize() {
        if (ChatApp.contactHandler != null) {
            ChatApp.contactHandler.fillList(this);
        }   else {
            System.out.println("ERROR");
        }
    }
}
