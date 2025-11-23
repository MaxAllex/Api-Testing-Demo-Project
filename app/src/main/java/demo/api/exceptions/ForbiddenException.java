package demo.api.exceptions;

import io.restassured.response.Response;

public class ForbiddenException extends ApiException {
    public ForbiddenException(String endpoint, String method, String responseBody) {
        super(403, "Forbidden access", endpoint, method, responseBody);
    }

    public ForbiddenException(Response response, String endpoint, String method) {
        super(response, endpoint, method);
    }
}

