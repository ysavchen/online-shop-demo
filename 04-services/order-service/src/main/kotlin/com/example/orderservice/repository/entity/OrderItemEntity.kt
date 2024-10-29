package com.example.orderservice.repository.entity

import jakarta.persistence.*

@Entity
@Table(name = "order_items")
data class OrderItemEntity(
    @EmbeddedId
    val orderItemId: OrderItemId,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: ItemCategoryEntity,

    @Column(name = "quantity", nullable = false)
    val quantity: Int,

    @Embedded
    val price: ItemPriceEntity
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderItemEntity

        return orderItemId == other.orderItemId
    }

    override fun hashCode(): Int {
        return orderItemId.hashCode()
    }
}

enum class ItemCategoryEntity {
    BOOKS
}