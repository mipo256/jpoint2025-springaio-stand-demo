package com.mpolivaha.jpoint2025.springaio.spel.entity_name;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mpolivaha.jpoint2025.springaio.AbstractDatabaseTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EntityNameSpelTest.EntityNameSpelTestConfig.class)
public class EntityNameSpelTest extends AbstractDatabaseTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Configuration
	@EnableJpaRepositories(
			basePackageClasses = OrderRepository.class,
			transactionManagerRef = JpaConfiguration.TRANSACTION_MANAGER_BEAN)
	static class EntityNameSpelTestConfig extends JpaConfiguration { }

	//language=sql
	@Test
	@Sql(statements = """
   		CREATE TABLE IF NOT EXISTS authors(
   			id BIGSERIAL PRIMARY KEY,
   			name TEXT,
   			deleted BOOLEAN,
   			deleted_at TIMESTAMP
   		);
   		
   		CREATE TABLE IF NOT EXISTS orders(
   			id UUID PRIMARY KEY,
   			type TEXT,
   			deleted BOOLEAN,
   			deleted_at TIMESTAMP
   		);
   		
   		INSERT INTO orders VALUES(CAST('7fe2815f-0145-4e15-9013-07e81dbf8796' AS UUID), 'common', false, NULL);
   		INSERT INTO authors VALUES(1, 'my_author', false, NULL);
			""")
	@Transactional
	void testSoftDeleteAuthorAndOrder() {
		orderRepository.softDelete(Collections.singleton(UUID.fromString("7fe2815f-0145-4e15-9013-07e81dbf8796")), Instant.now());
		authorRepository.softDelete(Collections.singleton(1L), Instant.now().minusSeconds(10));
	}
}
