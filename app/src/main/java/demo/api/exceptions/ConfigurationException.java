package demo.api.exceptions;

public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String message) {
        super("Configuration error: " + message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super("Configuration error: " + message, cause);
    }
}

