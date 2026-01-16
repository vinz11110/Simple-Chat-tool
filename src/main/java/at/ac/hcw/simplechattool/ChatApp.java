package at.ac.hcw.simplechattool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatApp.class.getResource("Scene1.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Orca-Talk");
        stage.setScene(scene);
        stage.show();
        MessageHandler handler = new MessageHandler();
        Connection connection = new Connection(handler);
    }
}
