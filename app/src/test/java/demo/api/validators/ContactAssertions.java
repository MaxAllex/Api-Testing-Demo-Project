package demo.api.validators;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class ContactAssertions {

    public static demo.api.dto.Contacts.response.Contact assertContactCreated(Response response) {
        ResponseValidator.validateStatusCode(response, 201);
        ResponseValidator.validateJson(response);
        ResponseValidator.validateFieldNotNull(response, "_id", "Contact ID");
        
        demo.api.dto.Contacts.response.Contact contact = response.as(demo.api.dto.Contacts.response.Contact.class);
        Assertions.assertNotNull(contact._id(), "Contact ID should not be null after creation");
        Assertions.assertFalse(
            contact._id().isBlank(),
            "Contact ID should not be blank after creation"
        );
        
        return contact;
    }

    public static void assertContactValid(demo.api.dto.Contacts.response.Contact contact) {
        Assertions.assertNotNull(contact, "Contact should not be null");
        Assertions.assertNotNull(contact._id(), "Contact ID should not be null");
        Assertions.assertFalse(contact._id().isBlank(), "Contact ID should not be blank");
        Assertions.assertNotNull(contact.firstName(), "First name should not be null");
        Assertions.assertFalse(contact.firstName().isBlank(), "First name should not be blank");
        Assertions.assertNotNull(contact.lastName(), "Last name should not be null");
        Assertions.assertFalse(contact.lastName().isBlank(), "Last name should not be blank");
        Assertions.assertNotNull(contact.email(), "Email should not be null");
        Assertions.assertFalse(contact.email().isBlank(), "Email should not be blank");
        
        Assertions.assertTrue(
            contact.email().contains("@"),
            String.format("Email '%s' should contain @ symbol", contact.email())
        );
    }

    public static void assertContactMatchesRequest(demo.api.dto.Contacts.response.Contact createdContact, demo.api.dto.Contacts.request.Contact requestContact) {
        Assertions.assertNotNull(createdContact, "Created contact should not be null");
        Assertions.assertNotNull(requestContact, "Request contact should not be null");
        
        Assertions.assertEquals(
            requestContact.firstName(),
            createdContact.firstName(),
            "First name should match request"
        );
        
        Assertions.assertEquals(
            requestContact.lastName(),
            createdContact.lastName(),
            "Last name should match request"
        );
        
        Assertions.assertEquals(
            requestContact.email(),
            createdContact.email(),
            "Email should match request"
        );
        
        if (requestContact.phone() != null) {
            Assertions.assertEquals(
                requestContact.phone(),
                createdContact.phone(),
                "Phone should match request"
            );
        }
    }

    public static void assertContactsListValid(List<demo.api.dto.Contacts.response.Contact> contacts) {
        Assertions.assertNotNull(contacts, "Contacts list should not be null");
        Assertions.assertFalse(
            contacts.isEmpty(),
            "Contacts list should not be empty"
        );
        
        for (demo.api.dto.Contacts.response.Contact contact : contacts) {
            assertContactValid(contact);
        }
    }

    public static demo.api.dto.Contacts.response.Contact assertContactRetrieved(Response response) {
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateJson(response);
        ResponseValidator.validateNotEmpty(response);
        
        demo.api.dto.Contacts.response.Contact contact = response.as(demo.api.dto.Contacts.response.Contact.class);
        assertContactValid(contact);
        
        return contact;
    }

    public static void assertContactDeleted(Response response) {
        int statusCode = response.getStatusCode();
        Assertions.assertTrue(
            statusCode == 200 || statusCode == 204,
            String.format("Expected status code 200 or 204 for deletion, but got %d", statusCode)
        );
    }

    public static void assertContactRequiredFields(Response response) {
        ResponseValidator.validateRequiredFields(
            response,
            List.of("_id", "firstName", "lastName", "email")
        );
    }

    public static void assertValidEmail(demo.api.dto.Contacts.response.Contact contact) {
        Assertions.assertNotNull(contact.email(), "Email should not be null");
        Assertions.assertFalse(contact.email().isBlank(), "Email should not be blank");
        
        String email = contact.email();
        Assertions.assertTrue(
            email.contains("@") && email.contains("."),
            String.format("Email '%s' should be in valid format", email)
        );
        
        Assertions.assertTrue(
            email.indexOf("@") > 0 && email.indexOf("@") < email.length() - 1,
            String.format("Email '%s' should have @ in the middle", email)
        );
    }
}
