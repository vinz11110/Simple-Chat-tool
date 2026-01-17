package at.ac.hcw.simplechattool.ServerCode;

public class DeviceLink {
    private String deviceID;
    private static int connectIDs = 10000000;
    private int connectID;

    public DeviceLink(String deviceID){
        this.deviceID=deviceID;
        this.connectID=connectIDs;
        connectIDs++;
    }

    public int getConnectID() {
        return connectID;
    }

    public String getDeviceID() {
        return deviceID;
    }
}
