package config;

import java.util.Map;

import static java.util.Map.entry;

public class Environment implements IEnvironment {

    private final Map<String, String> envs = Map.ofEntries(
        entry("PROD", "https://reqres.in")
    );
    private String environmentName;

    public Environment(String environmentName) {
        this.environmentName = environmentName;
    }

    public String getUrl() {
        return envs.get(environmentName);
    }
}
