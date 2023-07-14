package com.example.practice.integration;

import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseContainer extends PostgreSQLContainer<DatabaseContainer> {

    private static final String IMAGE_VERSION = "postgres:13.4";
    private static DatabaseContainer container;

    private DatabaseContainer() {
        super(IMAGE_VERSION);
    }

    public static DatabaseContainer getInstance() {
        if (container == null) {
            container = new DatabaseContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }

    @Override
    public void stop() {

    }
}
