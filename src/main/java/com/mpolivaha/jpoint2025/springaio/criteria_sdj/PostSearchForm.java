package com.mpolivaha.jpoint2025.springaio.criteria_sdj;

import java.time.Instant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import lombok.Data;

//TODO: records are not documented this way, adhere to javadoc for records
public record PostSearchForm(

	/**
	 * The id of the author of the post
	 */
	Long authorId,

	/**
	 * Title pattern
	 */
	String titleLike,

	/**
	 * Created At Less Than Or Equals
	 */
	Instant createdAtLte,

	/**
	 * Created At Greater Than Or Equals
	 */

	Instant createdAtGte,
	/**
	 * Likes Count Greater Than Or Equals
	 */
	Integer likesGte,
	Pagination pagination
) {

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

	/**
	 * <i>Typically</i>, page number is zero based
	 */
	public record Pagination(
			int pageNumber,
			int pageSize,
			Sort sort
	) { }

	public record Sort(
			String property,
			String direction
	) { }
}
