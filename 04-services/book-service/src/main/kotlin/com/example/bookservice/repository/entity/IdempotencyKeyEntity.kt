package com.example.bookservice.repository.entity

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
    @Column(name = "idempotency_key", nullable = false)
    val idempotencyKey: UUID,

    @Column(name = "book_id")
    val bookId: UUID?,

    @Column(name = "review_id")
    val reviewId: UUID?,

    @Column(name = "order_id")
    val orderId: UUID?
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