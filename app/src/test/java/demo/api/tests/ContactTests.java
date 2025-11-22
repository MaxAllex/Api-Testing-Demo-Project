package demo.api.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import demo.api.client.RestClient;
import demo.api.services.Contacts;
import demo.api.testdata.generators.ContactGenerator;

public class ContactTests {
    private Contacts contactsService;

    public ContactTests() {
        this.contactsService = new Contacts(RestClient.getRequestSpec());
    }

    @Test
    public void testCreateContact() {
        var contact = contactsService.addContact(ContactGenerator.generateContact());
        var successStatusCode = 200;
        Assertions.assertEquals(successStatusCode, contact.getStatusCode());
    }
}
