package demo.api.config;

import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class Config {
    private static final Map<String, Object> root;
    private static final Map<String, Object> profileCfg;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config.yaml")) {
            if (is == null) {
                throw new RuntimeException("config.yaml not found in resources");
            }

            Yaml yaml = new Yaml();
            root = (Map<String, Object>) yaml.load(is);
            String rawActiveProfile = String.valueOf(root.get("activeProfile"));
            String activeProfile = resolveEnv(rawActiveProfile);  // например "dev"

            Map<String, Object> profiles = (Map<String, Object>) root.get("profiles");
            profileCfg = (Map<String, Object>) profiles.get(activeProfile);

            if (profileCfg == null) {
                throw new RuntimeException("Profile not found in config.yaml: " + activeProfile);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.yaml", e);
        }
    }

    public static String getBaseUrl() {
        return resolveEnv((String) profileCfg.get("baseUrl"));
    }

    public static int getTimeout() {
        Object t = profileCfg.get("timeout");
        if (t instanceof Number) {
            return ((Number) t).intValue();
        }
        return Integer.parseInt(String.valueOf(t));
    }

    public static boolean isLogRequests() {
        Object lr = profileCfg.get("logRequests");
        if (lr instanceof Boolean) {
            return (Boolean) lr;
        }
        return Boolean.parseBoolean(String.valueOf(lr));
    }

    public static String getAuthToken() {
        Map<String, Object> auth = MAPPER.convertValue(profileCfg.get("auth"), new TypeReference<Map<String, Object>>() {});
        return resolveEnv((String) auth.get("token"));
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
