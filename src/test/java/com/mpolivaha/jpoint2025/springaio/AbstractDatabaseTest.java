package com.mpolivaha.jpoint2025.springaio;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class AbstractDatabaseTest {

	protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
			DockerImageName.parse("postgres:15.2")
	)
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");

	protected static class AbstractRelationalTestConfiguration extends AbstractJdbcConfiguration {

		@Bean("transactionManager")
		PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}

		@Bean
		public DataSource dataSource() {
			return new DriverManagerDataSource(
					postgreSQLContainer.getJdbcUrl(), // PostgreSQLContainer to be initialized is guaranteed by TestExecutionListeners invocation order
					postgreSQLContainer.getUsername(),
					postgreSQLContainer.getPassword()
			);
		}

		@Bean
		public NamedParameterJdbcOperations namedParameterJdbcOperations() {
			return new NamedParameterJdbcTemplate(dataSource());
		}
	}

	@BeforeAll
	static void beforeAll() {
		postgreSQLContainer.start();
	}

	@AfterAll
	static void afterAll() {
		postgreSQLContainer.close();
	}
}
