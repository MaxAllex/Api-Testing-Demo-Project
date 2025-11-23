package demo.api.clients;

import demo.api.exceptions.*;
import io.restassured.response.Response;

public class ResponseHandler {

    public static Response handleResponse(Response response, String endpoint, String method) {
        int statusCode = response.getStatusCode();
        
        if (statusCode >= 200 && statusCode < 300) {
            return response;
        }
        if (statusCode == 400 || statusCode == 422) {
            throw new ValidationException(response, endpoint, method);
        } else if (statusCode == 401) {
            throw new UnauthorizedException(response, endpoint, method);
        } else if (statusCode == 403) {
            throw new ForbiddenException(response, endpoint, method);
        } else if (statusCode == 404) {
            throw new NotFoundException(response, endpoint, method);
        } else if (statusCode == 409) {
            throw new ApiException(statusCode, "Conflict", endpoint, method, response.getBody().asString());
        } else if (statusCode >= 500) {
            ServerException exception = new ServerException(response, endpoint, method);
            throw exception;
        } else {
            throw new ApiException(response, endpoint, method);
        }
    }

    public static Response handleResponse(Response response, String endpoint, String method, int... expectedStatusCodes) {
        int statusCode = response.getStatusCode();
        
        for (int expected : expectedStatusCodes) {
            if (statusCode == expected) {
                return response;
            }
        }

        return handleResponse(response, endpoint, method);
    }

    public static Response handleResponseSilently(Response response) {
        return response;
    }
}

