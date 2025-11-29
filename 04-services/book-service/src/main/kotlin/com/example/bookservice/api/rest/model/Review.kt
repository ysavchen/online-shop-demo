package com.example.bookservice.api.rest.model

import com.example.online.shop.model.Author
import com.example.online.shop.model.Rating
import com.example.online.shop.model.ReviewText
import com.example.online.shop.model.Title
import java.util.*

data class ReviewSearchRequest(
    val bookId: UUID
)

data class CreateReviewRequest(
    val title: Title?,
    val reviewText: ReviewText?,
    val author: Author,
    val rating: Rating,
    val bookId: UUID
)

data class Review(
    val id: UUID,
    val title: Title?,
    val reviewText: ReviewText?,
    val author: Author,
    val rating: Rating,
    val bookId: UUID
)