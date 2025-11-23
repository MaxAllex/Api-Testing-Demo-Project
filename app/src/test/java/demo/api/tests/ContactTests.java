package demo.api.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import demo.api.client.RestClient;
import demo.api.dto.Contacts.response.Contact;
import demo.api.services.Contacts;
import demo.api.testdata.generators.ContactGenerator;

public class ContactTests {
    private Contacts contactsService;
    private static final ThreadLocal<String> CREATED_CONTACT_ID = new ThreadLocal<>();
    private static final int successStatusCode = 201;
    public ContactTests() {
        this.contactsService = new Contacts(RestClient.getRequestSpec());
    }

    @Test
    @Tag("smoke")
    public void testCreateContact() {
        var contact = contactsService.addContact(ContactGenerator.generateContact());
        Assertions.assertEquals(successStatusCode, contact.getStatusCode());
        String createdId = contact.thenReturn().as(Contact.class)._id();
        if (createdId == null || createdId.isBlank()) {
            createdId = contact.jsonPath().getString("id");
        }
        CREATED_CONTACT_ID.set(createdId);
    }

    @Test
    @Tag("smoke")
    public void testGetContacts() {
        var contacts = contactsService.getContacts();
        Assertions.assertNotNull(contacts);
        Assertions.assertFalse(contacts.isEmpty(), "Contacts list should not be empty");
    }

    @Test
    @Tag("smoke")
    public void testGetContact() {
        var contactId = "69231717dd8c2f001555e159";
        var contact = contactsService.getContact(contactId).then().extract().as(demo.api.dto.Contacts.response.Contact.class);
        Assertions.assertNotNull(contact, "Contact should not be null");
        Assertions.assertNotNull(contact.lastName(), "Contact last name should not be null");
    }

    @Test
    @Tag("integration")
    public void testCreateAndRetrieveContact() {
        var newContact = ContactGenerator.generateContact();
        var createResponse = contactsService.addContact(newContact);
        Assertions.assertEquals(successStatusCode, createResponse.getStatusCode(), "Contact creation failed");
        String createdId2 = createResponse.thenReturn().as(Contact.class)._id();
        CREATED_CONTACT_ID.set(createdId2);

        var retrievedContacts = contactsService.getContacts();
        boolean contactExists = retrievedContacts.stream()
                .anyMatch(c -> c.email().equals(newContact.email()) &&
                        c.firstName().equals(newContact.firstName()) &&
                        c.lastName().equals(newContact.lastName()));
        Assertions.assertTrue(contactExists, "Created contact not found in retrieved contacts");
    }

    @Test
    @Tag("integration")
    public void testDeleteContact() {
        var contact = contactsService.addContact(ContactGenerator.generateContact());
        Assertions.assertEquals(successStatusCode, contact.getStatusCode());
        String contactId = contact.thenReturn().as(Contact.class)._id();
        CREATED_CONTACT_ID.set(contactId);

        var deleteResponse = contactsService.deleteContact(contactId);
        int delStatus = deleteResponse.getStatusCode();
        Assertions.assertTrue(delStatus == 200,
            "Failed to delete contact: status " + delStatus);

        var getDeletedContactResponse = contactsService.getContact(contactId).andReturn().body().asString();
        Assertions.assertTrue(getDeletedContactResponse.isEmpty(), "Deleted contact should not be retrievable");
    }

    @AfterEach
    public void postTestCleanup() {
        String contactId = CREATED_CONTACT_ID.get();
        if (contactId != null && !contactId.isBlank()) {
            Response del = contactsService.deleteContact(contactId);
            int status = del.getStatusCode();
            Assertions.assertTrue(status == 200 || status == 204 || status == 404,
                    "Failed to delete contact: status " + status);
        }
        CREATED_CONTACT_ID.remove();
    }
}
