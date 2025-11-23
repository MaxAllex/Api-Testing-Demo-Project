package demo.api.testdata;

import demo.api.dto.Contacts.request.Contact;
import demo.api.services.Contacts;
import demo.api.testdata.generators.ContactGenerator;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.List;

public class TestDataManager {
    private static final ThreadLocal<List<String>> createdContactIds = new ThreadLocal<>();
    private final Contacts contactsService;

    public TestDataManager(RequestSpecification spec) {
        this.contactsService = new Contacts(spec);
    }

    public String createContact() {
        Contact contact = ContactGenerator.generateContact();
        var response = contactsService.addContact(contact);
        String contactId = response.thenReturn().as(demo.api.dto.Contacts.response.Contact.class)._id();
        addContactId(contactId);
        return contactId;
    }

    public String createContact(Contact contact) {
        var response = contactsService.addContact(contact);
        String contactId = response.thenReturn().as(demo.api.dto.Contacts.response.Contact.class)._id();
        addContactId(contactId);
        return contactId;
    }


    public void cleanup() {
        List<String> ids = createdContactIds.get();
        if (ids != null) {
            for (String id : ids) {
                try {
                    contactsService.deleteContact(id);
                } catch (Exception e) {
                }
            }
            ids.clear();
        }
        createdContactIds.remove();
    }

    public List<String> getCreatedContactIds() {
        List<String> ids = createdContactIds.get();
        return ids != null ? new ArrayList<>(ids) : new ArrayList<>();
    }

    public void addContactId(String contactId) {
        List<String> ids = createdContactIds.get();
        if (ids == null) {
            ids = new ArrayList<>();
            createdContactIds.set(ids);
        }
        ids.add(contactId);
    }
}

