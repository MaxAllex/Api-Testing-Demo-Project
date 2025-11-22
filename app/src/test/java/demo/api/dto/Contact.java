package demo.api.dto;

import java.util.Date;

public record Contact(
        String firstName,
        String lastName,
        Date birthdate,
        String email,
        String phone,
        String street1,
        String street2,
        String city,
        String stateProvince,
        String postalCode,
        String country) {
}
