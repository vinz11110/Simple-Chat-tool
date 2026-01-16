package at.ac.hcw.simplechattool.ServerCode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionAutoCheck extends Thread {
    private final int port = 5000;
    private Socket socket = null;
    private ServerSocket serverSocket;

    public ConnectionAutoCheck() throws IOException {
        serverSocket = new ServerSocket(port);
    }
    public void run() { //repeatedly checks for new connection requests from clients and creates new Connection instances accordingly
        while (true) {
            try {
                socket = serverSocket.accept();
                ServerConnection connection = new ServerConnection(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
