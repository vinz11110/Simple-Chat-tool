package at.ac.hcw.simplechattool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactHandler {
    List<Contact> contactList;
    private static final String FILE_PATH = "Files" + File.separator + "Contacts.dat";
    private File contacts;

    public ContactHandler() {
        this.contacts = new File(FILE_PATH);
        if (!contacts.exists()) {
            contactList = new ArrayList<>();
        } else {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(contacts))) {
                contactList = (ArrayList<Contact>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                contactList = new ArrayList<>();
            }}
        for (Contact contact : contactList) {
            controller.AddContact(contact.getID, contact.getName);
        }
    }

    public void addContact(int ID, String name) {
        Contact newContact = new Contact(ID, name);
        contactList.add(newContact);
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(contacts))) {
                out.writeObject(contactList);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
