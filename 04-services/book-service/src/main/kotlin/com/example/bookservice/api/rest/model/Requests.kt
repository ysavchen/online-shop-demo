package com.example.bookservice.api.rest.model

const val IDEMPOTENCY_KEY = "Idempotency-Key"

data class BookRequestParams(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "title",
    val orderBy: String = "asc"
)

data class ReviewRequestParams(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "rating",
    val orderBy: String = "desc"
)