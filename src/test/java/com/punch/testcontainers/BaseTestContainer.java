package com.punch.testcontainers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BaseTestContainer {
    
	@Container
	protected static MySQLContainer container = new MySQLContainer<>("mysql")
			.withUsername("user")
			.withPassword("password")
			.withDatabaseName("mydatabase")
            .withExposedPorts(3306)
            .waitingFor(Wait.forHealthcheck());

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
                    
}
