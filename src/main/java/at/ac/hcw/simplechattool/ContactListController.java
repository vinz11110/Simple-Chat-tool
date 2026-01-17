package at.ac.hcw.simplechattool;

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

public class ContactListController {

    @FXML
    private VBox contactContainer;

    @FXML
    private TextField newContactField;

//    @FXML
//    public void initialize() {
//        addContactCard("Simon Fürsatz");
//        addContactCard("Vinzenz Manu");
//        addContactCard("Björn Stuparek");
//    }

    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "AfterLoginScreen.fxml");
    }

    private void addContactCard(String name) {
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
            System.out.println("Start chat with: " + name);
            SceneSwitcher.switchScene(e, "chat-view.fxml");
        });

        card.getChildren().addAll(nameLabel, spacer, chatButton);
        contactContainer.getChildren().add(card);
    }

    @FXML
    protected void onAddContact(ActionEvent event) {
        String name = newContactField.getText();
        if (name != null && !name.trim().isEmpty()) {
            addContactCard(name);
            newContactField.clear();
        }
    }
}
