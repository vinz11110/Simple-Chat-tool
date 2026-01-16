package at.ac.hcw.simplechattool.ServerCode;

public class Conversation {

    private int ID1;
    private int ID2;
    private final int convoID;
    private static int nextConvoID = 1;


    public Conversation(){
        this.convoID = nextConvoID++;
    }

    public int getConversationID(){
        return convoID;
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
