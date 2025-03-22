package com.mpolivaha.jpoint2025.springaio.inline_classes

import com.mpolivaha.jpoint2025.springaio.inline_classes.Account.*
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface AccountRepository : CrudRepository<Account, Long> {

    fun findByExternalId(externalId: ExternalId) : Optional<Account>

    override fun findById(id: Long): Optional<Account>
}
