package at.ac.hcw.simplechattool.ServerCode;

import java.util.*;

public class ListHandler {
    //    Object used to manage multiple of the used lists of the server
    private List<DeviceLink> linkList = new ArrayList<>();
    private List<Conversation> convList = new ArrayList<>();
    private List<ServerConnection> connectList = new ArrayList<>();

    public ListHandler() {
        int nextConvoID = 0;
        for (Conversation conversation : convList) {
            if (conversation.getConversationID() > nextConvoID) {
                nextConvoID = conversation.getConversationID();
            }
        }
        Conversation.setNextConvoID(nextConvoID);
    }

    //Used for seeing if a device already has a connection ID allocated to it, if not it creates a new one
    public int checkAndSetConnectID(String deviceID) {
        for (DeviceLink links : linkList) {
            if (links.getDeviceID().equals(deviceID)) {
                return links.getConnectID();
            }
        }
        DeviceLink newLink = new DeviceLink(deviceID);
        linkList.add(newLink);
        return newLink.getConnectID();
    }

    //Used to find the counterpart connected user to be sure the message gets routed correctly
    public ServerConnection searchConversationID(int conversID, int connectionID) {
        Conversation getIt = null;
        int user2 = 0;
        for (Conversation convos : convList) {
            if (convos.getConversationID() == conversID) {
                getIt = convos;
                break;
            }
        }
        if (connectionID == getIt.getConnectionID()) {
            user2 = getIt.getConnectionID2();
        } else if (connectionID == getIt.getConnectionID2()) {
            user2 = getIt.getConnectionID();
        }
        for (ServerConnection connection : connectList) {
            if (connection.getConnectID() == user2) {
                return connection;
            }
        }
        return null;
    }

    public void addConversation(Conversation x) {
        convList.add(x);
    }

    public void addServerConnection(ServerConnection x) {
        connectList.add(x);
    }

    //Assures that the connection a new device establishes with the server is the connection used in all further functions
    public void checkServerConnection(ServerConnection x) {
        for (ServerConnection connection : connectList) {
            if (connection.getConnectID() == x.getConnectID()) {
                connectList.remove(connection);
                connectList.add(x);
                return;
            }
        }
        connectList.add(x);
    }

    //Used for seeing if a connection between 2 users already has an ID allocated to it, if not it creates a new one
    public int checkby2IDs(int ID, int ID2) {
        for (Conversation convos : convList) {
            if (convos.getConnectionID() == ID && convos.getConnectionID2() == ID2 || convos.getConnectionID() == ID2 && convos.getConnectionID2() == ID) {
                return convos.getConversationID();
            }
        }
        Conversation convo = new Conversation(ID, ID2);
        convList.add(convo);
        return convo.getConversationID();
    }
}
