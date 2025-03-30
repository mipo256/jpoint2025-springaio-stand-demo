package com.mpolivaha.jpoint2025.springaio.spel.entity_name;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "authors")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Author extends AbstractAuditableEntity {

	@Id
	@ToString.Include
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ToString.Include
	@Column(name = "name")
	private String name;
}
