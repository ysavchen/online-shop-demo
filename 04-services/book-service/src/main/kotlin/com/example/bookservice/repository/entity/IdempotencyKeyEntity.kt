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
    val reviewId: UUID?
)