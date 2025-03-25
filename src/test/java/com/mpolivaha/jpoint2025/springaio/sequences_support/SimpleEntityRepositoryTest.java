package com.mpolivaha.jpoint2025.springaio.sequences_support;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mpolivaha.jpoint2025.springaio.AbstractDatabaseTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SimpleEntityRepositoryTest.SimpleEntityRepositoryTestConfig.class)
public class SimpleEntityRepositoryTest extends AbstractDatabaseTest {

	@Autowired
	protected SimpleEntityRepository simpleEntityRepository;

	@Configuration
	@EnableJdbcRepositories(basePackageClasses = SimpleEntityRepository.class)
	static class SimpleEntityRepositoryTestConfig extends RelationalTestConfiguration { }

	// language=sql
	@Sql(statements = """
			CREATE TABLE simple_entity(
				id BIGINT PRIMARY KEY,
				name TEXT,
				type TEXT
			);
			
			CREATE SEQUENCE simple_entity_seq START WITH 15 INCREMENT BY 1 NO CYCLE;
			""")
	@Test
	@Transactional
	void testInsertionOfSimpleEntity() {
		SimpleEntity saved = simpleEntityRepository.save(new SimpleEntity().setName("MyName").setType("COMMON"));

		Assertions.assertThat(saved.getId()).isNotNull().isEqualTo(15L);
	}
}
