package com.example.bookservice.api.rest.model

import java.math.BigDecimal
import java.util.*

data class ReviewSearchRequest(
    val bookId: UUID
)

data class CreateReviewRequest(
    val title: String?,
    val reviewText: String?,
    val author: String,
    val rating: BigDecimal,
    val bookId: UUID
)

data class Review(
    val id: UUID,
    val title: String?,
    val reviewText: String?,
    val author: String,
    val rating: BigDecimal,
    val bookId: UUID
)