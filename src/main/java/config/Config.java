package config;

import java.util.HashMap;
import java.util.Map;

public final class Config {

    public static final long MAX_RESPONSE_TIME = 1000L;
    private static final String DEFAULT_ENV = "PROD";

    public static String getBaseUrl() {
        Environment env = new Environment(
            System.getenv("ENV") != null ? System.getenv("ENV") : DEFAULT_ENV
        );
        return env.getUrl();
    }

    public static Map<String, String> getUserCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", System.getenv("EMAIL"));
        credentials.put("password", System.getenv("PASSWORD"));

        return credentials;
    }
}
