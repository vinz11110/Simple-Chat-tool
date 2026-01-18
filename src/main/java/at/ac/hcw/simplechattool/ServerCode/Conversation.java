package at.ac.hcw.simplechattool.ServerCode;

import java.io.IOException;

public class Conversation {

    private int ID1;
    private int ID2;
    private final int convoID;
    private static int nextConvoID = 1;
    private String IDname1;
    private String IDname2;


    public Conversation(int ID1, int ID2){
        this.ID2=ID2;
        this.ID1=ID1;
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
