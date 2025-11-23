package demo.api.clients;

import io.restassured.response.Response;

import java.util.function.Supplier;

public class RetryHandler {
    
    public static Response executeWithRetry(Supplier<Response> request, int maxRetries, long delayMs) {
        int attempt = 0;
        Exception lastException = null;
        
        while (attempt < maxRetries) {
            try {
                Response response = request.get();
                int statusCode = response.getStatusCode();
                
                if (statusCode < 500 || attempt >= maxRetries - 1) {
                    return response;
                }
                
                attempt++;
                if (attempt < maxRetries) {
                    Thread.sleep(delayMs * attempt);
                }
            } catch (Exception e) {
                lastException = e;
                attempt++;
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(delayMs * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(ie);
                    }
                }
            }
        }
        
        if (lastException != null) {
            throw new RuntimeException("Request failed after " + maxRetries + " attempts", lastException);
        }
        
        throw new RuntimeException("Request failed after " + maxRetries + " attempts");
    }
    
    public static Response executeWithRetry(Supplier<Response> request) {
        return executeWithRetry(request, 3, 1000);
    }
}

