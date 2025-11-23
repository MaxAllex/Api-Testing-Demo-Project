package demo.api.exceptions;

import io.restassured.response.Response;

public class NotFoundException extends ApiException {
    public NotFoundException(String endpoint, String method, String responseBody) {
        super(404, "Resource not found", endpoint, method, responseBody);
    }

    public NotFoundException(Response response, String endpoint, String method) {
        super(response, endpoint, method);
    }
}

