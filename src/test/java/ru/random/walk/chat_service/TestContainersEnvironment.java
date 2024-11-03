package ru.random.walk.chat_service;

import org.flywaydb.core.Flyway;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("resource")
@Testcontainers
public abstract class TestContainersEnvironment {
    @Container
    public static final JdbcDatabaseContainer<?> DATABASE_CONTAINER;
    static {
        DATABASE_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("random_walk_postgres")
                .withUsername("postgres")
                .withPassword("postgres");
        DATABASE_CONTAINER.start();
        Startables.deepStart(DATABASE_CONTAINER)
                .thenAccept(unused -> runMigrations());
    }

    private static void runMigrations() {
        try (var ignored = DriverManager
                .getConnection(TestContainersEnvironment.DATABASE_CONTAINER.getJdbcUrl(),
                        TestContainersEnvironment.DATABASE_CONTAINER.getUsername(),
                        TestContainersEnvironment.DATABASE_CONTAINER.getPassword())) {
            Flyway flyway = Flyway.configure()
                    .dataSource(TestContainersEnvironment.DATABASE_CONTAINER.getJdbcUrl(),
                            TestContainersEnvironment.DATABASE_CONTAINER.getUsername(),
                            TestContainersEnvironment.DATABASE_CONTAINER.getPassword())
                    .locations("filesystem:src/main/resources/db/migration")
                    .defaultSchema("chat")
                    .load();
            flyway.migrate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DATABASE_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", DATABASE_CONTAINER::getUsername);
        registry.add("spring.datasource.password", DATABASE_CONTAINER::getPassword);
    }
}