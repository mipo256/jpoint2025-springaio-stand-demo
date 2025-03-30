package com.mpolivaha.jpoint2025.springaio.spel;

import static com.mpolivaha.jpoint2025.springaio.spel.UserRepositoryTest.UserRepositoryTestConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.data.spel.spi.Function;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mpolivaha.jpoint2025.springaio.AbstractDatabaseTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserRepositoryTestConfiguration.class)
class UserRepositoryTest extends AbstractDatabaseTest {

	@Autowired
	private UserRepository userRepository;

	@EnableJdbcRepositories
	@EnableConfigurationProperties
	static class UserRepositoryTestConfiguration extends RelationalTestConfiguration {

		@Bean
		@ConfigurationProperties(prefix = "deployment.user")
		UserProperties userProperties() {
			return new UserProperties(UserProperties.Customer.HAULMOUNT);
		}

		@Bean
		EvaluationContextExtension evaluationContextExtension(UserProperties userProperties) {
			return new EvaluationContextExtension() {

				@Override
				public String getExtensionId() {
					return "userProperties";
				}

				@Override
				public Map<String, Function> getFunctions() {
					try {
						return Map.of("customer", new Function(UserProperties.class.getMethod("getCustomer"), userProperties));
					} catch (NoSuchMethodException e) {
						throw new RuntimeException(e);
					}
				}

				@Override
				public Object getRootObject() {
					return userProperties;
				}
			};
		}
	}

	@Test
	//language=sql
	@Sql(statements = """
				CREATE TABLE IF NOT EXISTS users(id BIGSERIAL PRIMARY KEY, name TEXT, customer TEXT);
				DELETE FROM users;
				INSERT INTO users(id, name, customer) VALUES(1, 'userName', 'HAULMOUNT');
			""")
	void 	testPredefinedMethodById() {
		Optional<User> user = userRepository.findById(1L);

		assertThat(user).isPresent().hasValueSatisfying(it -> {
			assertThat(it.getId()).isEqualTo(1L);
			assertThat(it.getName()).isEqualTo("userName");
		});
	}

	@Test
	//language=sql
	@Sql(statements = """
				CREATE TABLE IF NOT EXISTS users(id BIGSERIAL PRIMARY KEY, name TEXT, customer TEXT);
				DELETE FROM users;
				INSERT INTO users(id, name, customer) VALUES(1, 'userName', 'HAULMOUNT');
			""")
	void testViaSpelExtension() {
		Optional<User> user = userRepository.findByNameSpelExtension("userName");

		assertThat(user).isPresent().hasValueSatisfying(it -> {
			assertThat(it.getId()).isEqualTo(1L);
			assertThat(it.getName()).isEqualTo("userName");
		});
	}

	@Test
	void testDefaultMethodInvocation() {
		assertThatThrownBy(() -> userRepository.findByIdMyCustomMethod(1L)).isInstanceOf(
				UnsupportedOperationException.class);
	}
}
