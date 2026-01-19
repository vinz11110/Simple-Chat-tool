package at.ac.hcw.simplechattool;

import at.ac.hcw.simplechattool.ChatControllers.ContactListController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactHandler {
    List<Contact> contactList;
    private static final String FILE_PATH = "Files" + File.separator + "Contacts.dat";
    private File contacts;

    public ContactHandler(ContactListController controller) {
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
            controller.addContact(contact.getID(), contact.getName());
        }
    }

    public void addContact(int ID, String name) {
        Contact newContact = new Contact(ID, name);
        contactList.add(newContact);
        saveContacts();


    }
    //removes Contact from Contact List if Contact exists
    public void removeContact(Contact contact){
        contactList.remove(contact);
        saveContacts();
    }

    public void saveContacts(){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(contacts))) {
            out.writeObject(contactList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
