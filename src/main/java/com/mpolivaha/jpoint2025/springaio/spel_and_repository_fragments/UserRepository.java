package com.mpolivaha.jpoint2025.springaio.spel_and_repository_fragments;

import java.util.Optional;

import org.aopalliance.intercept.MethodInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryLookupStrategy;

public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Spring Data will handle the invocation via internal {@link MethodInterceptor Method Interceptor}
	 *
	 * @implNote If curios, see {@link org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor}
	 * 		class
	 */
	@Override
	@NotNull Optional<User> findById(@NotNull Long id);

	/**
	 * <i>Typically (not always!)</i>, depending on:
	 *   <ol>
	 *     <li>The way you configured the {@link QueryLookupStrategy}</li>
	 *     <li>And on a particular Spring Data module</li>
	 *   </ol>
	 * the SQL, CQL and any other query language inside the {@link Query} annotation <strong>will not be validated</strong>.
	 * Spring Data JDBC, for instance cannot validate the SQL inside the @{@link Query} so we can do shit like that.
	 */
	@Query(value = "We do not care about whats inside in this case")
	default void findByIdMyCustomMethod(Long id) {
		System.out.printf("id = %s%n", id);
		throw new UnsupportedOperationException("Ha-HA!");
	}

	// Solution one - provide from outside
	// language=sql
	@Query(value = "SELECT * FROM users WHERE name = :name AND customer = :customer")
	Optional<User> findByName(@Param("name") String name, @Param("customer") String customer);

	// Solution with SPEL
	// language=sql
	@Query(value = "SELECT * FROM users WHERE name = :name AND customer = :#{userProperties.customer()}")
	Optional<User> findByNameSpelExtension(@Param("name") String name);
}
