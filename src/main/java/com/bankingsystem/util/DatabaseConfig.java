package com.bankingsystem.util;

/**
 * Central place for JDBC connection properties. Allows overriding via environment variables
 * while keeping sensible defaults for local development.
 */
public final class DatabaseConfig {
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/BankingSystemDb";
    private static final String DEFAULT_USERNAME = "postgres";
    private static final String DEFAULT_PASSWORD = "7841";

    private DatabaseConfig() {
    }

    public static String getUrl() {
        return getEnvOrDefault("BANKING_DB_URL", DEFAULT_URL);
    }

    public static String getUsername() {
        return getEnvOrDefault("BANKING_DB_USERNAME", DEFAULT_USERNAME);
    }

    public static String getPassword() {
        return getEnvOrDefault("BANKING_DB_PASSWORD", DEFAULT_PASSWORD);
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
