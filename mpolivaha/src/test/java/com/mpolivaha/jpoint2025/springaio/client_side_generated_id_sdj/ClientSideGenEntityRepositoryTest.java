package com.mpolivaha.jpoint2025.springaio.client_side_generated_id_sdj;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mpolivaha.jpoint2025.springaio.AbstractDatabaseTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ClientSideGenEntityRepositoryTest.ClientSideGenEntityRepositoryTestConfig.class)
class ClientSideGenEntityRepositoryTest extends AbstractDatabaseTest {

	@Autowired
	private ClientSideGenEntityRepository repository;

	@EnableJdbcRepositories(basePackageClasses = ClientSideGenEntityRepository.class)
	static class ClientSideGenEntityRepositoryTestConfig extends RelationalTestConfiguration { }

	//language=sql
	@Test
	@Sql(statements = """
			CREATE TABLE IF NOT EXISTS client_side_gen_entity(
				id UUID PRIMARY KEY,
				name TEXT
			);
			""")
	void testClientSideGeneratedId() {
		ClientSideGenEntity saved = repository.save(ClientSideGenEntity.forInsert("MyNewEntity"));

		ClientSideGenEntity updated = repository.save(ClientSideGenEntity.forUpdate(saved.getId(), "newName"));

		assertThat(updated.getId()).isEqualTo(saved.getId());
		assertThat(updated.getName()).isEqualTo(updated.getName());
	}
}
