package at.ac.hcw.simplechattool;

import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends Thread {
    private final int port = 5000;
    private String ConnectIP;
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private ChatController controller;


    public Connection(String ConnectIP, ChatController controller) {
        this.ConnectIP = ConnectIP;
        this.controller = controller;
        this.start();
    }


    public void run() {
        try {
            if (ConnectIP != null&&!ConnectIP.isEmpty()&& typeof ConnectIP === "string") {
                socket = new Socket(ConnectIP, port);

            } else{
                serverSocket = new ServerSocket(port);
                socket = serverSocket.accept();
                serverSocket.close();
                this.ConnectIP = socket.getInetAddress().getHostAddress();
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
                    Platform.runLater(() -> controller.displayMessage(message));

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(ChatMessage Message) throws IOException {
        if(!socket.isClosed()){
        out.writeObject(Message);
        out.flush();}
        else {
            return;
        }
    }

    public void close() throws IOException {
        socket.close();
        in.close();
    }

    public void reconnect() {
        try {
             this.socket = new Socket(ConnectIP, port);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException x) {
            System.out.println(x);
            return;
        }
    }

}
