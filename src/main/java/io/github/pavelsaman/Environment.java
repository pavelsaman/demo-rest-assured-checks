package io.github.pavelsaman;

public enum Environment {
    DEV("https://reqres.dev.in"),
    STAGING("https://reqres.staging.in"),
    PROD("https://reqres.in");

    private final String env;

    Environment(String env) {
        this.env = env;
    }

    public String getEnvName() {
        return name();
    }

    public String getUrl() {
        return this.env;
    }

    @Override
    public String toString() {
        return getEnvName() + " - " + getUrl();
    }
}
