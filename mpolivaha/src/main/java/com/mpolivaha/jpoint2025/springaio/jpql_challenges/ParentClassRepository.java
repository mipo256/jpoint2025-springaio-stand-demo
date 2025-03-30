package com.mpolivaha.jpoint2025.springaio.jpql_challenges;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ParentClassRepository extends JpaRepository<ParentClass, Long> {

	// language=sql
	@Query("SELECT t FROM ParentClass t WHERE t.id = ?1")
	Optional<ParentClass> findByIdJPQL(Long id);

	@Modifying
	// language=sql
	@Query("DELETE FROM ParentClass p WHERE p.id = ?1")
	void deleteByIdJPQL(Long id);
}
