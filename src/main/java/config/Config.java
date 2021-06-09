package config;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static final long maxResponseTime = 1000L;

    public static String getBaseUrl() {
        Map<String, String> envs = new HashMap<>();
        for (Environment env : Environment.values()) {
            envs.put(env.getEnvName(), env.getUrl());
        }

        return envs.get((System.getenv("ENV") != null) ? System.getenv("ENV") : "PROD");
    }

    public static Map<String, String> getUserCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", System.getenv("EMAIL"));
        credentials.put("password", System.getenv("PASSWORD"));

        return credentials;
    }
}
