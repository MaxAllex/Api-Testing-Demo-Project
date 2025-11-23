package demo.api.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import demo.api.clients.RestClient;
import demo.api.exceptions.NotFoundException;
import demo.api.services.Contacts;
import demo.api.testdata.TestDataManager;
import demo.api.testdata.generators.ContactGenerator;
import demo.api.validators.ContactAssertions;

@Epic("API Testing")
@Feature("Contacts API")
public class ContactTests {
    private Contacts contactsService;
    private TestDataManager testDataManager;
    
    @BeforeEach
    public void setUp() {
        var spec = RestClient.getRequestSpec();
        this.contactsService = new Contacts(spec);
        this.testDataManager = new TestDataManager(spec);
    }

    @Test
    @Tag("smoke")
    @Story("Create Contact")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateContact() {
        var requestContact = ContactGenerator.generateContact();
        var response = contactsService.addContact(requestContact);
        
        var createdContact = ContactAssertions.assertContactCreated(response);
        testDataManager.addContactId(createdContact._id());
        
        ContactAssertions.assertContactMatchesRequest(createdContact, requestContact);
    }

    @Test
    @Tag("smoke")
    @Story("Get All Contacts")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetContacts() {
        var contacts = contactsService.getContacts();
        ContactAssertions.assertContactsListValid(contacts);
    }

    @Test
    @Tag("smoke")
    @Story("Get Contact by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetContact() {
        var newContact = ContactGenerator.generateContact();
        var contactId = testDataManager.createContact(newContact);
        
        var response = contactsService.getContact(contactId);
        var retrievedContact = ContactAssertions.assertContactRetrieved(response);
        
        Assertions.assertEquals(newContact.email(), retrievedContact.email(), "Email should match");
        ContactAssertions.assertValidEmail(retrievedContact);
    }

    @Test
    @Tag("integration")
    @Story("Create and Retrieve Contact")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateAndRetrieveContact() {
        var newContact = ContactGenerator.generateContact();
        testDataManager.createContact(newContact);

        var retrievedContacts = contactsService.getContacts();
        ContactAssertions.assertContactsListValid(retrievedContacts);
        
        boolean contactExists = retrievedContacts.stream()
                .anyMatch(c -> c.email().equals(newContact.email()) &&
                        c.firstName().equals(newContact.firstName()) &&
                        c.lastName().equals(newContact.lastName()));
        Assertions.assertTrue(contactExists, "Created contact not found in retrieved contacts");
    }

    @Test
    @Tag("integration")
    @Story("Delete Contact")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteContact() {
        var requestContact = ContactGenerator.generateContact();
        var contactId = testDataManager.createContact(requestContact);

        var deleteResponse = contactsService.deleteContact(contactId);
        ContactAssertions.assertContactDeleted(deleteResponse);

        Assertions.assertThrows(NotFoundException.class, () -> {
            contactsService.getContact(contactId);
        }, "Deleted contact should not be retrievable");
        
        testDataManager.cleanup();
    }
    
    @Test
    @Tag("negative")
    @Story("Get Non-Existent Contact")
    @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentContact() {
        String nonExistentId = "000000000000000000000000";
        Assertions.assertThrows(NotFoundException.class, () -> {
            contactsService.getContact(nonExistentId);
        }, "Should throw NotFoundException for non-existent contact");
    }

    @Test
    @Tag("negative")
    @Story("Delete Non-Existent Contact")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistentContact() {
        String nonExistentId = "000000000000000000000000";
        Assertions.assertThrows(NotFoundException.class, () -> {
            contactsService.deleteContact(nonExistentId);
        }, "Should throw NotFoundException when trying to delete non-existent contact");
    }

    @Test
    @Tag("negative")
    @Story("Create Contact with Invalid Data")
    @Severity(SeverityLevel.MINOR)
    public void testCreateContactWithNullData() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            contactsService.addContact(null);
        }, "Should throw IllegalArgumentException for null contact");
    }

    @Test
    @Tag("negative")
    @Story("Get Contact with Invalid ID")
    @Severity(SeverityLevel.MINOR)
    public void testGetContactWithNullId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            contactsService.getContact(null);
        }, "Should throw IllegalArgumentException for null contact ID");
    }

    @Test
    @Tag("negative")
    @Story("Get Contact with Invalid ID")
    @Severity(SeverityLevel.MINOR)
    public void testGetContactWithEmptyId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            contactsService.getContact("");
        }, "Should throw IllegalArgumentException for empty contact ID");
    }

    @Test
    @Tag("negative")
    @Story("Delete Contact with Invalid ID")
    @Severity(SeverityLevel.MINOR)
    public void testDeleteContactWithNullId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            contactsService.deleteContact(null);
        }, "Should throw IllegalArgumentException for null contact ID");
    }

    @AfterEach
    public void postTestCleanup() {
        testDataManager.cleanup();
    }
}
