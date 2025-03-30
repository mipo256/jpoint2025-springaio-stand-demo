package com.mpolivaha.jpoint2025.springaio.allocation_size;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "allocation_size")
@ToString(onlyExplicitlyIncluded = true)
public class AllocationSizeEntity {

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allc_size_entity_gen")
	@SequenceGenerator(name = "allc_size_entity_gen", allocationSize = 10, sequenceName = "allc_size_entity_sequence")
	private Long id;

	private String name;
}
