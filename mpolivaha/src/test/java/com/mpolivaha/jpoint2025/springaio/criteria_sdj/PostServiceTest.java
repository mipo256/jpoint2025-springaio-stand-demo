package com.mpolivaha.jpoint2025.springaio.criteria_sdj;

import java.time.Duration;
import java.time.Instant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mpolivaha.jpoint2025.springaio.AbstractDatabaseTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostServiceTest.PostServiceTestConfig.class)
class PostServiceTest extends AbstractDatabaseTest {

	@Autowired
	private PostService postService;

	@Configuration
	static class PostServiceTestConfig extends RelationalTestConfiguration {

		@Bean
		public PostService postService(JdbcAggregateTemplate jdbcAggregateTemplate) {
			return new PostService(jdbcAggregateTemplate);
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
	@Transactional
	void testFindDynamic() {

		 //given.
		PostSearchForm searchForm = new PostSearchForm(
				1L,
				"Data",
				null,
				Instant.now().minusSeconds(Duration.ofDays(1).toSeconds()),
				null,
				null
		);

		// when.
		Page<Post> content = postService.findDynamic(searchForm);

		// then.
		System.out.println(content.getContent());
		Assertions.assertThat(content.getContent()).hasSize(1);
	}
}
