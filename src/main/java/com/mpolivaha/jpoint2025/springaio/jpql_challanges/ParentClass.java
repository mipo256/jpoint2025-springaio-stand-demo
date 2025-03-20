package com.mpolivaha.jpoint2025.springaio.jpql_challanges;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@ToString
@Table(name = "parent_class")
@Accessors(chain = true)
public class ParentClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "parentClass")
	private List<ChildClass> childClasses;

	@Transient
	private int invocationCounter;

	@PreRemove
	void preRemove() {
		System.out.println("PRE REMOVE CALLBACK!");
		invocationCounter++;
	}
}
