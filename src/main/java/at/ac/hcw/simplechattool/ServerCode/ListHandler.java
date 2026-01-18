package at.ac.hcw.simplechattool.ServerCode;

import java.util.*;

public class ListHandler {
    private List<DeviceLink> linkList = new ArrayList<>();
    private List<Conversation> convList = new ArrayList<>();
    private List<ServerConnection> connectList = new ArrayList<>();

    public int checkAndSetConnectID(String deviceID) {
        for(DeviceLink links: linkList){
            if(links.getDeviceID().equals(deviceID)){
                return links.getConnectID();
            }
        }
        DeviceLink newLink = new DeviceLink(deviceID);
        linkList.add(newLink);
        return newLink.getConnectID();
    }
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
