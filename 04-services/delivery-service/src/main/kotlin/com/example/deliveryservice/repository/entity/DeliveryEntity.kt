package com.example.deliveryservice.repository.entity

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "deliveries")
data class DeliveryEntity(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: TypeEntity,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: StatusEntity,

    @Type(JsonBinaryType::class)
    @Column(name = "address", columnDefinition = "jsonb", nullable = false)
    val address: AddressEntity,

    @Column(columnDefinition = "order_id", nullable = false)
    val orderId: UUID,

    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: OffsetDateTime
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeliveryEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}

enum class TypeEntity {
    HOME_DELIVERY,
    IN_STORE_PICKUP
}

enum class StatusEntity {
    /**
     * Создана заявка на доставку заказа
     */
    CREATED,

    /**
     * Заказ доставляется покупателю
     */
    IN_PROGRESS,

    /**
     * Заказ доставлен покупателю
     */
    DELIVERED
}