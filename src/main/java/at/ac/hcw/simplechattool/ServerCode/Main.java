package at.ac.hcw.simplechattool.ServerCode;

import java.io.IOException;

public class Main extends Thread {
    public static void main(String[] args) throws IOException {
        ConnectionAutoCheck autoCheck = new ConnectionAutoCheck();
        autoCheck.start();
    }
}
