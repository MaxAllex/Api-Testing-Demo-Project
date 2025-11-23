package demo.api.exceptions;

import io.restassured.response.Response;

public class ValidationException extends ApiException {
    public ValidationException(String endpoint, String method, String responseBody, String validationMessage) {
        super(400, "Validation failed: " + validationMessage, endpoint, method, responseBody);
    }

    public ValidationException(Response response, String endpoint, String method) {
        super(response, endpoint, method);
    }
}

