package com.mpolivaha.jpoint2025.springaio.criteria_sdj;

import java.time.Duration;
import java.time.Instant;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mpolivaha.jpoint2025.springaio.AbstractDatabaseTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostServiceTest.PostServiceTestConfig.class)
class PostServiceTest extends AbstractDatabaseTest {

	@Autowired
	private PostService postService;

	@Configuration
	static class PostServiceTestConfig extends AbstractJdbcConfiguration {

		@Bean
		public PostService postService(JdbcAggregateTemplate jdbcAggregateTemplate) {
			return new PostService(jdbcAggregateTemplate);
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

	// language=sql
	@Sql(statements = """
			CREATE TABLE post(
				id BIGSERIAL PRIMARY KEY,
				content TEXT,
				title TEXT,
				created_at TIMESTAMPTZ,
				likes SMALLINT, -- because our posts cannot get many likes 😁
				author_id BIGINT
			);
			
			INSERT INTO post(content, title, created_at, likes, author_id)
			VALUES ('A long time ago, in a galaxy...', 'Jakarta Data', NOW(), 7, 1);
			
			INSERT INTO post(content, title, created_at, likes, author_id)
			VALUES ('Wake up, Neo', 'Spring Security', NOW() - INTERVAL '400 days', 21, 2);
			""")
	@Test
	void testFindDynamic() {

		 //given.
		PostSearchForm searchForm = PostSearchForm
				.builder()
				.authorId(1L)
				.createdAtGte(Instant.now().minusSeconds(Duration.ofDays(1).toSeconds()))
				.titleLike("Data")
				.build();

		// when.
		Page<Post> content = postService.findDynamic(searchForm);

		// then.
		System.out.println(content);
		Assertions.assertThat(content.getContent()).hasSize(1);
	}
}
