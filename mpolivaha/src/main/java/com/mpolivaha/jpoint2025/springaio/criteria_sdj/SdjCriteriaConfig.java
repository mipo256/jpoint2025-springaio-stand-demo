package com.mpolivaha.jpoint2025.springaio.criteria_sdj;

import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class SdjCriteriaConfig {

	@Bean
	public DataSource dataSource(ConfigurableEnvironment environment) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(environment.getProperty("spring.datasource.url"));
		dataSource.setUsername(environment.getProperty("spring.datasource.username"));
		dataSource.setPassword(environment.getProperty("spring.datasource.password"));
		return dataSource;
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource, DatabasePopulator databasePopulator) {
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		return dataSourceInitializer;
	}

	@Bean
	public DatabasePopulator databasePopulator() {
		return new ResourceDatabasePopulator(
				//language=sql
				new ByteArrayResource("""
       			CREATE TABLE IF NOT EXISTS post(
							id BIGSERIAL PRIMARY KEY,
							content TEXT,
							title TEXT,
							created_at TIMESTAMPTZ,
							likes SMALLINT, -- because our posts cannot get many likes 😁
							author_id BIGINT
						);
						
						DELETE FROM post;
						
						INSERT INTO post(content, title, created_at, likes, author_id)
						VALUES ('A long time ago, in a galaxy...', 'Jakarta Data', NOW(), 7, 1);
						
						INSERT INTO post(content, title, created_at, likes, author_id)
						VALUES ('Wake up, Neo', 'Spring Security', NOW() - INTERVAL '400 days', 21, 2);

							""".getBytes(StandardCharsets.UTF_8))
		);
	}

	@Bean
	public NamedParameterJdbcOperations operations(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
}
