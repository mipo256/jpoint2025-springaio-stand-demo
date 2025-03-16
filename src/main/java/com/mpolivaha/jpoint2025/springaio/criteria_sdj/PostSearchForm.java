package com.mpolivaha.jpoint2025.springaio.criteria_sdj;

import java.time.Instant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostSearchForm {

	/**
	 * The id of the author of the post
	 */
	private Long authorId;

	/**
	 * Title pattern
	 */
	private String titleLike;

	/**
	 * Created At Less Than Or Equals
	 */
	private Instant createdAtLte;

	/**
	 * Created At Greater Than Or Equals
	 */
	private Instant createdAtGte;

	/**
	 * Likes Count Greater Than Or Equals
	 */
	private Integer likesGte;

	private Pagination pagination;

	public Pageable toPageable() {
		if (pagination == null) {
			return Pageable.unpaged();
		}

		return PageRequest.of(
				pagination.pageNumber,
				pagination.pageSize,
				Direction.fromString(pagination.sort.direction),
				pagination.sort.property
		);
	}

	@Data
	public static class Pagination {

		/**
		 * <i>Typically</i>, page number is zero based
		 */
		private int pageNumber;

		private int pageSize;

		private Sort sort;
	}

	@Data
	public static class Sort {

		private String property;
		private String direction;
	}
}
