package com.example.bookservice.repository.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable

import java.util.*

@Entity
@Immutable
@Table(name = "idempotency_keys")
data class IdempotencyKeyEntity(
    @Id
    @Column(name = "idempotency_key", nullable = false)
    val idempotencyKey: UUID,

    @Column(name = "resource_id", nullable = false)
    val resourceId: UUID,

    @Column(name = "resource", nullable = false)
    @Enumerated(EnumType.STRING)
    val resource: ResourceEntity
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IdempotencyKeyEntity

        return idempotencyKey == other.idempotencyKey
    }

    override fun hashCode(): Int {
        return idempotencyKey.hashCode()
    }
}

enum class ResourceEntity {
    BOOK, REVIEW, ORDER
}