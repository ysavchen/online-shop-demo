package com.example.bookservice.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "reviews")
data class ReviewEntity(
    @Id
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "title")
    val title: String?,

    @Column(name = "review_text")
    val reviewText: String?,

    @Column(name = "author", nullable = false)
    val author: String,

    @Column(name = "rating", nullable = false)
    val rating: BigDecimal
)