package demo.api.services;

import java.util.List;

import demo.api.dto.Contacts.request.Contact;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import io.restassured.specification.RequestSpecification;
import io.restassured.common.mapper.TypeRef;

public class Contacts {
    private RequestSpecification spec;

    public Contacts(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response addContact(Contact contact) {
        return given()
                .spec(spec)
                .body(contact)
                .when()
                .post("/contacts");
    }

    public List<demo.api.dto.Contacts.response.Contact> getContacts() {
        return given()
                .spec(spec)
                .when()
                .get("/contacts/")
                .then()
                .extract()
                .as(new TypeRef<List<demo.api.dto.Contacts.response.Contact>>() {
                });
    }

    public Response getContact(String contactId) {
        return given()
            .spec(spec)
            .when()
            .get("/contacts/" + contactId);
            
    }

    public Response deleteContact(String contactId) {
        return given()
                .spec(spec)
                .when()
                .delete("/contacts/" + contactId);}
}
