package com.example.bookservice.repository.entity

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "reviews")
data class ReviewEntity(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "title")
    val title: String?,

    @Column(name = "review_text")
    val reviewText: String?,

    @Column(name = "author", nullable = false)
    val author: String,

    @Column(name = "rating", nullable = false)
    val rating: BigDecimal,

    @Column(name = "book_fk", nullable = false)
    val bookFk: UUID
)