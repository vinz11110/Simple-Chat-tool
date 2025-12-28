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


    public Connection(String ConnectIP, ChatController controller) {
        this.connectIP = ConnectIP;
        this.controller = controller;
        this.start();
    }


    public void run() {                         //multithreading thread used to stop blocking, that happens when a serversocket waits for a first message, from blocking the whole Programm;
        try {
            if (connectIP != null||!connectIP.isEmpty()) {
                socket = new Socket(connectIP, port);

            } else{
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

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(ChatMessage message) throws IOException {
        if(!socket.isClosed()){
        out.writeObject(message);
        out.flush();}
        else {
            return;
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
}
