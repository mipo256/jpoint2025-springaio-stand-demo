package com.mpolivaha.jpoint2025.springaio.spel;

public record UserProperties(
		Customer customer
) {

	/**
	 * Unfortunately, using enums as eventual evaluation values is kind of buggy right now, so we have to convert it via {@link Enum#name()} for now
	 *
	 * @see <a href="https://github.com/spring-projects/spring-data-relational/issues/2007">Corresponding issue in the Spring Data bugtracker</a>
	 */
	public String getCustomer() {
		return customer.name();
	}

	enum Customer {
		HAULMOUNT,
		AXOIM_JDK,
		ASTRA_LINUX
	}
}
