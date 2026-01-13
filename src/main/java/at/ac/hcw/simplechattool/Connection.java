package at.ac.hcw.simplechattool;
import javafx.application.Platform;
import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection extends Thread {
    private final int port = ;
    private int connectID2;
    private String myIP;
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private final MessageHandler messageHandler;
    private Scene3Controller controller;
    private int connectionID;

    public Connection(Scene3Controller controller, MessageHandler messageHandler) throws UnknownHostException {
        this.messageHandler = messageHandler;
        this.controller = controller;
        this.myIP = Inet4Address.getLocalHost().getHostAddress();
        this.start();
    }

    public void run() {
        try {
            socket = new Socket(, );
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        while (!socket.isClosed()) {
            try {
                Object obj = in.readObject();
                ChatMessage message = (ChatMessage) obj;
                if (message.getType()==1) {
                    Platform.runLater(() -> {
                        controller.displayMessage(message);
                    });
                    int receivedMessageID = (int) message.getMessageID();
                    sendMessage(receivedMessageID, 2);
                } else if (message.getType()==3) {
                    this.connectionID = message.getContent();
                } else if (message.getType()==2 && messageHandler.findMessageID(message.getMessageID())) {
                    messageHandler.findMessageByID((int) obj).markAsReceived();
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        ChatMessage messageObj = new ChatMessage(connectionID, message, 0, 1);
        this.send(messageObj);
    }
    public void sendMessage(int message, int type) throws IOException {
        ChatMessage messageObj = new ChatMessage(connectionID, message, 0, type);
        this.send(messageObj);
    }

    public void send(Object message) throws IOException {
        int sentMessageID;
        if (!socket.isClosed()) {
            out.writeObject(message);
            out.flush();
            if ((ChatMessage)message.getType=1) {
                sentMessageID = ((ChatMessage) message).getMessageID();
                messageHandler.addMessage((ChatMessage) message);
                messageHandler.findMessageByID(sentMessageID).markAsSent();
            }
        }
    }

    public void shutdown() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    public void reconnect() {
        try {
            socket.close();
            in.close();
            this.socket = new Socket(, );
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            setConnectID2(connectID2);

        } catch (IOException x) {
            System.out.println(x);
            return;
        }
    }

    public int getConnectionID() {
        return connectionID;
    }

    public void setConnectID2(int connectID2) throws IOException {
        this.connectID2 = connectID2;
        this.sendMessage(connectID2, 3);
    }
}
