package com.mpolivaha.jpoint2025.springaio.spel.entity_name;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString
public class AbstractAuditableEntity {

	@Column(name = "deleted")
	private boolean deleted;

	@Column(name = "deleted_at")
	private Instant deletedAt;
}
