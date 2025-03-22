package com.mpolivaha.jpoint2025.springaio.inline_classes

import org.springframework.data.relational.core.mapping.Table

@Table
data class Account(
    val id: InternalId,
    val externalId: ExternalId,
    val name: String
) {

    @JvmInline
    value class InternalId(val id: Long)

    @JvmInline
    value class ExternalId(val id: Long)
}
