package com.mpolivaha.jpoint2025.springaio.jpql_challanges;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.hibernate.annotations.CascadeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mpolivaha.jpoint2025.springaio.AbstractDatabaseTest;

import jakarta.persistence.PostRemove;
import jakarta.persistence.PreRemove;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ParentClassRepositoryTest.ParentClassTestConfig.class)
class ParentClassRepositoryTest extends AbstractDatabaseTest {

	@Autowired
	private ParentClassRepository parentClassRepository;

	@Autowired
	private ChildClassRepository childClassRepository;

	@EnableJpaRepositories(basePackageClasses = ParentClassRepository.class,
			transactionManagerRef = JpaConfiguration.TRANSACTION_MANAGER_BEAN)
	static class ParentClassTestConfig extends JpaConfiguration {
	}

	/**
	 * First things first - JPQL is not that easy in terms of Persistence Context.
	 * <p>
	 * For instance, JPQL queries in general <strong>DO NOT</strong> hit first level cache, see below.
	 * But the loaded entities after SELECT (if they are, indeed, entities) are loaded into the
	 * corresponding persistence context, but taht happens after the query.
	 * <p>
	 * The takeaway - JPQL queries, before execution, bypass the first level cache, as do the Criteria queries by the way.
	 */
	@Test
	//language=sql
	@Sql(statements = """
			CREATE TABLE parent_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT
			);
			CREATE TABLE child_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT,
				parent_class_id BIGINT REFERENCES parent_class(id)
			);
						
			INSERT INTO parent_class(id, name) VALUES(1, 'some name');
			""")
	@Transactional
	void testSelectJPQL() {
		Optional<ParentClass> found = parentClassRepository.findByIdJPQL(1L);
		Optional<ParentClass> foundAgainByCacheHit = parentClassRepository.findById(1L);

		assertThat(found).isPresent();
		assertThat(foundAgainByCacheHit).isPresent().containsSame(found.get());
	}

	/**
	 * So, for delete operation, it is quite interesting. In particular
	 * <p>
	 * As we know from previous example, JPQL/HQL queries just bypass the 1-st level cache
	 * That means, that <strong>no entity state transition happening!</strong>. That means, that
	 * the callbacks like {@link PreRemove} or {@link PostRemove} <strong> just would not be invoked, even
	 * if the entity is managed!
	 * </strong>
	 */
	@Test
	//language=sql
	@Sql(statements = """
			CREATE TABLE parent_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT
			);
			CREATE TABLE child_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT,
				parent_class_id BIGINT REFERENCES parent_class(id)
			);
						
			INSERT INTO parent_class(id, name) VALUES(1, 'some name');
			""")
	@Transactional
	void testCallbacksAreNotInvoked() {
		// making entity managed
		Optional<ParentClass> managed = parentClassRepository.findById(1L);

		assertThat(managed).isPresent();

		// deleting managed entity via id
		parentClassRepository.deleteAllByIdInBatch(List.of(managed.get().getId()));

		assertThat(managed.get().getInvocationCounter()).isEqualTo(0L);
	}

	/**
	 * But that's not all!
	 * <p>
	 * In general, cascade operations, such as {@link CascadeType#REMOVE Hibernate @Cascade(CascadeType.REMOVE)} or
	 * JPA's {@link jakarta.persistence.CascadeType#REMOVE @OneToMany(cascade=CascadeType.REMOVE)} work only on managed
	 * entities inside context.
	 * <p>
	 * But because HQL/JPQL bypass the context - the REMOVE cascade does not work. The same thing for UPDATE or INSERT
	 * HQL/JPQL statements.
	 * </strong>
	 */
	@Test
	//language=sql
	@Sql(statements = """
			CREATE TABLE parent_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT
			);
			CREATE TABLE child_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT,
				parent_class_id BIGINT -- ATTENTION!!!!! NOT FOREIGN KEY INTENTIONALLY!!
			);
						
			INSERT INTO parent_class(id, name) VALUES(1, 'some name');
			INSERT INTO child_class(id, name, parent_class_id) VALUES(1, 'child', 1);
			""")
	@Transactional
	void testCascadesDoNotWork() {
		// no cascading!
		parentClassRepository.deleteByIdJPQL(1L);
		parentClassRepository.flush();
		// Everything is flushed!

		Assertions.assertThat(childClassRepository.findById(1L)).isPresent();
	}

	/**
	 * <strong>So, simple Spring Data's JPA deleteById works via {@link jakarta.persistence.EntityManager#remove(Object)}</strong>
	 * <p>
	 * Therefore, callbacks, cascades ant etc works. HOWEVER! The fact that callbacks work mean, that Spring Data JPA has to load
	 * entity first before deleting it!
	 * <p>
	 * Therefore, this is trade off - performance with HQL VS callbacks, cascades, but worse perf. with {@link jakarta.persistence.EntityManager#remove(Object)}
	 */
	@Test
	//language=sql
	@Sql(statements = """
			CREATE TABLE parent_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT
			);
			CREATE TABLE child_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT,
				parent_class_id BIGINT -- ATTENTION!!!!! NOT FOREIGN KEY INTENTIONALLY!!
			);
						
			INSERT INTO parent_class(id, name) VALUES(1, 'some name');
			INSERT INTO child_class(id, name, parent_class_id) VALUES(1, 'child', 1);
			""")
	@Transactional
	void deleteByIds() {
		// Notice the SELECTs first!
		parentClassRepository.deleteById(1L);
	}

	/**
	 * And not DELETE in batch operation
	 */
	@Test
	//language=sql
	@Sql(statements = """
			CREATE TABLE parent_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT
			);
			CREATE TABLE child_class(
				id BIGSERIAL PRIMARY KEY,
				name TEXT,
				parent_class_id BIGINT -- ATTENTION!!!!! NOT FOREIGN KEY INTENTIONALLY!!
			);
						
			INSERT INTO parent_class(id, name) VALUES(1, 'some name');
			INSERT INTO child_class(id, name, parent_class_id) VALUES(1, 'child', 1);
			""")
	@Transactional
	void deleteByIdsInBatch() {
		// No SELECT! Just plain HQL!
		parentClassRepository.deleteAllByIdInBatch(Set.of(1L));
	}
}
