package demo.api.exceptions;

import io.restassured.response.Response;

public class ApiException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;
    private final String endpoint;
    private final String method;

    public ApiException(int statusCode, String message, String endpoint, String method, String responseBody) {
        super(String.format("API Error [%s %s]: %s (Status: %d)", method, endpoint, message, statusCode));
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.endpoint = endpoint;
        this.method = method;
    }

    public ApiException(int statusCode, String message, String endpoint, String method, String responseBody, Throwable cause) {
        super(String.format("API Error [%s %s]: %s (Status: %d)", method, endpoint, message, statusCode), cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.endpoint = endpoint;
        this.method = method;
    }

    public ApiException(Response response, String endpoint, String method) {
        this(
            response.getStatusCode(),
            extractErrorMessage(response),
            endpoint,
            method,
            response.getBody().asString()
        );
    }

    private static String extractErrorMessage(Response response) {
        try {
            String body = response.getBody().asString();
            if (body != null && !body.isEmpty()) {
                String message = response.jsonPath().getString("message");
                if (message != null && !message.isEmpty()) {
                    return message;
                }
                String error = response.jsonPath().getString("error");
                if (error != null && !error.isEmpty()) {
                    return error;
                }
                return body.length() > 200 ? body.substring(0, 200) + "..." : body;
            }
        } catch (Exception e) {
        }
        return "HTTP " + response.getStatusCode() + " " + response.getStatusLine();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getMethod() {
        return method;
    }
}

