package demo.api.services;

import java.util.List;

import demo.api.clients.ResponseHandler;
import demo.api.constants.HttpStatusCodes;
import demo.api.dto.Contacts.request.Contact;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import io.restassured.specification.RequestSpecification;
import io.restassured.common.mapper.TypeRef;

public class Contacts {
    private static final Logger logger = LoggerFactory.getLogger(Contacts.class);
    private static final String CONTACTS_ENDPOINT = "/contacts";
    private RequestSpecification spec;

    public Contacts(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        
        logger.debug("Creating contact with email: {}", contact.email());
        Response response = given()
                .spec(spec)
                .body(contact)
                .when()
                .post(CONTACTS_ENDPOINT);
        
        logger.debug("Contact creation response status: {}", response.getStatusCode());
        return ResponseHandler.handleResponse(response, CONTACTS_ENDPOINT, "POST", HttpStatusCodes.CREATED.getValue());
    }

    public List<demo.api.dto.Contacts.response.Contact> getContacts() {
        Response response = given()
                .spec(spec)
                .when()
                .get(CONTACTS_ENDPOINT + "/");
        
        ResponseHandler.handleResponse(response, CONTACTS_ENDPOINT, "GET", HttpStatusCodes.OK.getValue());
        
        return response.then()
                .extract()
                .as(new TypeRef<List<demo.api.dto.Contacts.response.Contact>>() {
                });
    }

    public Response getContact(String contactId) {
        if (contactId == null || contactId.isBlank()) {
            throw new IllegalArgumentException("Contact ID cannot be null or empty");
        }
        
        logger.debug("Getting contact with ID: {}", contactId);
        String endpoint = CONTACTS_ENDPOINT + "/" + contactId;
        Response response = given()
                .spec(spec)
                .when()
                .get(endpoint);
        
        logger.debug("Get contact response status: {}", response.getStatusCode());
        return ResponseHandler.handleResponse(response, endpoint, "GET", HttpStatusCodes.OK.getValue());
    }

    public Response deleteContact(String contactId) {
        if (contactId == null || contactId.isBlank()) {
            throw new IllegalArgumentException("Contact ID cannot be null or empty");
        }
        
        logger.debug("Deleting contact with ID: {}", contactId);
        String endpoint = CONTACTS_ENDPOINT + "/" + contactId;
        Response response = given()
                .spec(spec)
                .when()
                .delete(endpoint);
        
        logger.debug("Delete contact response status: {}", response.getStatusCode());
        return ResponseHandler.handleResponse(response, endpoint, "DELETE", 
                HttpStatusCodes.OK.getValue(), HttpStatusCodes.NO_CONTENT.getValue());
    }
}
