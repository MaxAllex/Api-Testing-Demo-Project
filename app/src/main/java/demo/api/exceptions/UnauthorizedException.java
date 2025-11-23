package demo.api.exceptions;

import io.restassured.response.Response;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String endpoint, String method, String responseBody) {
        super(401, "Unauthorized access", endpoint, method, responseBody);
    }

    public UnauthorizedException(Response response, String endpoint, String method) {
        super(response, endpoint, method);
    }
}

