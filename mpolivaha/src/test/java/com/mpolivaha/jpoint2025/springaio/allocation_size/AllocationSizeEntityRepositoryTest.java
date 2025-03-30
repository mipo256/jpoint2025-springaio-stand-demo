package com.mpolivaha.jpoint2025.springaio.allocation_size;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mpolivaha.jpoint2025.springaio.AbstractDatabaseTest;
import com.mpolivaha.jpoint2025.springaio.allocation_size.AllocationSizeEntityRepositoryTest.AllocationSizeEntityRepositoryTestConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AllocationSizeEntityRepositoryTestConfig.class)
class AllocationSizeEntityRepositoryTest extends AbstractDatabaseTest {

	@Autowired
	private AllocationSizeEntityRepository allocationSizeEntityRepository;

	@EnableJpaRepositories(
			basePackageClasses = AllocationSizeEntityRepository.class,
			transactionManagerRef = JpaConfiguration.TRANSACTION_MANAGER_BEAN)
	static class AllocationSizeEntityRepositoryTestConfig extends JpaConfiguration {
	}

	//language=sql
	@Test
	@Transactional
	@Sql(statements = """
   			CREATE TABLE IF NOT EXISTS allocation_size(
   				id BIGINT PRIMARY KEY,
   				name TEXT
   			);
   			CREATE SEQUENCE IF NOT EXISTS allc_size_entity_sequence INCREMENT BY 10 START WITH 1 NO CYCLE;
		""")
	void testExplicitAllocationSize() {
		IntStream
				.range(0, 10)
				.mapToObj(value -> new AllocationSizeEntity().setName("name" + value))
				.forEach(it ->
						allocationSizeEntityRepository.save(it)
				);
		allocationSizeEntityRepository.flush();
	}
}
