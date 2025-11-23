package demo.api.exceptions;

import io.restassured.response.Response;

public class ServerException extends ApiException {
    public ServerException(int statusCode, String endpoint, String method, String responseBody) {
        super(statusCode, "Server error", endpoint, method, responseBody);
    }

    public ServerException(Response response, String endpoint, String method) {
        super(response, endpoint, method);
    }
}

