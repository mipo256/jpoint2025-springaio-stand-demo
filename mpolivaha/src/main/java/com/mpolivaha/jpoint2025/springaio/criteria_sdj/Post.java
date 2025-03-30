package com.mpolivaha.jpoint2025.springaio.criteria_sdj;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table
@Getter
@Setter
@ToString // In Spring Data JDBC it is fine
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

	@Id
	@EqualsAndHashCode.Include
	private Long id;

	@Column(value = "content")
	private String content;

	@Column(value = "title")
	private String title;

	@Column(value = "created_at")
	private Instant createdAt;

	@Column(value = "likes")
	private int likes;

	@Column(value = "author_id")
	private AggregateReference<Author, Long> authorId;
}
