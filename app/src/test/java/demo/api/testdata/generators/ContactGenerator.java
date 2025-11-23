package demo.api.testdata.generators;

import java.time.ZoneId;

import com.github.javafaker.Faker;

import demo.api.dto.Contacts.request.Contact;

public class ContactGenerator {
    private static final Faker faker = new Faker();

    public static Contact generateContact() {
        return new Contact(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.date().birthday().toInstant().atZone(ZoneId.of("UTC")).toLocalDate().toString(),
            faker.internet().emailAddress(),
            faker.phoneNumber().cellPhone().replaceAll("(?!\\+)\\D", ""),
            faker.address().streetAddress(),
            faker.address().secondaryAddress(),
            faker.address().city(),
            faker.address().state(),
            faker.address().zipCode(),
            faker.address().country()
        );
    }
}
