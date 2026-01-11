package at.ac.hcw.simplechattool;
import javafx.application.Platform;
import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection extends Thread {
    private final int port = 5000;
    private String connectIP;
    private String myIP;
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private final MessageHandler messageHandler;
    private ChatController controller;


    public Connection(String ConnectIP, ChatController controller, MessageHandler messageHandler) throws UnknownHostException {
        this.connectIP = ConnectIP;
        this.messageHandler = messageHandler;
        this.controller = controller;
        this.myIP = Inet4Address.getLocalHost().getHostAddress();
        this.start();
    }


    public void run() {
        try {
            if (connectIP != null && !connectIP.isEmpty()) {
                socket = new Socket(connectIP, port);

            } else {
                serverSocket = new ServerSocket(port);
                socket = serverSocket.accept();
                serverSocket.close();
            }
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
                if (obj instanceof ChatMessage) {
                    ChatMessage message = (ChatMessage) obj;
                    Platform.runLater(() -> {
                        controller.displayMessage(message);
                    });
                    int receivedMessageID = (int) message.getMessageID();
                    sendMessage(receivedMessageID);
                } else if (messageHandler.findMessageID((int) obj)) {
                    Platform.runLater(() -> {
                        messageHandler.findMessageByID((int) obj).markAsReceived();
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(Object message) throws IOException {
        int sentMessageID;

        if (!socket.isClosed()) {
            out.writeObject(message);
            out.flush();
            if (message instanceof ChatMessage) {
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
            this.socket = new Socket(connectIP, port);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

        } catch (IOException x) {
            System.out.println(x);
            return;
        }
    }

    public String myIP() {
        return myIP;
    }


}
