package demo.api.testdata.generators;

import com.github.javafaker.Faker;

import demo.api.dto.Contact;

public class ContactGenerator {
    private static final Faker faker = new Faker();

    public static Contact generateContact() {
        return new Contact(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.date().birthday(),
            faker.internet().emailAddress(),
            faker.phoneNumber().phoneNumber(),
            faker.address().streetAddress(),
            faker.address().secondaryAddress(),
            faker.address().city(),
            faker.address().state(),
            faker.address().zipCode(),
            faker.address().country()
        );
    }
}
