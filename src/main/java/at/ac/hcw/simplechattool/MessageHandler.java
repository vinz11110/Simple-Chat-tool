package at.ac.hcw.simplechattool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    private List<ChatMessage> messages;
    private static final String FILE_PATH = "Files" + File.separator + "Messages.dat";
    private final File messagesFile;


    public MessageHandler(){
        this.messagesFile = new File(FILE_PATH);
        File folder = new File("Files");
        //tries to create new folder if it doesn't exist and throws Exception if it cant create it
        if(!folder.exists()){
            boolean h = folder.mkdirs();
            if(!h){
                throw new RuntimeException("Could not create new folder: " + folder.getAbsolutePath());
            }
        }

        if(!messagesFile.exists()){
            messages = new ArrayList<>();
        }
        else {
            try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(messagesFile))){
                messages = (ArrayList<ChatMessage>) in.readObject();
            }
            catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                messages = new ArrayList<>();
            }
        }

        int maxID = 0;
        for(ChatMessage msg: messages){
            if(msg != null && msg.getMessageID() > maxID){
                maxID = msg.getMessageID();
            }
        }
        ChatMessage.setNextMessageID(maxID +1);
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }
    //returns true of Message is found else returns false
    public boolean findMessageID(int ID){
        for(ChatMessage message: messages){
            if(message !=null && message.getMessageID() == ID){
                return true;
            }
        }
        return false;
    }
    //returns message Object if message ID exists otherwise returns null
    public ChatMessage findMessageByID(int ID){
        for(ChatMessage message: messages){
            if(message != null && ID == message.getMessageID()) {
                return message;
            }
        }
        return null;
    }
    //adds Message Object to Messages List
    public void addMessage(ChatMessage message){
        if(message != null) messages.add(message);
        else return;
        saveMessage();
    }
    //Deletes Messages from List if message exists in Messages List
    public void deleteMessage(ChatMessage msg){
        messages.remove(msg);
        saveMessage();
    }

    public void saveMessage(){
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(messagesFile))){
            outputStream.writeObject(new ArrayList<>(messages));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
