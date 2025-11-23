package demo.api.services;

import static io.restassured.RestAssured.given;

import demo.api.config.Config;
import demo.api.dto.Users.request.LogInUser;
import io.restassured.specification.RequestSpecification;

public class Users {
    private RequestSpecification spec;

    public Users(RequestSpecification spec) {
        this.spec = spec;
    }

    public String getAuthToken(LogInUser user) {
        return given()
                .spec(spec)
                .baseUri(Config.getBaseUrl())
                .body(user)
                .when()
                .post("/users/login")
                .then()
                .extract()
                .response().as(demo.api.dto.Users.response.LogInUser.class)
                .token();
    }
}
