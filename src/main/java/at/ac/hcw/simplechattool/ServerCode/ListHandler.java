package at.ac.hcw.simplechattool.ServerCode;

import java.util.*;

public class ListHandler {
    private List<DeviceLink> linkList = new ArrayList<>();

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
}
