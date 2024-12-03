package com.example.deliveryservice.repository.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable
import java.util.*

@Entity
@Immutable
@Table(name = "processed_messages")
data class ProcessedMessageEntity(
    @Id
    @Column(name = "message_key", nullable = false)
    val messageKey: UUID,

    @Column(name = "resource_id", nullable = false)
    val resourceId: UUID,

    @Column(name = "resource", nullable = false)
    @Enumerated(EnumType.STRING)
    val resource: ResourceEntity
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProcessedMessageEntity

        return messageKey == other.messageKey
    }

    override fun hashCode(): Int {
        return messageKey.hashCode()
    }
}

enum class ResourceEntity {
    DELIVERY
}