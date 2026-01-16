package at.ac.hcw.simplechattool.ServerCode;

public class Conversation {

    private int ID1;
    private int ID2;
    private final int conversationID;
    private static int convoID = 1;


    public Conversation(){
        this.conversationID = convoID++;
    }

    public int getConversationID(){
        return conversationID;
    }
    public int getConnectionID(){
        return ID1;
    }
    public int getConnectionID2(){
        return ID2;
    }

    public void setConnectionID(int ID){
        this.ID1 = ID;
    }
    public void setConnectionID2(int ID2){
        this.ID2 = ID2;
    }
}
