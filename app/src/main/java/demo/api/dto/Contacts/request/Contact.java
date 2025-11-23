package demo.api.dto.Contacts.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Contact(
        String firstName,
        String lastName,
        String birthdate,
        String email,
        String phone,
        String street1,
        String street2,
        String city,
        String stateProvince,
        String postalCode,
        String country) {
}
