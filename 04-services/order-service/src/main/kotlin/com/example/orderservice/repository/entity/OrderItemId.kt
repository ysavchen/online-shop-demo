package com.example.orderservice.repository.entity

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Embeddable
data class OrderItemId(
    @Column(name = "id", nullable = false)
    val id: UUID

) : Serializable {

    /**
     * Set order via parent entity using [OrderEntity.addItems]
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_fk", nullable = false)
    var order: OrderEntity? = null
}