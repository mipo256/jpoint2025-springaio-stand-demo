package com.mpolivaha.jpoint2025.springaio.spel;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.mpolivaha.jpoint2025.springaio.spel.UserProperties.Customer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "users")
@ToString // it's okay
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

	@Id
	@EqualsAndHashCode.Include
	private Long id;

	private String name;

	private Customer customer;
}
