package demo.api.services;

import static io.restassured.RestAssured.given;

import demo.api.clients.ResponseHandler;
import demo.api.config.Config;
import demo.api.constants.HttpStatusCodes;
import demo.api.dto.Users.request.LogInUser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Users {
    private static final String LOGIN_ENDPOINT = "/users/login";
    private RequestSpecification spec;

    public Users(RequestSpecification spec) {
        this.spec = spec;
    }

    public String getAuthToken(LogInUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.username() == null || user.username().isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (user.password() == null || user.password().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        Response response = given()
                .spec(spec)
                .baseUri(Config.getBaseUrl())
                .body(user)
                .when()
                .post(LOGIN_ENDPOINT);
        
        ResponseHandler.handleResponse(response, LOGIN_ENDPOINT, "POST", HttpStatusCodes.OK.getValue());
        
        return response.then()
                .extract()
                .response().as(demo.api.dto.Users.response.LogInUser.class)
                .token();
    }
}
