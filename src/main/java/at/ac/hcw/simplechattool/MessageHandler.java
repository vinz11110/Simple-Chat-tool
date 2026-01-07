package at.ac.hcw.simplechattool;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    private List<ChatMessage> messages = new ArrayList<>();
    private ChatMessage message;

    public boolean findMessageID(int ID){
        for(ChatMessage message: messages){
            if(message !=null && message.getMessageID() == ID){
                return true;
            }
        }
        return false;
    }

    public Object findMessageByID(int ID){
        for(ChatMessage message: messages){
            if(ID == message.getMessageID()) {
                return message;
            }
        }
        return false;
    }

    public void addMessage(ChatMessage message){
        messages.add(message);
    }

}
