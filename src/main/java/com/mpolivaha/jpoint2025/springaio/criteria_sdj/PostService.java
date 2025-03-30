package com.mpolivaha.jpoint2025.springaio.criteria_sdj;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

	private final JdbcAggregateTemplate jdbcAggregateTemplate;

	public PostService(JdbcAggregateTemplate jdbcAggregateTemplate) {
		this.jdbcAggregateTemplate = jdbcAggregateTemplate;
	}

	@Transactional(readOnly = true)
	public Page<Post> findDynamic(PostSearchForm query) {
		Criteria criteria = null;

		criteria = eq(criteria, "authorId", query.authorId());
		criteria = gte(criteria, "likes", query.likesGte());
		criteria = gte(criteria, "createdAt", query.createdAtGte());
		criteria = lte(criteria, "createdAt", query.createdAtLte());
		criteria = like(criteria, "title", query.titleLike());

		return jdbcAggregateTemplate.findAll(
				Query.query(criteria),
				Post.class,
				query.toPageable()
		);
	}

	/**
	 * In the end the passed {@code value} will be rendered via {@link Object#toString() toString call}
	 *
	 * @implNote see {@link Criteria#renderValue(Object)}
	 */
	private <T> Criteria eq(Criteria criteria, String columnName, T value) {
		if (value == null) {
			return criteria;
		}

		return Optional
				.ofNullable(criteria)
				.map(forwardingChain -> forwardingChain.and(columnName).is(value))
				.orElseGet(() -> Criteria.where(columnName).is(value));
	}

	private <T> Criteria like(Criteria criteria, String columnName, T value) {
		if (value == null) {
			return criteria;
		}

		return Optional
				.ofNullable(criteria)
				.map(forwardingChain -> forwardingChain.and(columnName).like("%" + value + "%")) // Important! The like needs to be explicit!
				.orElseGet(() -> Criteria.where(columnName).like("%" + value + "%"));
	}

	private <T> Criteria gte(Criteria criteria, String columnName, T value) {
		if (value == null) {
			return criteria;
		}

		return Optional
				.ofNullable(criteria)
				.map(forwardingChain -> forwardingChain.and(columnName).greaterThanOrEquals(value))
				.orElseGet(() -> Criteria.where(columnName).greaterThanOrEquals(value));
	}

	private <T> Criteria lte(Criteria criteria, String columnName, T value) {
		if (value == null) {
			return criteria;
		}

		return Optional
				.ofNullable(criteria)
				.map(forwardingChain -> forwardingChain.and(columnName).lessThanOrEquals(value))
				.orElseGet(() -> Criteria.where(columnName).lessThanOrEquals(value));
	}
}
