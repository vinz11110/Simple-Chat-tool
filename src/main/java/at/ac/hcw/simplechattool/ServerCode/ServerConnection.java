package at.ac.hcw.simplechattool.ServerCode;

import at.ac.hcw.simplechattool.ChatMessage;
import java.io.*;
import java.net.Socket;

public class ServerConnection extends Thread {
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private int connectID;
    private int conversID;
    private ListHandler handler;
    private int connectID2;
    private String deviceID;

    public ServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.start();
        handler=Main.handler;
    }

    public void run() {
        //setting up input and output
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
        while (!socket.isClosed()) {
            try {
                //once a message is received, checks message type to correctly handle received info
                Object obj = in.readObject();
                ChatMessage message = (ChatMessage) obj;
                // any type but 3 gets forwarded directly to the other user
                if (message.getType() == 1 || message.getType() == 2 || message.getType() == 4) {
                    forwardMessage(obj, connectID);
                    //type 3: sets the connection ID of the counterpart user, sets or creates a conversation ID for this connection
                } else if (message.getType() == 3) {
                    setConnectID2(message.getContent());
                    setConversID(handler.checkby2IDs(connectID, connectID2));
                } else if (message.getType() == 31) {
                    deviceID = message.getContent();
                    this.connectID = handler.checkAndSetConnectID(deviceID);
                    sendMessage(connectID, 3);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //message sending method used for sending the internal connection ID
    public void sendMessage(int message, int type) throws IOException {
        ChatMessage messageObj = new ChatMessage(connectID, message, type);
        this.send(messageObj);
    }

    public void send(Object message) throws IOException {
        if (!socket.isClosed()) {
            out.writeObject(message);
            out.flush();
        }
    }

    //finds teh counterpart connection and sends the message object through its Connection class
    public void forwardMessage(Object message, int connectionID) {
        handler.searchConversationID(conversID, connectionID).send(message);
    }

    public void setConversID(int ID) {
        this.conversID = ID;
    }

    public int getConnectID() {
        return connectID;
    }

    public void setConnectID2(int connectID2) {
        this.connectID2 = connectID2;
    }
}