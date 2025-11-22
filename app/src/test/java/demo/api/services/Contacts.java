package demo.api.services;

import java.util.List;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import io.restassured.specification.RequestSpecification;
import io.restassured.common.mapper.TypeRef;
import demo.api.dto.Contact;

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

    public List<Contact> getContacts() {
        return given()
                .spec(spec)
                .when()
                .get("/contacts/")
                .then()
                .extract()
                .as(new TypeRef<List<Contact>>() {});
    }

    public Contact getContact() {
        return given()
                .spec(spec)
                .when()
                .get("/contacts")
                .then()
                .extract()
                .as(Contact.class);
    }
}
