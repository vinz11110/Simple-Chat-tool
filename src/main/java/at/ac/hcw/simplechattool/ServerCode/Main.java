package at.ac.hcw.simplechattool.ServerCode;

import java.io.IOException;

public class Main extends Thread {
    public static ListHandler handler;

    public static void main(String[] args) throws IOException {
        handler = new ListHandler();
        ConnectionAutoCheck autoCheck = new ConnectionAutoCheck();
        autoCheck.start();
    }
//Print methods used for continuous observation of server activity
    public static void print(int x) {
        System.out.println(x);
    }

    public static void printText(String x) {
        System.out.println(x);
    }
}
