package at.ac.hcw.simplechattool;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.UUID;
import java.io.File;
import java.nio.file.Paths;

public class Connection extends Thread {
    ;
    private int connectID2 = 0;
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private final MessageHandler messageHandler;
    private ChatScreenController controller;
    private int connectID;
    private int otherTyping = 0;
    private String deviceID;
    private static final String FILE_PATH = "Files" + File.separator + "deviceID.txt";
    private File DID;
    private TypingThread thread;
    private StartScreenController startController;

    public Connection(MessageHandler messageHandler) throws IOException {
        this.messageHandler = messageHandler;
        this.start();
        this.DID = new File(FILE_PATH);
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
        if (DID.exists()) {
            try (Scanner fileScanner = new Scanner(DID)) {
                if (fileScanner.hasNextLine()) {
                    String line1 = fileScanner.nextLine();
                    if (!line1.trim().isEmpty()) {
                        deviceID = line1.trim();
                    } else {
                        deviceID = writeUUID();
                    }
                } else {
                    deviceID = writeUUID();
                }
            } catch (FileNotFoundException e) {
                deviceID = writeUUID();
            }
        } else {
            deviceID = writeUUID();
        }
        try {
            this.sendMessage(deviceID, 31);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (!socket.isClosed()) {
            //checks if user is typing, sets bit accordingly
            try {
                Object obj = in.readObject();
                ChatMessage message = (ChatMessage) obj;
                //once a message is received, checks message type to correctly handle received info
                //type 1: simple message, calls message display method
                if (message.getType() == 1 &&  controller != null) {
                    Platform.runLater(() -> {
                        controller.displayMessage(message);
                    });
                    int receivedMessageID = (int) message.getMessageID();
                    sendMessage(receivedMessageID, 2);
                }//type3: sets the connection ID, to be shared with other users
                else if (message.getType() == 3) {
                    this.connectID = message.getContentID();
                    //updating the ID to make it visible in StartScreen.fxml
                    if (startController != null) {
                        startController.updateId(this.connectID);
                    }
                } //type 2: updates a specific message's status to received
                else if (message.getType() == 2 && messageHandler.findMessageID(message.getMessageID())) {
                    messageHandler.findMessageByID(message.getContentID()).markAsReceived();
                }//type 4: sends typing status if status changes (1:typing; 0: not typing)
                else if (message.getType() == 4 && controller != null) {
                    if (otherTyping != message.getContentID()) {
                        Platform.runLater(() -> {
                            controller.updateTyping(message.getContent());
                        });
                        otherTyping = message.getContentID();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String writeUUID() {
        UUID uuidIfNeeded = UUID.randomUUID();
        try (FileWriter writer = new FileWriter(DID)) {
            writer.write(String.valueOf(uuidIfNeeded));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(uuidIfNeeded);
    }

    //message sending method used for standard text messages and internal data
    public void sendMessage(String message, int type) throws IOException {
        ChatMessage messageObj = new ChatMessage(connectID, message, 0, type);
        this.send(messageObj);
        if (type == 1) {
            messageHandler.addMessage(messageObj);
        }
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
            if (message.getType() == 1) {
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


    public void setController(ChatScreenController controller) {
        this.controller = controller;
        thread= new TypingThread(controller, this);
    }

    //sets the counterpart connection ID and sends it to the server to be saved
    public void setConnectID2(int connectID2) throws IOException {
        this.connectID2 = connectID2;
        this.sendMessage(connectID2, 3);
    }

    public int getConnectID() {
        return connectID;
    }

    public void setStartController(StartScreenController startController) {
        this.startController = startController;
    }
}
