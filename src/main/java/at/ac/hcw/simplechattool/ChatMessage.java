package at.ac.hcw.simplechattool;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

public class ChatMessage implements Serializable {
    private int sender; //Unique Identifier of Sender
    private String content;
    private int contentID; //ConnectionID from Connection
    private final int messageID; //Unique identifier for every message
    private static int nextMessageID = 1;
    private static final long serialVersionUID = 1L;
    private String nickname;
    private int currentState = 0;
    //Message States:
    //0 Not sent
    //1 Sent
    //2 Received
    //3 Read
    private int messageType;
//    Message Types:
//    0: used for empty messages, only used to keep connection upright
//    1: Standard text message
//    2: Message Status updater
//    3: ConnectIDs exchange
//    31: Sends device ID to server
//    5: holds information of a messaging rquest

    //Constructor used to send the connection ID specifically
    public ChatMessage(int sender, int contentID, int messageType){
        this.sender = sender;
        this.contentID = contentID;
        this.messageID = nextMessageID++;
        this.messageType = messageType;
    }
    //Contructor used to pass "Frontend" information about the Message Object
    public ChatMessage(int sender, String content, int currentState, int messageType, String nickname){
        this.sender = sender;
        this.content = content;
        this.messageID = nextMessageID++;
        this.currentState = currentState;
        this.messageType = messageType;
        this.nickname =nickname;

    }
    //Contructor to pass internal Data
    public ChatMessage(int sender, int contentID, int currentState, int messageType){
        this.sender = sender;
        this.contentID = contentID;
        this.messageID = nextMessageID++;
        this.currentState = currentState;
        this.messageType = messageType;

    }


    //The full Message in a String format
    public String getFormattedMessage(String sender, String content){
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s: \n%s", time, sender, content);
    }
    //Returns a formatted version of the Current State of the Message used to Display state in UI
    public String getFormattedState(){
        return switch (getCurrentState()) {
            case 0 -> "Not Sent";
            case 1 -> "Sent";
            case 2 -> "Received";
            case 3 -> "Read";
            default -> null;
        };
    }
    //Changes state to Sent
    public void markAsSent(){
        if(getCurrentState() == 0) setCurrentState(1);
    }
    //Changes state to Received
    public void markAsReceived(){
        if(getCurrentState() == 1) setCurrentState(2);
    }

    //Changes state to Read
    public void markAsRead(){
        if(getCurrentState() == 2) setCurrentState(3);
    }
    //Changes state back to Unread
    public void markAsUnread(){
        if(getCurrentState() > 1) setCurrentState(2);
    }

    public int getSender(){
        return sender;
    }

    public String getContent(){
        return content;
    }

    public int getContentID(){
        return contentID;
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
    //Changes Message State
    public void setCurrentState(int state){
        this.currentState = state;
    }
    //Changes Message Type
    public void setMessageType(int type){
        this.messageType = type;
    }

    public String getNickname() {
        return nickname;
    }

    public static void setNextMessageID(int nextID){
        nextMessageID = nextID;
    }

    public int getNextMessageID(){
        return nextMessageID;
    }
}
