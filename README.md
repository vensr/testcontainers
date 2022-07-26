# Using TestContainers for Integration Testing

## What are testcontainers?

As per the [website](https://www.testcontainers.org/), Testcontainers is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.

Testcontainers make the following kinds of tests easier:

* Data access layer integration tests - write your tests that insert, delete, update and read data from the database without worrying anything about where and how the database is provided.
* Application integration tests - write your integration tests with any applications for which containers are available (ex: redis, keycloak, localstack anything on a container)
* UI/Acceptance tests - write and run your functional tests dynamically (example: selenium web containers for running your selenium tests)

## Prerequisites

* Docker
* JVM testing framework (JUnit 4,5 / Spock etc)

## Steps to write the test case in a Spring Boot Application

### 1. Add testcontainer dependencies in spring boot application

Add the following dependencies in your spring boot application. This sample application assumes that junit-jupiter / Junit 5 is used and the database that is used is MySQL. Refer the [pom.xml](./pom.xml) of the current project for more details.

```xml
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>mysql</artifactId>
        <scope>test</scope>
    </dependency>
```

### 2. Create Container and Dynamically adjust Property Sources of spring boot

Create MySql container and dynamically set the property source to adjust the url, username and password details. This will create a new docker container and bring my mysql service.

Check out the class below, you can create this class and extend it in all your integration test cases. Check out the [BaseTestContainer.java](./src/test/java/com/punch/testcontainers/BaseTestContainer.java) class in the current project.

```java
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
```

### 3. Write your tests

Now you can write your tests to insert, delete, get or update records from the underlying database without needing to worry about mysql dependency.

Check the below sample MessageTests class, that tests the create and get flow of a sample message.

```java

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.punch.testcontainers.model.Message;
import com.punch.testcontainers.repository.MessageRepository;

@SpringBootTest
class MessageTests extends BaseTestContainer {

	@Autowired
	private MessageRepository messageRepository;

	@Test
	void createMessages() {
		Message message = new Message();
		message.setMessage("This is a sample message");
		messageRepository.save(message);

		assertTrue(message.getId() > 0);
	}

	@Test
	void getMessages() {
		assertTrue(messageRepository.findAll().size() == 1);
	}

}

```

### 4. Run your tests

Now you can run your tests using the following command below.

```bash
mvn clean test
```

This is all is to it. Now you can write your integration tests and enjoy all the advantages of the testcontainers.

Refer [TestContainers](https://www.testcontainers.org/) website for more details.

