package com.mpolivaha.jpoint2025.springaio;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import jakarta.persistence.EntityManagerFactory;

public class AbstractDatabaseTest {

	protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
			DockerImageName.parse("postgres:15.2")
	)
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");

	@Import(AbstractDatabaseConfiguration.class)
	public static class RelationalTestConfiguration extends AbstractJdbcConfiguration {

		@Bean("transactionManager")
		PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
	}

	@Import(AbstractDatabaseConfiguration.class)
	@Configuration
	protected static class JpaConfiguration {

		public static final String TRANSACTION_MANAGER_BEAN = "jpaTransactionManager";

		@Bean(TRANSACTION_MANAGER_BEAN)
		PlatformTransactionManager platformTransactionManager(EntityManagerFactory entityManagerFactory) {
			return new JpaTransactionManager(entityManagerFactory);
		}

		@Bean
		EntityManagerFactory entityManagerFactory(DataSource dataSource) {
			LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

			entityManagerFactoryBean.setDataSource(dataSource);
			entityManagerFactoryBean.setPersistenceUnitName("jpql_and_bulk_delete");
			entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
			entityManagerFactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
			entityManagerFactoryBean.afterPropertiesSet();

			EntityManagerFactory object = entityManagerFactoryBean.getObject();
			return object;
		}
	}

	@Configuration
	protected static class AbstractDatabaseConfiguration {

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
