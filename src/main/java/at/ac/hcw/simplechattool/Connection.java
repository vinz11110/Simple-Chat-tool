package at.ac.hcw.simplechattool;

import javafx.application.Platform;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection extends Thread {
    private int connectID2=0;
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private final MessageHandler messageHandler;
    private Scene4Controller controller;
    private int connectID;
    private int checkTyping = 0;
    private int otherTyping = 0;

    public Connection(MessageHandler messageHandler) throws UnknownHostException {
        this.messageHandler = messageHandler;
        this.start();
        this.controller=controller;
    }

    public void run() {
        try {
            //establishing a connection to the hub server, and setting up input and output
            socket = new Socket("yodel-eden.gl.at.ply.gg", 41961);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        while (!socket.isClosed() && connectID2 !=0 && controller!=null) {
            //checks if user is typing, sets bit accordingly
            if (controller.getMessageField.isEmpty) {
                checkTyping = 0;
            } else if (!controller.getMessageField.isEmpty) {
                checkTyping = 1;
            }
            try {
                this.sendMessage(checkTyping, 4);
                Object obj = in.readObject();
                ChatMessage message = (ChatMessage) obj;
                //once a message is received, checks message type to correctly handle received info
                //type 1: simple message, calls message display method
                if (message.getType() == 1) {
                    Platform.runLater(() -> {
                        controller.displayMessage(message);
                    });
                    int receivedMessageID = (int) message.getMessageID();
                    sendMessage(receivedMessageID, 2);
                }//type3: sets the connection ID, to be shared with other users
                else if (message.getType() == 3) {
                    this.connectID = message.getContent();
                } //type 2: updates a specific message's status to received
                else if (message.getType() == 2 && messageHandler.findMessageID(message.getMessageID())) {
                    messageHandler.findMessageByID((int) obj).markAsReceived();
                }//type 4: sends typing status if status changes (1:typing; 0: not typing)
                else if (message.getType() == 4) {
                    if (otherTyping != message.getContent()) {
                        Platform.runLater(() -> {
                            controller.updateTyping(message.getContent());
                        });
                        otherTyping = message.getContent();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //message sending method used for standard test messages
    public void sendMessage(String message) throws IOException {
        ChatMessage messageObj = new ChatMessage(connectID, message, 0, 1);
        this.send(messageObj);
        messageHandler.addMessage(messageObj);
    }
    //message sending method used for sending internal data (IDs/status info)
    private void sendMessage(int message, int type) throws IOException {
        ChatMessage messageObj = new ChatMessage(connectID, message, 0, type);
        this.send(messageObj);
    }
    //sends message objects
    public void send(ChatMessage message) throws IOException {
        if (!socket.isClosed()) {
            out.writeObject(message);
            out.flush();
            if (message.getType = 1) {
                message.markAsSent();
            }
        }
    }

    public void shutdown() throws IOException {
        socket.close();
        in.close();
        out.close();
    }
    //closes current connections and creates new ones, the counterpart connection ID is sent again
    public void reconnect() {
        try {
            socket.close();
            in.close();
            this.socket = new Socket("yodel-eden.gl.at.ply.gg", 41961);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            setConnectID2(connectID2);
        } catch (IOException x) {
            System.out.println(x);
            return;
        }
    }

    public int getConnectID() {
        return connectID;
    }

    public void setController(Scene4Controller controller) {
        this.controller = controller;
    }

    //sets the counterpart connection ID and sends it to the server to be saved
    public void setConnectID2(int connectID2) throws IOException {
        this.connectID2 = connectID2;
        this.sendMessage(connectID2, 3);
    }
}
