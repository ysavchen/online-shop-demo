package com.mycompany.online_shop_backend;

import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractRepositoryTest {

    private static final PostgreSQLContainer<PostgresContainer> POSTGRES_CONTAINER = PostgresContainer.getInstance();

    static {
        POSTGRES_CONTAINER.start();
    }
}
