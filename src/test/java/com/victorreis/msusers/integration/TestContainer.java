package com.victorreis.msusers.integration;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

public class TestContainer implements BeforeAllCallback {

    private static final PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("users")
            .withUsername("admin")
            .withPassword("root");

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (!postgresSQLContainer.isRunning()) {
            postgresSQLContainer.setPortBindings(List.of("5432:5432"));
            postgresSQLContainer.start();
        }
    }
}
