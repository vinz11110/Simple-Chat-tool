package at.ac.hcw.simplechattool;

import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends Thread {
    private final int port = 5000;
    private String connectIP;
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private final MessageHandler messageHandler;
    private int receivedMessageID;
    private int sentMessageID;


    public Connection(String ConnectIP, ChatController controller, MessageHandler messageHandler) {
        this.connectIP = ConnectIP;
        this.messageHandler = messageHandler;
        this.controller = controller;
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
                this.connectIP = socket.getInetAddress().getHostAddress();
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
                    receivedMessageID = (int) message.getMessageID();
                    sendMessage(receivedMessageID);
                } else if (messageHandler.findMessageID((int) obj)) {
                    Platform.runLater(() -> {
                        messageHandler.messages(messageHandler.findMessageID((int) obj)).updateState(2);
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(Object message) throws IOException {
        if (!socket.isClosed()) {
            out.writeObject(message);
            out.flush();
            if (message instanceof ChatMessage) {
                sentMessageID = ((ChatMessage) message).getMessageID();
                messageHandler.messages(sentMessageID).updateState(1);
            }
        } else {
            if (message instanceof ChatMessage) {
                sentMessageID = ((ChatMessage) message).getMessageID();
                messageHandler.messages(sentMessageID).updateState(-1);
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

    public String getConnectIP() {
        return connectIP;
    }


}
