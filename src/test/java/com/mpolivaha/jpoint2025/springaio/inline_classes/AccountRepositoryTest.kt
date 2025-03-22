package com.mpolivaha.jpoint2025.springaio.inline_classes

import com.mpolivaha.jpoint2025.springaio.AbstractDatabaseTest
import com.mpolivaha.jpoint2025.springaio.inline_classes.Account.ExternalId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(value = [SpringExtension::class])
@ContextConfiguration(classes = [AccountRepositoryTest.AccountRepositoryTestConfig::class])
open class AccountRepositoryTest : AbstractDatabaseTest() {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Configuration
    @EnableJdbcRepositories(basePackageClasses = [AccountRepository::class])
    open class AccountRepositoryTestConfig : RelationalTestConfiguration() {

    }

    @Test
    //language=sql
    @Sql(
        statements = ["""
				CREATE TABLE account(id BIGSERIAL PRIMARY KEY, name TEXT, external_id BIGINT);
				INSERT INTO account(id, name, external_id) VALUES(1, 'name1', 3);
				INSERT INTO account(id, name, external_id) VALUES(2, 'name2', 4);
			"""]
    )
    fun testInlineClasses() {
        val byExternalId = accountRepository.findByExternalId(ExternalId(4L))

        assertThat(byExternalId).isNotEmpty.hasValueSatisfying {
            assertThat(it.name).isEqualTo("name2")
        }
    }
}
