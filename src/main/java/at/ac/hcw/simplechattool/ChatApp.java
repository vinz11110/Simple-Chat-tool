package at.ac.hcw.simplechattool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatApp extends Application {
    public static Connection connection;
    @Override
    public void start(Stage stage) throws IOException {
        MessageHandler handler = new MessageHandler();
        connection = new Connection(handler);
        FXMLLoader fxmlLoader = new FXMLLoader(ChatApp.class.getResource("StartScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Orca-Talk");
        stage.setScene(scene);
        stage.show();

    }
    public static void print(int x){
        System.out.println(x);
    }
    public static void printText(String x){
        System.out.println(x);
    }
}
