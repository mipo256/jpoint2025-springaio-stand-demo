package com.mpolivaha.jpoint2025.springaio.sequences_support;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Sequence;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Table
@Getter
@Setter
public class SimpleEntity {

	@Id
	@Sequence(sequence = "simple_entity_seq")
	private Long id;

	private String name;

	private String type;
}
