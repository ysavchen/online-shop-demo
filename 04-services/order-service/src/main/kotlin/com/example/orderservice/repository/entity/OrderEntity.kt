package com.example.orderservice.repository.entity

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: StatusEntity,

    @Column(name = "total_quantity", nullable = false)
    val totalQuantity: Int,

    @Embedded
    val totalPrice: TotalPriceEntity,

    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,

    @Version
    @Column(name = "updated_at", nullable = false)
    val updatedAt: OffsetDateTime
) {

    @OneToMany(mappedBy = "orderItemId.order", cascade = [CascadeType.PERSIST, CascadeType.MERGE], orphanRemoval = true)
    val items: MutableSet<OrderItemEntity> = mutableSetOf()

    fun addItems(itemEntities: Set<OrderItemEntity>): OrderEntity {
        itemEntities.forEach { item ->
            this.items.add(item)
            item.orderItemId.order = this
        }
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}

enum class StatusEntity {
    /**
     * Заказ создан в системе
     */
    CREATED,

    /**
     * Заказ взят в обработку
     */
    IN_PROGRESS,

    /**
     * Заказ отклонен системой, например, из-за недостатка средств
     */
    DECLINED,

    /**
     * Заказ отменен пользователем
     */
    CANCELLED,

    /**
     * Заказ доставлен пользователю
     */
    DELIVERED
}