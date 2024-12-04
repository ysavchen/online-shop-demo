package com.example.bookservice.repository.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable

import java.util.*

@Entity
@Immutable
@Table(name = "processed_requests")
data class ProcessedRequestEntity(
    @Id
    @Column(name = "idempotency_key", nullable = false)
    val idempotencyKey: UUID,

    @Column(name = "resource_id", nullable = false)
    val resourceId: UUID,

    @Column(name = "resource_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val resourceType: ResourceTypeEntity
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProcessedRequestEntity

        return idempotencyKey == other.idempotencyKey
    }

    override fun hashCode(): Int {
        return idempotencyKey.hashCode()
    }
}