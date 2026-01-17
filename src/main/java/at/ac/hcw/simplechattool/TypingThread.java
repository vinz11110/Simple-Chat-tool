package at.ac.hcw.simplechattool;

public class TypingThread extends Thread{
    private ChatScreenController controller;
    Connection connection;
    public TypingThread(ChatScreenController controller, Connection connection){
        this.controller=controller;
        this.connection=connection;
        this.start();
    }
    public void run() {
    while (true) {
        boolean check = controller.checkTyping();
        if(check){
            connection.sendMessage(1,4);
        } else {
            connection.sendMessage(0,4);
        }
    }
    }
}
