package at.ac.hcw.simplechattool;

import at.ac.hcw.simplechattool.ChatControllers.ContactListController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactHandler {
    //    Object used to manage Contact objects and the its list
    List<Contact> contactList;
    private static final String FILE_PATH = "Files" + File.separator + "Contacts.dat";
    private File contacts;

    //Checks if contacts file contains the needed array or creates a new one
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
            }
        }
    }

    //    Method adds the list of preexisting contacts to the UI to be viewed and used
    public void fillList(ContactListController controller) {
        for (Contact contact : contactList) {
            controller.addContact(contact.getID(), contact.getName());
        }
    }

    //Method to add a contact to the list and directly save the updated list
    public void addContact(int ID, String name) {
        Contact newContact = new Contact(ID, name);
        contactList.add(newContact);
        saveContacts();
    }

    //removes Contact from Contact List if Contact exists
    public void removeContact(Contact contact) {
        contactList.remove(contact);
        saveContacts();
    }

    //    Method check if a contact already existst within the list
    public boolean checkContactExist(int ID) {
        int count = 0;
        for (Contact contact : contactList) {
            if (contact.getID() == ID) {
                count++;
            }

        }
        if (count > 1) {
            return true;
        }
        return false;
    }

    //Saves the contact list back to its file
    public void saveContacts() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(contacts))) {
            out.writeObject(contactList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Contact> getContactList() {
        return contactList;
    }
}
