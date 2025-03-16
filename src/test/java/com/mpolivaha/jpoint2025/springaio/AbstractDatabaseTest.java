package com.mpolivaha.jpoint2025.springaio;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class AbstractDatabaseTest {

	protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
			DockerImageName.parse("postgres:15.2")
	)
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");

	@BeforeAll
	static void beforeAll() {
		postgreSQLContainer.start();
	}

	@AfterAll
	static void afterAll() {
		postgreSQLContainer.close();
	}
}
