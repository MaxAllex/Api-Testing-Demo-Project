package demo.api.config;

import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import demo.api.exceptions.ConfigurationException;

public class Config {
    private static final Map<String, Object> root;
    private static final Map<String, Object> profileCfg;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config.yaml")) {
            if (is == null) {
                throw new ConfigurationException("config.yaml not found in resources. " +
                    "Please ensure config.yaml exists in src/main/resources or src/test/resources");
            }

            Yaml yaml = new Yaml();
            root = (Map<String, Object>) yaml.load(is);
            
            if (root == null || root.isEmpty()) {
                throw new ConfigurationException("config.yaml is empty or invalid");
            }
            
            String rawActiveProfile = String.valueOf(root.get("activeProfile"));
            String activeProfile = resolveEnv(rawActiveProfile);

            if (activeProfile == null || activeProfile.isEmpty()) {
                throw new ConfigurationException("activeProfile is not set or resolved to empty value");
            }

            Map<String, Object> profiles = (Map<String, Object>) root.get("profiles");
            if (profiles == null || profiles.isEmpty()) {
                throw new ConfigurationException("No profiles defined in config.yaml");
            }
            
            profileCfg = (Map<String, Object>) profiles.get(activeProfile);

            if (profileCfg == null) {
                throw new ConfigurationException(
                    String.format("Profile '%s' not found in config.yaml. Available profiles: %s", 
                        activeProfile, profiles.keySet()));
            }

        } catch (ConfigurationException e) {
            throw e; // Перебрасываем ConfigurationException как есть
        } catch (Exception e) {
            throw new ConfigurationException("Failed to load config.yaml: " + e.getMessage(), e);
        }
    }

    public static String getBaseUrl() {
        Object baseUrl = profileCfg.get("baseUrl");
        if (baseUrl == null) {
            throw new ConfigurationException("baseUrl is not configured for active profile");
        }
        String url = resolveEnv((String) baseUrl);
        if (url == null || url.isEmpty()) {
            throw new ConfigurationException("baseUrl resolved to empty value");
        }
        return url;
    }

    public static int getTimeout() {
        Object t = profileCfg.get("timeout");
        if (t == null) {
            return 5000; // Default timeout
        }
        try {
            if (t instanceof Number) {
                int timeout = ((Number) t).intValue();
                if (timeout <= 0) {
                    throw new ConfigurationException("timeout must be positive, got: " + timeout);
                }
                return timeout;
            }
            int timeout = Integer.parseInt(String.valueOf(t));
            if (timeout <= 0) {
                throw new ConfigurationException("timeout must be positive, got: " + timeout);
            }
            return timeout;
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Invalid timeout value: " + t, e);
        }
    }

    public static boolean isLogRequests() {
        Object lr = profileCfg.get("logRequests");
        if (lr == null) {
            return false; // Default: no logging
        }
        if (lr instanceof Boolean) {
            return (Boolean) lr;
        }
        return Boolean.parseBoolean(String.valueOf(lr));
    }

    public static String getAuthToken() {
        Map<String, Object> auth = getAuthConfig();
        Object token = auth.get("token");
        if (token == null) {
            throw new ConfigurationException("auth.token is not configured for active profile");
        }
        return resolveEnv((String) token);
    }

    public static String getAuthLogin() {
        Map<String, Object> auth = getAuthConfig();
        Object login = auth.get("login");
        if (login == null) {
            throw new ConfigurationException("auth.login is not configured for active profile");
        }
        return resolveEnv((String) login);
    }

    public static String getAuthPassword() {
        Map<String, Object> auth = getAuthConfig();
        Object password = auth.get("password");
        if (password == null) {
            throw new ConfigurationException("auth.password is not configured for active profile");
        }
        return resolveEnv((String) password);
    }

    private static Map<String, Object> getAuthConfig() {
        Object authObj = profileCfg.get("auth");
        if (authObj == null) {
            throw new ConfigurationException("auth section is not configured for active profile");
        }
        try {
            return MAPPER.convertValue(authObj, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new ConfigurationException("Failed to parse auth configuration: " + e.getMessage(), e);
        }
    }

    private static String resolveEnv(String value) {
        if (value == null) return "";

        if (!value.startsWith("${") || !value.endsWith("}")) {
            return value;
        }

        String inner = value.substring(2, value.length() - 1);

        String[] parts = inner.split(":", 2);
        String envName = parts[0];
        String defaultValue = parts.length > 1 ? parts[1] : "";

        String envValue = System.getenv(envName);

        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        return defaultValue;
    }
}
