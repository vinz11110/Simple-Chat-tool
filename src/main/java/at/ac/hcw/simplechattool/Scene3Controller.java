package at.ac.hcw.simplechattool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.swing.*;

public class Scene3Controller {

    @FXML
    protected void onBackClick(ActionEvent event) {
        SceneSwitcher.switchScene(event, "Scene1.fxml");
    }

    protected void onSettingsClick(ActionEvent event) {
        System.out.println("Settings clicked");
        //Hier Settings scene einfügen
    }

    @FXML
    protected void onStartChatClick(ActionEvent event) {
        System.out.println("Starting Chat...");
        SceneSwitcher.switchScene(event, "chat-view.fxml");
    }
}
