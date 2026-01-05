package at.ac.hcw.simplechattool;

import java.sql.Time;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    private final String sender;
    private final String content;
    private final int messageID;
    private static int nextMessageID = 1;

    public ChatMessage(String sender, String content){
        this.sender = sender;
        this.content = content;
        this.messageID = nextMessageID++;

    }


    public String getMessage(String sender, String content){
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s: \n%s", time, sender, content);
    }

    public String getSender(){
        return sender;
    }

    public String getContent(){
        return content;
    }

    public int getMessageID(){
        return messageID;
    }

}
