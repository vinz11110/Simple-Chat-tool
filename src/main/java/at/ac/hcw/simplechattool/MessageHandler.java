package at.ac.hcw.simplechattool;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    private ObservableList<ChatMessage> messages = FXCollections.observableArrayList();

    public ObservableList<ChatMessage> getMessages() {
        return messages;
    }

    public boolean findMessageID(int ID){
        for(ChatMessage message: messages){
            if(message !=null && message.getMessageID() == ID){
                return true;
            }
        }
        return false;
    }

    public ChatMessage findMessageByID(int ID){
        for(ChatMessage message: messages){
            if(ID == message.getMessageID()) {
                return message;
            }
        }
        return null;
    }
    public void addMessage(ChatMessage message){
        if(message != null) messages.add(message);
        else return;
    }

    public void deleteMessage(ChatMessage msg){
        for(ChatMessage message: messages){
            if(message == msg && message != null){
                messages.remove(message);
            }
        }
    }

}
