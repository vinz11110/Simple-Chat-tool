package at.ac.hcw.simplechattool;

import java.io.Serializable;

public class Contact implements Serializable {
    private int ID;
    private String name;
    private static final long serialVersionUID = 1L;
    public Contact(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
