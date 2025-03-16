package com.mpolivaha.jpoint2025.springaio.criteria_sdj;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Author {

	@Id
	private Long id;

	private String firstName;

	private String lastName;
}
