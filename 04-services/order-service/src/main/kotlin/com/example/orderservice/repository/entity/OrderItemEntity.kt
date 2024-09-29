package com.example.orderservice.repository.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "order_items")
data class OrderItemEntity(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: ItemCategoryEntity,

    @Column(name = "quantity", nullable = false)
    val quantity: Int,

    @Embedded
    val price: ItemPriceEntity
) {

    /**
     * Set order via the parent entity using method [OrderEntity.addItems]
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_fk", nullable = false)
    var order: OrderEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderItemEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

enum class ItemCategoryEntity {
    BOOKS
}