package com.example.orderservice.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.*

@Embeddable
data class OrderItemId(
    @Column(name = "id", nullable = false)
    val id: UUID,

    @Column(name = "order_fk", nullable = false)
    val orderFk: UUID
) : Serializable