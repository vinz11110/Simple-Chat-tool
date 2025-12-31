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
    private final ChatController controller;
    private String receivedMessageID;
    private String sentMessageID;
    private boolean sentReceived;           //For person doing controller, if possible only allow sending button to call sendMessage if sentReceived is true, to prevent mixup in status update cycle


    public Connection(String ConnectIP, ChatController controller) {
        this.connectIP = ConnectIP;
        this.controller = controller;
        this.start();
    }


    public void run() {                         //multithreading thread used to stop blocking, that happens when a serversocket waits for a first message, from blocking the whole Programm;
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

        while (!socket.isClosed()) {                                            //function will send received ChatMessage messages to a method in the controller to be displayed
            try {
                Object obj = in.readObject();
                if (obj instanceof ChatMessage) {
                    ChatMessage message = (ChatMessage) obj;
                    Platform.runLater(() -> controller.displayMessage(message));
                    receivedMessageID = message.getID;
                    sendMessage(receivedMessageID);
                } else if (((String) obj).equals(sentMessageID)) {
                    message.updateState(2);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(Object message) throws IOException {
        if (!socket.isClosed()) {
            sentMessageID = message.getID;
            out.writeObject(message);
            out.flush();
            if (message instanceof ChatMessage) {
                message.updateState(1);
            }
        } else {
            message.updateState(-1);
        }
    }

    public void shutdown() throws IOException {
        socket.close();
        in.close();
    }

    public void reconnect() {
        try {
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


    public boolean isSentReceived() {
        return sentReceived;
    }
}
