package at.ac.hcw.simplechattool.ChatControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneSwitcher {

    //Handles navigation between different .fxml files
    public static void switchScene(ActionEvent event, String fxmlFile) {
        try {
            //Loads .fxml file
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlFile));
            Parent root = loader.load();

            //Gets current stage from event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //shows the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error! The file " + fxmlFile + " couldn't be found!");
        }
    }
}
