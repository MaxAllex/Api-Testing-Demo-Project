package demo.api.validators;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

public class ResponseValidator {

    public static void validateSuccess(Response response) {
        int statusCode = response.getStatusCode();
        Assertions.assertTrue(
            statusCode >= 200 && statusCode < 300,
            String.format("Expected success status code (2xx), but got %d. Response: %s", 
                statusCode, response.getBody().asString())
        );
    }

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assertions.assertEquals(
            expectedStatusCode,
            actualStatusCode,
            String.format("Expected status code %d, but got %d. Response: %s",
                expectedStatusCode, actualStatusCode, response.getBody().asString())
        );
    }

    public static void validateJson(Response response) {
        try {
            response.jsonPath();
        } catch (Exception e) {
            throw new AssertionError(
                String.format("Response is not valid JSON. Response body: %s", 
                    response.getBody().asString()),
                e
            );
        }
    }

    public static void validateNotEmpty(Response response) {
        String body = response.getBody().asString();
        Assertions.assertFalse(
            body == null || body.trim().isEmpty(),
            "Response body should not be empty"
        );
    }

    public static void validateRequiredFields(Response response, List<String> requiredFields) {
        validateJson(response);
        
        for (String field : requiredFields) {
            Object value = response.jsonPath().get(field);
            Assertions.assertNotNull(
                value,
                String.format("Required field '%s' is missing in response. Response: %s",
                    field, response.getBody().asString())
            );
            
            if (value instanceof String && ((String) value).isEmpty()) {
                throw new AssertionError(
                    String.format("Required field '%s' is empty in response. Response: %s",
                        field, response.getBody().asString())
                );
            }
        }
    }

    public static void validateFieldNotNull(Response response, String fieldPath, String fieldName) {
        validateJson(response);
        Object value = response.jsonPath().get(fieldPath);
        Assertions.assertNotNull(
            value,
            String.format("Field '%s' (path: %s) should not be null. Response: %s",
                fieldName, fieldPath, response.getBody().asString())
        );
    }

    public static void validateFieldNotEmpty(Response response, String fieldPath, String fieldName) {
        validateFieldNotNull(response, fieldPath, fieldName);
        String value = response.jsonPath().getString(fieldPath);
        Assertions.assertFalse(
            value == null || value.trim().isEmpty(),
            String.format("Field '%s' (path: %s) should not be empty. Response: %s",
                fieldName, fieldPath, response.getBody().asString())
        );
    }

    public static void validateArrayNotEmpty(Response response) {
        validateJson(response);
        List<?> array = response.jsonPath().getList("$");
        Assertions.assertNotNull(array, "Response should be a JSON array");
        Assertions.assertFalse(
            array.isEmpty(),
            "Response array should not be empty"
        );
    }

    public static void validateContentType(Response response, String expectedContentType) {
        String actualContentType = response.getContentType();
        Assertions.assertEquals(
            expectedContentType,
            actualContentType,
            String.format("Expected Content-Type '%s', but got '%s'",
                expectedContentType, actualContentType)
        );
    }

    public static void validateHeaderPresent(Response response, String headerName) {
        String headerValue = response.getHeader(headerName);
        Assertions.assertNotNull(
            headerValue,
            String.format("Header '%s' should be present in response", headerName)
        );
    }

    public static void validateResponseTime(Response response, long maxResponseTimeMs) {
        long responseTime = response.getTime();
        Assertions.assertTrue(
            responseTime <= maxResponseTimeMs,
            String.format("Response time %d ms exceeds maximum %d ms",
                responseTime, maxResponseTimeMs)
        );
    }
}

