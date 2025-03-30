package com.mpolivaha.jpoint2025.springaio.spel.entity_name;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Order extends AbstractAuditableEntity {

	@ToString.Include
	@Id
	@Column(name = "id", nullable = false)
	@JdbcTypeCode(SqlTypes.UUID)
	private UUID id;

	@Column(name = "type")
	private String type;
}
