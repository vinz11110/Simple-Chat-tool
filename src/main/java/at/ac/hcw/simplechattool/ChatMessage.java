package at.ac.hcw.simplechattool;

import java.time.format.DateTimeFormatter;

public class ChatMessage {
    private final String sender;
    private final String content;
    private final int messageID;
    private static int nextMessageID = 1;
    private int currentState = 0; //States can be: 0 Not sent, 1 Sent, 2 Received, 3 Read,
    private int messageType;
//    Message Types:
//    1: Standard text message
//    2: Message Status updater
//    3: ConnectIDs exchange
//    4: Check if typing


    public ChatMessage(String sender, String content, int currentState, int messageType){
        this.sender = sender;
        this.content = content;
        this.messageID = nextMessageID++;
        this.currentState = currentState;
        this.messageType = messageType;

    }


    public String getFormattedMessage(String sender, String content){
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s: \n%s", time, sender, content);
    }

    public void markAsSent(){
        if(getCurrentState() == 0) setCurrentState(1);
    }

    public void markAsReceived(){
        if(getCurrentState() == 1) setCurrentState(2);
    }
    public void markAsRead(){
        if(getCurrentState() == 2) setCurrentState(3);
    }
    public void markAsUnread(){
       if(getCurrentState() > 1) setCurrentState(2);
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

    public int getCurrentState(){
        return currentState;
    }

    public int getType(){
        return messageType;
    }

    public void setCurrentState(int state){
        this.currentState = state;
    }

    public void setMessageType(int type){
        this.messageType = type;
    }
}
