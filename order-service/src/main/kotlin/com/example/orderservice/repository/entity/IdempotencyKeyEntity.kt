package com.example.orderservice.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.util.*

@Entity
@Immutable
@Table(name = "idempotency_keys")
data class IdempotencyKeyEntity(
    @Id
    @Column(name = "idempotency_key")
    val idempotencyKey: UUID,

    @Column(name = "order_id")
    val orderId: UUID
)