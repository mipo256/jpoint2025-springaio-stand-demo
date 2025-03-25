package com.mpolivaha.jpoint2025.springaio.inline_classes

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Account(

    @Id
    val id: Long,
//    val externalId: Long,
//    val internalId: Long,
    val externalId: ExternalId,
    val internalId: InternalId,
    val name: String
) {

    @JvmInline
    value class InternalId(val id: Long)

    @JvmInline
    value class ExternalId(val id: Long)
}
