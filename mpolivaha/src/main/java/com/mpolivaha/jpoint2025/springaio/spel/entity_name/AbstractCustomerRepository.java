package com.mpolivaha.jpoint2025.springaio.spel.entity_name;

import java.time.Instant;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface AbstractCustomerRepository<T extends AbstractAuditableEntity, ID> extends JpaRepository<T, ID> {

	//language=sql
	@Modifying
	@Query(value = "UPDATE #{#entityName} SET deleted = TRUE, deletedAt = :tombstone WHERE id IN (:ids)")
	void softDelete(@Param("ids") Collection<ID> ids, @Param("tombstone") Instant tombstone);
}
